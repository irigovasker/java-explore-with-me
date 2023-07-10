package ru.practicum.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.event.FullEventDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.ShortEventDto;
import ru.practicum.dto.event.UpdateEventDto;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.BadStateException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.event.EventMapper;
import ru.practicum.mapper.location.LocationMapper;
import ru.practicum.mapper.request.RequestMapper;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.Sort;
import ru.practicum.model.event.State;
import ru.practicum.model.event.StateAction;
import ru.practicum.model.request.Request;
import ru.practicum.repository.event.EventRepository;
import ru.practicum.repository.location.LocationRepository;
import ru.practicum.repository.request.RequestRepository;
import ru.practicum.service.category.CategoryService;
import ru.practicum.service.user.UserService;
import ru.practicum.statsclient.StatsClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static ru.practicum.util.Util.*;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@ComponentScan(basePackages = {"ru.practicum.statsclient"})
public class EventServiceImpl implements EventService {
    private final StatsClient statsClient;
    private final EventMapper eventMapper;
    private final UserService userService;
    private final RequestMapper requestMapper;
    private final LocationMapper locationMapper;
    private final CategoryService categoryService;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;

    @Transactional
    public FullEventDto createEvent(long userId, NewEventDto dto) {
        LocalDateTime now = LocalDateTime.now();
        checkDateTimeBeforeEventDate(now, dto.getEventDate());
        dto.setPaid(ofNullable(dto.getPaid()).orElse(false));
        dto.setParticipantLimit(ofNullable(dto.getParticipantLimit()).orElse(0L));
        dto.setRequestModeration(ofNullable(dto.getRequestModeration()).orElse(true));

        locationRepository.save(locationMapper.toLocation(getLocationFromDto(dto)));
        return eventMapper.toEventFullDto(
                eventRepository.save(
                        eventMapper.toEvent(
                                dto,
                                categoryService.getCategoryByIdOrThrow(dto.getCategory()),
                                userService.getUserByIdOrElseThrow(userId),
                                now
                        )
                )
        );
    }

    public List<ShortEventDto> getEventsCreatedByCreator(long userId, PageRequest pageRequest) {
        userService.getUserByIdOrElseThrow(userId);
        List<Event> events = eventRepository.findEventsByInitiator_Id(userId, pageRequest);
        return putViewsInEvents(events).stream().map(eventMapper::toEventShortDto).collect(Collectors.toList());
    }

    public FullEventDto getFullEventDtoByIdForPrivate(long userId, long eventId) {
        userService.getUserByIdOrElseThrow(userId);
        return eventMapper.toEventFullDto(putViewsInEvent(getEventByIdOrElseThrow(eventId)));
    }

