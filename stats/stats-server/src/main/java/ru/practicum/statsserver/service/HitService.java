package ru.practicum.statsserver.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.statsserver.models.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {
    EndpointHitDto createHit(EndpointHit hit);

    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> urls, boolean unique);
}
