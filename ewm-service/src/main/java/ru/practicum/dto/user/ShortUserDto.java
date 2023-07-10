package ru.practicum.dto.user;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ShortUserDto {
    private Long id;
    private String name;
}
