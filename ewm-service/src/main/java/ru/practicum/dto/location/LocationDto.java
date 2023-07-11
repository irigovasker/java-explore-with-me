package ru.practicum.dto.location;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    private Float lat;
    private Float lon;
}
