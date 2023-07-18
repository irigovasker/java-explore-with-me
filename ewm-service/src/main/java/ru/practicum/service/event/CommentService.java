package ru.practicum.service.event;

import org.springframework.data.domain.PageRequest;
import ru.practicum.dto.event.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(long userId, long eventId, CommentDto commentDto);

    List<CommentDto> getComments(long eventId);

    List<CommentDto> getComments(long eventId, PageRequest pageRequest);

    void deleteCommentByAdmin(long eventId, long commentId);
}
