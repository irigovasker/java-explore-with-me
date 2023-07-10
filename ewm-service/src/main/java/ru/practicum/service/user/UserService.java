package ru.practicum.service.user;

import org.springframework.data.domain.PageRequest;
import ru.practicum.dto.user.NewUserDto;
import ru.practicum.model.user.User;

import java.util.List;

public interface UserService {
    void deleteUserById(long id);

    User getUserByIdOrElseThrow(long id);

    NewUserDto createUser(NewUserDto newUserDto);

    List<NewUserDto> getUsers(List<Long> ids, PageRequest pageRequest);
}
