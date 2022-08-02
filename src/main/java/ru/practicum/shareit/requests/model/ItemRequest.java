package ru.practicum.shareit.requests.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "item_requests", schema = "public")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Description should not be empty")
    @Size(max = 4000, message = "Description should less 4000 characters")
    @Column(name = "description")
    private String description;

    @NotNull(message = "User should not be null")
    @OneToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @NotNull(message = "Created date should not be null")
    @Column(name = "created", nullable = false)
    private LocalDateTime created = LocalDateTime.now();

    public ItemRequest(String description, User requester) {
        this.description = description;
        this.requester = requester;
    }
}
