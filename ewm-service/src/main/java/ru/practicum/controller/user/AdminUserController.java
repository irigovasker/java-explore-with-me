package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.NewUserDto;
import ru.practicum.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewUserDto createUser(@RequestBody @Valid NewUserDto newUserDto) {
        return userService.createUser(newUserDto);
    }

    @GetMapping
    public List<NewUserDto> getUsers(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        return userService.getUsers(ids, PageRequest.of(from, size));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable("userId") Long userId) {
        userService.deleteUserById(userId);
    }
}
