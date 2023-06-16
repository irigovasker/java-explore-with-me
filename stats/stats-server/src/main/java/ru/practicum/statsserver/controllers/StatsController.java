package ru.practicum.statsserver.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.statsserver.models.EndpointHit;
import ru.practicum.statsserver.service.HitService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController("/")
@RequiredArgsConstructor
public class StatsController {
    private final HitService hitService;

    @PostMapping(value = "hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto createHit(@Valid @RequestBody EndpointHit hit) {
        return hitService.createHit(hit);
    }

    @GetMapping(value = "stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStatsDto> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                       @RequestParam(required = false) List<String> uris,
                                       @RequestParam(defaultValue = "false") Boolean unique) {
        return hitService.getStats(start, end, uris, unique);
    }

}
