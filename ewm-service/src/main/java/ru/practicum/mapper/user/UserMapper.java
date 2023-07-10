package ru.practicum.mapper.user;

import org.mapstruct.Mapper;
import ru.practicum.dto.user.NewUserDto;
import ru.practicum.dto.user.ShortUserDto;
import ru.practicum.model.user.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    NewUserDto toUserDto(User user);

    User toUser(NewUserDto newUserDto);

    ShortUserDto toShortUserDto(User user);
}
