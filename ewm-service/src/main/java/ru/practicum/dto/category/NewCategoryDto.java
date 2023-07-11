package ru.practicum.dto.category;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class NewCategoryDto {
    private Long id;
    @NotBlank
    @Length(max = 50)
    private String name;
}
