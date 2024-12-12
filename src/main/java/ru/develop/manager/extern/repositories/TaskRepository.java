package ru.develop.manager.extern.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.develop.manager.domain.Person;
import ru.develop.manager.domain.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByAuthor(Person author, Pageable pageable);

    Page<Task> findByExecutorsId(Long executorId, Pageable pageable);


}
