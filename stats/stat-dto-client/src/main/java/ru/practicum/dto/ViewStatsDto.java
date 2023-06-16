package ru.practicum.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class ViewStatsDto {
    private String app;
    private String uri;
    private long hits;

    public ViewStatsDto(String app, String uri, long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
