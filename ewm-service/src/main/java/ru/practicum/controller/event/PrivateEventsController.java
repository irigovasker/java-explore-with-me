package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.*;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.event.EventService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventsController {
    private final EventService eventsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FullEventDto createEvent(@PathVariable("userId") Long userId, @RequestBody @Valid NewEventDto dto) {
        return eventsService.createEvent(userId, dto);
    }

    @GetMapping
    public List<ShortEventDto> findEventsCreatedByUser(@PathVariable("userId") Long userId,
                                                       @RequestParam(defaultValue = "0") Integer from,
                                                       @RequestParam(defaultValue = "10") Integer size) {
        return eventsService.getEventsCreatedByCreator(userId, PageRequest.of(from, size));
    }

    @GetMapping("/{eventId}")
    public FullEventDto getFullEventByIdCreatedByUser(@PathVariable("userId") Long userId,
                                                                      @PathVariable("eventId") Long eventId) {
        return eventsService.getFullEventDtoByIdForPrivate(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public FullEventDto updateEventPrivate(@PathVariable("userId") Long userId,
                                           @PathVariable("eventId") Long eventId,
                                           @RequestBody @Valid UpdateEventDto dto) {
        return eventsService.updateEventCreatedByCurrentUser(userId, eventId, dto);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> findRequestsMadeByUserForEvent(@PathVariable("userId") Long userId,
                                                                        @PathVariable("eventId") Long eventId) {
        return eventsService.getRequestsMadeByUserForEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestsStatus(
            @PathVariable("userId") Long userId,
            @PathVariable("eventId") Long eventId,
            @RequestBody EventRequestStatusUpdateRequest dto) {
        return eventsService.changeRequestsStatus(userId, eventId, dto);
    }

    @PostMapping("/{eventId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable("userId") Long userId, @PathVariable("eventId") Long eventId,
                                    @RequestBody @Valid CommentDto commentDto) {
        return eventsService.createComment(userId, eventId, commentDto);
    }
}
