package ru.develop.manager.application.services;

import ru.develop.manager.domain.Person;
import ru.develop.manager.extern.DTOs.JwtRequest;
import ru.develop.manager.extern.DTOs.JwtResponse;
import ru.develop.manager.extern.exceptions.WrongDataException;

public interface AuthService {

    /**
     * Создание токена JWT из данных пользователя (логин и пароль)
     * @param authRequest DTO с логином и паролем
     * @return Ответ в виде токена
     */
    JwtResponse createAuthToken(JwtRequest authRequest);

    /**
     * Создание/регистрация пользователя
     * @param person Сущность нового пользователя
     * @return Созданный пользователь
     * @throws WrongDataException выбрасывается, если пароли не совпадают
     */
    Person createPerson(Person person) throws WrongDataException;

    /**
     * Создание админа ничем не отличается от создание пользователя кроме:
     * Создать админа может только другой админ
     * Созданному админу присваивается роль ARMIN
     */
    Person createAdmin(Person person) throws WrongDataException;


}