    @Transactional
    public FullEventDto updateEventCreatedByCurrentUser(long userId, long eventId, UpdateEventDto dto) {
        Event event = getEventByIdOrElseThrow(eventId);
        userService.getUserByIdOrElseThrow(userId);

        if (dto.getEventDate() != null) {
            checkDateTimeBeforeEventDate(LocalDateTime.now(), dto.getEventDate());
        }
        if (!(event.getState().equals(State.CANCELED) || event.getState().equals(State.PENDING))) {
            throw new BadStateException("Invalid state=" + event.getState() + "." +
                    " It allowed to change events with CANCELED ot PENDING state");
        }
        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(State.CANCELED);
                    break;
                default:
                    throw new BadStateException("Invalid StateAction status");
            }
        }

        Event result = eventRepository.save(updateEventFields(event, dto));
        locationRepository.save(result.getLocation());
        return eventMapper.toEventFullDto(putViewsInEvent(result));
    }

    public List<FullEventDto> getEventsForAdmin(List<Long> users, List<String> states,
                                                List<Long> categories, LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd, PageRequest pageRequest) {
        return putViewsInEvents(
                eventRepository.getEventsWithUsersStatesCategoriesDateTime(
                        users,
                        states == null ? null : states.stream().map(State::valueOf).collect(Collectors.toList()),
                        categories,
                        rangeStart,
                        rangeEnd,
                        pageRequest
                )
        ).stream().map(eventMapper::toEventFullDto).collect(Collectors.toList());
    }

    @Transactional
    public FullEventDto updateByAdmin(long eventId, UpdateEventDto dto) {
        Event event = getEventByIdOrElseThrow(eventId);

        if (dto.getEventDate() != null) {
            if (LocalDateTime.now().plusHours(1).isAfter(dto.getEventDate())) {
                throw new BadRequestException("Error. " +
                        "Datetime of event cannot be earlier than one hour before present time");
            }
        } else {
            if (dto.getStateAction() != null) {
                if (dto.getStateAction().equals(StateAction.PUBLISH_EVENT) &&
                        LocalDateTime.now().plusHours(1).isAfter(event.getEventDate())) {
                    throw new BadStateException("Error. " +
                            "Datetime of event cannot be earlier than one hour before present time");
                }
                if (dto.getStateAction().equals(StateAction.PUBLISH_EVENT) && !(event.getState().equals(State.PENDING))) {
                    throw new BadStateException("Invalid StateAction. Event can be published only with PENDING state");
                }
                if (dto.getStateAction().equals(StateAction.REJECT_EVENT) && event.getState().equals(State.PUBLISHED)) {
                    throw new BadStateException("Invalid StateAction. Event can be cancelled only with unpublished state");
                }
            }
        }
        if (dto.getStateAction() != null) {
            updateStateAction(event, dto);
        }
        return eventMapper.toEventFullDto(
                putViewsInEvent(
                        eventRepository.save(
                                updateEventFields(event, dto)
                        )
                )
        );
    }

    @Transactional
    public List<ShortEventDto> getEventsForPublic(String text, List<Long> categories, Boolean paid,
                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                  Boolean onlyAvailable, String sort, PageRequest pageRequest,
                                                  HttpServletRequest request) {
        checkDateTime(rangeStart, rangeEnd);
        List<Event> events = new ArrayList<>();
        addStats(request);

        if (onlyAvailable) {
            if (sort == null) {
                events = eventRepository.getAvailableEventsWithFiltersDateSorted(
                        text, State.PUBLISHED, categories, paid, rangeStart, rangeEnd, pageRequest);
            } else {
                switch (Sort.valueOf(sort)) {
                    case EVENT_DATE:
                        return toShortEventDtosList(
                                eventRepository.getAvailableEventsWithFiltersDateSorted(
                                        text, State.PUBLISHED, categories, paid, rangeStart, rangeEnd, pageRequest
                                )
                        );
                    case VIEWS:
                        return toShortEventDtosListOrderByViews(
                                putViewsInEvents(
                                        eventRepository.getAvailableEventsWithFilters(
                                                text, State.PUBLISHED, categories, paid, rangeStart, rangeEnd, pageRequest
                                        )
                                )
                        );
                }
            }
        } else {
            if (sort == null) {
                events = eventRepository.getAllEventsWithFiltersDateSorted(
                        text, State.PUBLISHED, categories, paid, rangeStart, rangeEnd, pageRequest);
            } else {
                switch (Sort.valueOf(sort)) {
                    case EVENT_DATE:
                        return toShortEventDtosList(
                                eventRepository.getAllEventsWithFiltersDateSorted(
                                        text, State.PUBLISHED, categories, paid, rangeStart, rangeEnd, pageRequest
                                )
                        );
                    case VIEWS:
                        return toShortEventDtosListOrderByViews(
                                putViewsInEvents(
                                        eventRepository.getAllEventsWithFilters(
                                                text, State.PUBLISHED, categories, paid, rangeStart, rangeEnd, pageRequest
                                        )
                                )
                        );
                }
            }
        }
        return toShortEventDtosList(events);
    }

    @Transactional
    public FullEventDto getFullEventDtoByIdForPublic(long eventId, HttpServletRequest request) {
        Event event = getEventByIdOrElseThrow(eventId);
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("Event is not published");
        }
        addStats(request);
        return eventMapper.toEventFullDto(putViewsInEvent(event));
    }

    public List<ParticipationRequestDto> getRequestsMadeByUserForEvent(Long userId, Long eventId) {
        userService.getUserByIdOrElseThrow(userId);
        getEventByIdOrElseThrow(eventId);
        return requestRepository.findByEventId(eventId).stream().map(requestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Transactional
    public EventRequestStatusUpdateResult changeRequestsStatus(long userId, long eventId, EventRequestStatusUpdateRequest dto) {
        userService.getUserByIdOrElseThrow(userId);
        Event event = getEventByIdOrElseThrow(eventId);

        if (!event.getRequestModeration() || event.getParticipantLimit().equals(0L)) {
            throw new ConflictException("Confirmation is not necessary");
        }

        long limitBalance = event.getParticipantLimit() - event.getConfirmedRequests();
        if (event.getParticipantLimit() != 0 && limitBalance <= 0) {
            throw new ConflictException("Event has reached participation limit");
        }

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        List<Request> requests = findRequests(dto.getRequestIds());
        if (State.valueOf(dto.getStatus()) == State.REJECTED) {
            requests.forEach(el -> {
                el.setStatus(State.REJECTED);
                rejectedRequests.add(requestMapper.toRequestDto(el));
            });
        } else {
            for (Request request : requests) {
                if (limitBalance != 0) {
                    if (request.getStatus().equals(State.PENDING)) {
                        request.setStatus(State.CONFIRMED);
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                        confirmedRequests.add(requestMapper.toRequestDto(request));
                        limitBalance--;
                    }
                } else {
                    if (request.getStatus().equals(State.PENDING)) {
                        request.setStatus(State.REJECTED);
                        rejectedRequests.add(requestMapper.toRequestDto(request));
                    }
                }
            }
        }
        requestRepository.saveAll(requests);
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }

    private Event getEventByIdOrElseThrow(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " not found"));
    }

    private List<Request> findRequests(List<Long> ids) throws NotFoundException {
        List<Request> result = requestRepository.findRequestsByIdIn(ids);
        if (result.size() != ids.size()) {
            Set<Long> set = new HashSet<>(ids);
            result.stream().mapToLong(Request::getId).forEach(set::remove);
            throw new NotFoundException("Request with id=" + set.stream().findFirst().orElse(0L) + " not found");
        }
        return result;
    }

    private void addStats(HttpServletRequest request) {
        statsClient.sendEndpointHit(EndpointHitDto.builder()
                .app(APP_NAME)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }

    private Map<Long, Long> getStatisticFromListEvents(List<Event> events) {
        return toIdViewsMap(
                statsClient.getViewStats(
                        LocalDateTime.of(1950, 1, 1, 1, 1).format(DATE_TIME_FORMATTER),
                        LocalDateTime.now().format(DATE_TIME_FORMATTER),
                        events.stream().map(Event::getId).map(id -> EVENTS_URI + id).collect(Collectors.toList()),
                        true
                )
        );
    }

    private void checkDateTime(LocalDateTime start, LocalDateTime end) {
        if (start == null) {
            start = LocalDateTime.of(1950, 1, 1, 1, 1);
        }
        if (end == null) {
            end = LocalDateTime.now();
        }
        if (start.isAfter(end)) {
            throw new BadRequestException("Некорректный запрос. Дата окончания события задана позже даты старта");
        }
    }

    private Event updateEventFields(Event event, UpdateEventDto dto) {
        ofNullable(dto.getPaid()).ifPresent(event::setPaid);
        ofNullable(dto.getTitle()).ifPresent(event::setTitle);
        ofNullable(dto.getEventDate()).ifPresent(event::setEventDate);
        ofNullable(dto.getAnnotation()).ifPresent(event::setAnnotation);
        ofNullable(dto.getDescription()).ifPresent(event::setDescription);
        ofNullable(dto.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        ofNullable(dto.getRequestModeration()).ifPresent(event::setRequestModeration);
        ofNullable(dto.getLocation()).ifPresent(locationDto -> event.setLocation(locationMapper.toLocation(locationDto)));
        ofNullable(dto.getCategory()).ifPresent(category -> event.setCategory(categoryService.getCategoryByIdOrThrow(category)));
        return event;
    }

    private void checkDateTimeBeforeEventDate(LocalDateTime nowDateTime, LocalDateTime dtoDateTime) {
        if (nowDateTime.plusHours(2).isAfter(dtoDateTime)) {
            throw new BadRequestException("Error. Datetime of event cannot be earlier than 2 hours after current time");
        }
    }


    private LocationDto getLocationFromDto(NewEventDto dto) {
        if (dto.getLocation() == null) {
            return null;
        } else {
            return dto.getLocation();
        }
    }

    private Map<Long, Long> toIdViewsMap(List<ViewStatsDto> viewStatsDtos) {
        return viewStatsDtos.stream().collect(Collectors.toMap(
                viewStatsDto -> Long.parseLong(viewStatsDto.getUri().substring(EVENTS_URI.length())),
                ViewStatsDto::getHits
        ));
    }

    private List<Event> putViewsInEvents(List<Event> events, Map<Long, Long> hits) {
        events.forEach(event -> event.setViews(hits.getOrDefault(event.getId(), 0L)));
        return events;
    }

    private List<Event> putViewsInEvents(List<Event> events) {
        return putViewsInEvents(events, getStatisticFromListEvents(events));
    }

    private Event putViewsInEvent(Event event) {
        return putViewsInEvents(List.of(event)).get(0);
    }

    private void updateStateAction(Event event, UpdateEventDto dto) {
        switch (dto.getStateAction()) {
            case REJECT_EVENT:
                event.setState(State.CANCELED);
                break;
            case PUBLISH_EVENT:
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                break;
            default:
                throw new BadStateException("Invalid StateAction of dto.");
        }
    }

    private List<ShortEventDto> toShortEventDtosList(List<Event> events) {
        return events.stream().map(eventMapper::toEventShortDto).collect(Collectors.toList());
    }

    private List<ShortEventDto> toShortEventDtosListOrderByViews(List<Event> events) {
        return events.stream().map(eventMapper::toEventShortDto)
                .sorted(Comparator.comparing(ShortEventDto::getViews))
                .collect(Collectors.toList());
    }
}
