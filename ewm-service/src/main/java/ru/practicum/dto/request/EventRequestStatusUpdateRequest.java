package ru.practicum.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private String status;
}
