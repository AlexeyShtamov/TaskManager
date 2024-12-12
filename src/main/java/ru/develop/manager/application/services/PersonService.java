package ru.develop.manager.application.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.develop.manager.domain.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService extends UserDetailsService {

    /**
     * Создание пользователя (загрузка пользователя в бд)
     * @param person Новый пользовать
     * @return Созданный пользователь
     */
    Person createPerson(Person person);

    /**
     * Нахождение пользователя по электронной почте (логин)
     * @param email Электронная почта
     * @return Найденный пользователь (Optional)
     */
    Optional<Person> findByEmail(String email);

    /**
     * Нахождение пользователя по итендификатору
     * @param id Итендификатор пользователя
     * @return Найденный пользователь (Optional)
     */
    Optional<Person> findById(Long id);

    /**
     * Получение id задач, которые у пользователя на исполнеии
     * @param id Итендификатор пользователя
     * @return список ID задач
     */
    List<Long> getTasksId(Long id);
}
