package ru.practicum.dto.compilation;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.dto.event.ShortEventDto;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CompilationDto {
    private Long id;
    private List<ShortEventDto> events;
    private Boolean pinned;
    @NotBlank
    @Length(max = 50)
    private String title;
}
