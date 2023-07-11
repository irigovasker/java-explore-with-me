package ru.practicum.dto.compilation;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @NotBlank
    @Length(max = 50)
    private String title;
}
