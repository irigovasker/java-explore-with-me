package ru.practicum.mapper.event;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.dto.event.FullEventDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.ShortEventDto;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.dto.user.ShortUserDto;
import ru.practicum.mapper.category.CategoryMapper;
import ru.practicum.mapper.location.LocationMapper;
import ru.practicum.mapper.user.UserMapper;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.State;
import ru.practicum.model.location.Location;
import ru.practicum.model.user.User;

import java.time.LocalDateTime;


@Mapper(componentModel = "spring", uses = {CategoryMapper.class, UserMapper.class, LocationMapper.class})
public interface EventMapper {

    default FullEventDto toEventFullDto(Event event) {
        return FullEventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(toShortUserDto(event.getInitiator()))
                .location(toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn((event.getPublishedOn() == null) ? null : event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    default Event toEvent(NewEventDto newEventDto, Category category, User user, LocalDateTime dateTime) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .confirmedRequests(0L)
                .description(newEventDto.getDescription())
                .createdOn(dateTime)
                .eventDate(newEventDto.getEventDate())
                .initiator(user)
                .location(toLocation(newEventDto.getLocation()))
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .state(State.PENDING)
                .title(newEventDto.getTitle())
                .views(0L)
                .build();
    }

    default ShortEventDto toEventShortDto(Event event) {
        return ShortEventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(toShortUserDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    default NewCategoryDto toCategoryDto(Category category) {
        return Mappers.getMapper(CategoryMapper.class).toCategoryDto(category);
    }

    default ShortUserDto toShortUserDto(User user) {
        return Mappers.getMapper(UserMapper.class).toShortUserDto(user);
    }

    default LocationDto toLocationDto(Location location) {
        return Mappers.getMapper(LocationMapper.class).toLocationDto(location);
    }

    default Location toLocation(LocationDto locationDto) {
        return Mappers.getMapper(LocationMapper.class).toLocation(locationDto);
    }
}
