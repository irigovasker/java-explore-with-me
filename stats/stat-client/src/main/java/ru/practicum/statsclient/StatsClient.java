package ru.practicum.statsclient;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.util.Collections;
import java.util.List;

public class StatsClient {
    private final WebClient webClient;

    public StatsClient(String statsBaseUrl) {
        webClient = WebClient.builder()
                .baseUrl(statsBaseUrl)
                .build();
    }

    public EndpointHitDto sendEndpointHit(EndpointHitDto dto) {
        return webClient
                .post()
                .uri("/hit")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(dto), EndpointHitDto.class)
                .retrieve()
                .bodyToMono(EndpointHitDto.class)
                .block();
    }

    public List<ViewStatsDto> getViewStats(String start, String end, List<String> urls, boolean unique) {
        return Collections.singletonList(webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", urls)
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .bodyToMono(ViewStatsDto.class)
                .block());
    }
}
