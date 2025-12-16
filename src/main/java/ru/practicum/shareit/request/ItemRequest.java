package ru.practicum.shareit.request;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests")
public class ItemRequest {
    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Column(nullable = false)
    private String description;
    @NonNull
    @Column(name = "requestor_id", nullable = false)
    private Long requestor;
    @NonNull
    @Column(name = "created_time", nullable = false)
    private LocalDateTime created;
}
