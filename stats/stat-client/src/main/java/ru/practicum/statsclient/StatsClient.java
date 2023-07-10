package ru.practicum.statsclient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.util.List;
import java.util.Map;

public class StatsClient extends BaseClient {
    private final ObjectMapper mapper;

    public StatsClient(@Value("${stats-server.url}") String serverUrl, ObjectMapper mapper) {
        super(
                new RestTemplateBuilder()
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
        this.mapper = mapper;
    }

    public void sendEndpointHit(EndpointHitDto dto) {
        post("/hit", dto);
    }

    public List<ViewStatsDto> getViewStats(String start, String end, List<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", String.join(",", uris),
                "unique", unique
        );
        ResponseEntity<Object> res = get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
        return mapper.convertValue(res.getBody(), new TypeReference<>() {
        });
    }
}