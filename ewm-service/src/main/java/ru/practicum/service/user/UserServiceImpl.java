package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.user.NewUserDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.user.UserMapper;
import ru.practicum.model.user.User;
import ru.practicum.repository.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;

    @Transactional
    public NewUserDto createUser(NewUserDto newUserDto) {
        return userMapper.toUserDto(repository.save(userMapper.toUser(newUserDto)));
    }

    public List<NewUserDto> getUsers(List<Long> userIdList, PageRequest pageRequest) {
        List<User> result = userIdList == null ?
                repository.findAll(pageRequest).toList() :
                repository.findByIdIn(userIdList, pageRequest);
        return result.stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    @Transactional
    public void deleteUserById(long userId) {
        repository.deleteById(userId);
    }

    public User getUserByIdOrElseThrow(long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User with id=" + id + " not found"));
    }
}
