package ru.practicum.mapper.event;

import org.mapstruct.Mapper;
import ru.practicum.dto.event.CommentDto;
import ru.practicum.model.event.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDto toCommentDto(Comment comment);
    Comment toComment(CommentDto commentDto);
}
