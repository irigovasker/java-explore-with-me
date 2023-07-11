package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.dto.user.ShortUserDto;
import ru.practicum.model.event.State;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static ru.practicum.util.Util.YYYY_MM_DD_HH_MM_SS;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class FullEventDto {
    private Long id;
    private String annotation;
    private NewCategoryDto category;
    private String description;
    private Long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime eventDate;
    private ShortUserDto initiator;
    private LocationDto location;
    private Boolean paid;
    private Long participantLimit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private State state;
    @NotNull
    @Length(min = 3, max = 120)
    private String title;
    private Long views;
}
