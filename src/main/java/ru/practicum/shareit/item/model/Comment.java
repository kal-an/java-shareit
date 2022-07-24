package ru.practicum.shareit.item.model;

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
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments", schema = "public")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Text should not be empty")
    @Size(max = 4000, message = "Text should less 4000 characters")
    @Column(name = "text", nullable = false, length = 4000)
    private String text;

    @NotNull(message = "Item ID should not be empty")
    @Column(name = "item_id")
    private Long itemId;

    @NotNull(message = "Author ID should not be empty")
    @Column(name = "author_id")
    private Long authorId;

    @NotNull(message = "Author name should not be empty")
    @Transient
    private String authorName;

    @NotNull(message = "Created date should not be null")
    private LocalDateTime created = LocalDateTime.now();

    public Comment(String text, Long itemId, Long authorId, String authorName) {
        this.text = text;
        this.itemId = itemId;
        this.authorId = authorId;
        this.authorName = authorName;
    }
}