package ru.practicum.statsserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.statsserver.models.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<EndpointHit, Integer> {
    @Query(value = "SELECT new ru.practicum.dto.ViewStatsDto (h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM EndpointHit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (DISTINCT h.ip) DESC")
    List<ViewStatsDto> findUniqueStatsWithoutUris(@Param("start") LocalDateTime start,
                                                  @Param("end") LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.dto.ViewStatsDto (h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM EndpointHit h " +
            "WHERE h.uri IN :urls AND h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (DISTINCT h.ip) DESC")
    List<ViewStatsDto> findUniqueIpStatWithUris(@Param(value = "start") LocalDateTime start,
                                             @Param(value = "end") LocalDateTime end,
                                             @Param(value = "urls") List<String> urls);

    @Query(value = "SELECT new ru.practicum.dto.ViewStatsDto (h.app, h.uri, COUNT(h.id)) " +
            "FROM EndpointHit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (h.id) DESC")
    List<ViewStatsDto> findStatsWithoutUris(@Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.dto.ViewStatsDto (h.app, h.uri, COUNT(h.id)) " +
            "FROM EndpointHit h " +
            "WHERE h.uri IN :urls AND h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (h.id) DESC")
    List<ViewStatsDto> findStatsWithUris(@Param(value = "start") LocalDateTime start,
                                      @Param(value = "end") LocalDateTime end,
                                      @Param(value = "urls") List<String> urls);
}