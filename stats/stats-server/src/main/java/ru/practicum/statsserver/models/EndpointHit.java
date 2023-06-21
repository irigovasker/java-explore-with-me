package ru.practicum.statsserver.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hit")
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "app is empty")
    private String app;
    @NotBlank(message = "uri is empty")
    private String uri;
    @NotNull(message = "ip is null")
    @Pattern(regexp = "^(\\d{1,3}\\.){3}\\d{1,3}$", message = "wrong ip")
    private String ip;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "hit_at")
    private LocalDateTime timestamp;
}
