package ru.practicum.statsserver.utils;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.statsserver.models.EndpointHit;

public class StatsMapper {
    public static EndpointHitDto toEndpointHitDto(EndpointHit hit) {
        return EndpointHitDto.builder()
                .id(hit.getId())
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp())
                .build();
    }
}
