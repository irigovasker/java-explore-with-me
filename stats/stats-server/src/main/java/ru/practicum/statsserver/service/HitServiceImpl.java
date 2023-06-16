package ru.practicum.statsserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.statsserver.models.EndpointHit;
import ru.practicum.statsserver.repositories.HitRepository;
import ru.practicum.statsserver.utils.StatsMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {
    private final HitRepository repository;

    @Override
    public EndpointHitDto createHit(EndpointHit hit) {
        return StatsMapper.toEndpointHitDto(repository.save(hit));
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> urls, boolean unique) {
        if (urls == null) {
            if (unique) {
                return repository.findUniqueStatsWithoutUris(start, end);
            } else {
                return repository.findStatsWithoutUris(start, end);
            }
        } else {
            if (unique) {
                return repository.findUniqueIpStatWithUris(start, end, urls);
            } else {
                return repository.findStatsWithUris(start, end, urls);
            }
        }
    }
}
