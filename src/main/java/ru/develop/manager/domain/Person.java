package ru.develop.manager.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import ru.develop.manager.domain.enums.Role;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Сущность пользователя
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Person implements UserDetails, Serializable {

    /**
     * Итендификатор пользователя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя
     */
    @NotBlank(message = "FirstName couldn't be blank")
    @NotNull(message = "FirstName couldn't be null")
    private String firstName;

    /**
     * Фамилия пользователя
     */
    @NotBlank(message = "LastName couldn't be blank")
    @NotNull(message = "LastName couldn't be null")
    private String lastName;

    /**
     * Электронная почта пользователя
     */
    @Email(message = "Email format is invalid")
    @NotBlank(message = "Email couldn't be blank")
    @NotNull(message = "Email couldn't be null")
    private String email;

    /**
     * Пароль от аккаунта пользователя
     */
    @Size(min = 8, message = "Your password should include 8 or more symbols")
    @NotBlank(message = "Password couldn't be blank")
    @NotNull(message = "Password couldn't be null")
    private String password;

    @Transient
    private String repeatPassword;

    /**
     * Роль пользователя
     * @see Role
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Список задач, выполняемых пользователем
     * @see Task
     */
    @ManyToMany(mappedBy = "executors")
    private List<Task> progressTasks  = new ArrayList<>();

    /**
     * Список задач, созданных пользователем
     * @see Task
     */
    @OneToMany(mappedBy = "author")
    private List<Task> createdTasks = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(String.valueOf(this.role));
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
