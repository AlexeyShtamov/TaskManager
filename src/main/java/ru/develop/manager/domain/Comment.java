package ru.develop.manager.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * Сущность комментрия к задаче
 * @see Task
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Serializable {

    /**
     * Итендификатор комментария
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Текст комментария
     */
    @Size(min = 3, message = "comment couldn't be less that 3 symbols")
    private String comment;

    /**
     * Задача, к которой принадлежит комментарий
     */
    @ManyToOne
    private Task task;

    public Comment(String comment) {
        this.comment = comment;
    }
}
