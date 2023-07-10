package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.dto.user.ShortUserDto;

import java.time.LocalDateTime;

import static ru.practicum.util.Util.YYYY_MM_DD_HH_MM_SS;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ShortEventDto {
    private Long id;
    private String annotation;
    private NewCategoryDto category;
    private Long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime eventDate;
    private ShortUserDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
