package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "items", schema = "public")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name should not be empty")
    @Size(max = 255, message = "Name should less 255 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotEmpty(message = "Description should not be empty")
    @Size(max = 4000, message = "Description should less 4000 characters")
    @Column(name = "description", nullable = false, length = 4000)
    private String description;

    @NotNull(message = "Status should not be empty")
    private Boolean available;

    @NotNull(message = "User should not be null")
    @OneToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request;
}
