package ru.practicum.model.event;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "text")
    private String text;
    @Column(name = "author_id")
    private long authorId;
    @Column(name = "event_id")
    private long eventId;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
