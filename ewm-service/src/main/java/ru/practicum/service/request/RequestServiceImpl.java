package ru.practicum.service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.request.RequestMapper;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.State;
import ru.practicum.model.request.Request;
import ru.practicum.model.user.User;
import ru.practicum.repository.event.EventRepository;
import ru.practicum.repository.request.RequestRepository;
import ru.practicum.service.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserService userService;
    private final RequestMapper requestMapper;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Transactional
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        if (userId == null || eventId == null) {
            throw new BadRequestException("Bad request: userId or eventId is null");
        }

        User user = userService.getUserByIdOrElseThrow(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " not found"));

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Initiator with id=" + userId + " " +
                    "cannot request participation in own event with id=" + eventId);
        }
        if (event.getState().equals(State.PENDING) || event.getState().equals(State.CANCELED)) {
            throw new ConflictException("Cannot participate in unpublished event with id=" + eventId);
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new ConflictException("Event with id=" + eventId + " has reached the participation limit");
        }

        Request request = Request.builder()
                .eventId(eventId)
                .created(LocalDateTime.now())
                .requester(user)
                .build();

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(State.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            request.setStatus(State.PENDING);
        }

        return requestMapper.toRequestDto(requestRepository.save(request));
    }

    public List<ParticipationRequestDto> getRequestsByCreatorId(long userId) {
        return requestRepository.findByRequester(userService.getUserByIdOrElseThrow(userId))
                .stream().map(requestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Transactional
    public ParticipationRequestDto cancelRequestByCreator(long userId, long requestId) {
        userService.getUserByIdOrElseThrow(userId);
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + requestId + " not found"));
        request.setStatus(State.CANCELED);
        return requestMapper.toRequestDto(requestRepository.save(request));
    }
}
