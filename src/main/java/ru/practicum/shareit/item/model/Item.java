package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.requests.model.ItemRequest;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "items", schema = "public")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name should not be empty")
    @Column(name = "name", nullable = false)
    private String name;

    @NotEmpty(message = "Description should not be empty")
    @Size(max = 200, message = "Description should less 200 characters")
    @Column(name = "description", nullable = false, length = 4000)
    private String description;

    @NotEmpty(message = "Status should not be empty")
    private Boolean available;

    @NotNull(message = "User should not be null")
    @Column(name = "owner_id", nullable = false)
    private Long owner;

    @Column(name = "request_id")
    private ItemRequest request;
}
