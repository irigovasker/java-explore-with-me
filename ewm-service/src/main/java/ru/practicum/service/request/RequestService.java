package ru.practicum.service.request;

import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getRequestsByCreatorId(long userId);

    ParticipationRequestDto createRequest(Long userId, Long requestDto);

    ParticipationRequestDto cancelRequestByCreator(long userId, long requestId);
}
