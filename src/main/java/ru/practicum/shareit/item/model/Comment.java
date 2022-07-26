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
@Builder
@Table(name = "comments", schema = "public")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Text should not be empty")
    @Size(max = 4000, message = "Text should less 4000 characters")
    @Column(name = "text", nullable = false, length = 4000)
    private String text;

    @NotNull(message = "Item should not be empty")
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @NotNull(message = "Author should not be empty")
    @OneToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @NotNull(message = "Created date should not be null")
    private LocalDateTime created = LocalDateTime.now();

    public Comment(String text, Item item, User author) {
        this.text = text;
        this.item = item;
        this.author = author;
    }
}