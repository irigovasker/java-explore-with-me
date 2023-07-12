package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.FullEventDto;
import ru.practicum.dto.event.ShortEventDto;
import ru.practicum.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.util.Util.YYYY_MM_DD_HH_MM_SS;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class PublicEventController {
    private final EventService eventsService;

    @GetMapping
    public List<ShortEventDto> getEventsWithFilters(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "paid", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", required = false) @DateTimeFormat(pattern = YYYY_MM_DD_HH_MM_SS) LocalDateTime rangeStart,
            @RequestParam(name = "rangeEnd", required = false) @DateTimeFormat(pattern = YYYY_MM_DD_HH_MM_SS) LocalDateTime rangeEnd,
            @RequestParam(name = "onlyAvailable", required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            HttpServletRequest request) {
        return eventsService.getEventsForPublic(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, PageRequest.of(from, size), request);
    }

    @GetMapping("{eventId}")
    public FullEventDto getEventWithFullInfoById(
            @PathVariable("eventId") Long eventId, HttpServletRequest request) {
        return eventsService.getFullEventDtoByIdForPublic(eventId, request);
    }
}
