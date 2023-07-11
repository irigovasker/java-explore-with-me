package ru.practicum.dto.user;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class NewUserDto {
    public Long id;
    @NotBlank
    @Length(min = 2, max = 250)
    public String name;
    @Email
    @NotBlank
    @Length(min = 6, max = 254)
    public String email;
}
