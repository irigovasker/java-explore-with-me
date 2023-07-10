package ru.practicum.service.event;

import org.springframework.data.domain.PageRequest;
import ru.practicum.dto.event.FullEventDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.ShortEventDto;
import ru.practicum.dto.event.UpdateEventDto;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    FullEventDto createEvent(long userId, NewEventDto dto);

    FullEventDto updateByAdmin(long eventId, UpdateEventDto dto);

    FullEventDto getFullEventDtoByIdForPrivate(long userId, long eventId);

    FullEventDto getFullEventDtoByIdForPublic(long id, HttpServletRequest request);

    List<ShortEventDto> getEventsCreatedByCreator(long userId, PageRequest pageRequest);

    List<ParticipationRequestDto> getRequestsMadeByUserForEvent(Long userId, Long eventId);

    FullEventDto updateEventCreatedByCurrentUser(long userId, long eventId, UpdateEventDto dto);

    List<FullEventDto> getEventsForAdmin(List<Long> users, List<String> states, List<Long> categories,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest pageRequest);

    List<ShortEventDto> getEventsForPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, Boolean onlyAvailable, String sort,
                                           PageRequest pageRequest, HttpServletRequest request);

    EventRequestStatusUpdateResult changeRequestsStatus(long userId, long eventId, EventRequestStatusUpdateRequest dto);
}
