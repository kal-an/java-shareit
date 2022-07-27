package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name should not be empty")
    @Size(max = 255, message = "Name should less 255 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email should not be empty")
    @Column(name = "email", nullable = false, length = 512)
    @Size(max = 512, message = "Email should less 512 characters")
    private String email;
}
