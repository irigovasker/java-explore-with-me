package ru.practicum.dto.request;

import lombok.*;
import ru.practicum.model.event.State;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ParticipationRequestDto {
    private Long id;
    private Long event;
    private String created;
    private Long requester;
    private State status;
}
