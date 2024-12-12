package ru.develop.manager.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.develop.manager.domain.enums.Priority;
import ru.develop.manager.domain.enums.Status;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность задачи
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task implements Serializable {

    /**
     * Итендификатор задачи
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название задачи
     */
    @NotBlank
    @NotNull
    private String title;

    /**
     * Описание задачи
     */
    @Size(min = 5, message = "Minimum size of description is 5")
    @NotBlank
    @NotNull
    private String description;

    /**
     * Статус задачи
     * @see Status
     */
    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * Приоритет задачи
     * @see Priority
     */
    @Enumerated(EnumType.STRING)
    private Priority priority;

    /**
     * Список комментраии к задачи
     * @see Comment
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    /**
     * Автор задачи
     * @see Person
     */
    @ManyToOne
    private Person author;

    /**
     * Список Исполнителей задачи
     * @see Person
     */
    @ManyToMany
    private List<Person> executors = new ArrayList<>();

}
