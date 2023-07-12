package ru.practicum.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.CommentDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.event.CommentMapper;
import ru.practicum.model.event.Comment;
import ru.practicum.repository.event.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public CommentDto createComment(long userId, long eventId, CommentDto commentDto) {
        commentDto.setAuthorId(userId);
        commentDto.setEventId(eventId);
        commentDto.setCreatedAt(LocalDateTime.now());
        return commentMapper.toCommentDto(commentRepository.save(commentMapper.toComment(commentDto)));
    }

    @Override
    public List<CommentDto> getComments(long eventId) {
        return getComments(eventId, PageRequest.of(0, 20));
    }

    @Override
    public List<CommentDto> getComments(long eventId, PageRequest pageRequest) {
        return commentRepository.findCommentsByEventIdOrderByCreatedAtDesc(eventId, pageRequest)
                .stream().map(commentMapper::toCommentDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCommentByAdmin(long eventId, long commentId) {
        getCommentOrThrow(commentId);
        commentRepository.deleteById(commentId);
    }

    private Comment getCommentOrThrow(long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id =" + commentId + " not found"));
    }
}
