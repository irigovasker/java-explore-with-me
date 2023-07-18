package ru.practicum.dto.event;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CommentDto {
    private Long id;
    @NotBlank
    @Size(max = 500, message = "Comment length should be less than 500")
    private String text;
    private Long authorId;
    private Long eventId;
    private LocalDateTime createdAt;
}
