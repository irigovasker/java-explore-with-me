package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.FullEventDto;
import ru.practicum.dto.event.UpdateEventDto;
import ru.practicum.service.event.EventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.util.Util.YYYY_MM_DD_HH_MM_SS;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class AdminEventController {
    private final EventService eventsService;

    @GetMapping
    public List<FullEventDto> searchEventsForAdmin(
            @RequestParam(name = "users", required = false) List<Long> users,
            @RequestParam(name = "states", required = false) List<String> states,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "rangeStart", required = false) @DateTimeFormat(pattern = YYYY_MM_DD_HH_MM_SS)
            LocalDateTime rangeStart,
            @RequestParam(name = "rangeEnd", required = false) @DateTimeFormat(pattern = YYYY_MM_DD_HH_MM_SS)
            LocalDateTime rangeEnd,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return eventsService.getEventsForAdmin(users, states, categories, rangeStart, rangeEnd, PageRequest.of(from, size));
    }

    @PatchMapping("/{eventId}")
    public FullEventDto updateByAdmin(@PathVariable Long eventId,
                                                      @RequestBody @Valid UpdateEventDto dto) {
        return eventsService.updateByAdmin(eventId, dto);
    }
}
