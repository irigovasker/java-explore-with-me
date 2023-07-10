package ru.practicum.mapper.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.model.request.Request;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mapping(target = "created", source = "created", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "requester", source = "request.requester.id")
    @Mapping(target = "event", source = "request.eventId")
    ParticipationRequestDto toRequestDto(Request request);
}
