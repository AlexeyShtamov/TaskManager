package ru.develop.manager.extern.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
public class AppError {

    private int status;
    private String message;
    private Date date;

    public AppError(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
        this.date = new Date();
    }
}
