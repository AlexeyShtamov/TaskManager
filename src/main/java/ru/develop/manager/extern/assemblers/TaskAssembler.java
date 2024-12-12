package ru.develop.manager.extern.assemblers;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import ru.develop.manager.domain.Comment;
import ru.develop.manager.domain.Person;
import ru.develop.manager.domain.Task;
import ru.develop.manager.domain.enums.Priority;
import ru.develop.manager.domain.enums.Status;
import ru.develop.manager.extern.DTOs.personDTO.InfoPersonDto;
import ru.develop.manager.extern.DTOs.taskDTO.CreateTaskDTO;
import ru.develop.manager.extern.DTOs.taskDTO.InfoTaskDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskAssembler {
    private final PersonAssembler personAssembler;

    public Task fromDTOToTask(CreateTaskDTO createTaskDTO){
        Task task = new Task();
        task.setTitle(createTaskDTO.getTitle());
        task.setDescription(createTaskDTO.getDescription());

        if (createTaskDTO.getStatus() != null) task.setStatus(Status.valueOf(createTaskDTO.getStatus().toUpperCase()));
        if (createTaskDTO.getPriority() != null) task.setPriority(Priority.valueOf(createTaskDTO.getPriority().toUpperCase()));
        return task;
    }

    public InfoTaskDTO fromTaskToDTO(Task task){
        InfoTaskDTO infoTaskDTO = new InfoTaskDTO();
        infoTaskDTO.setId(task.getId());
        infoTaskDTO.setTitle(task.getTitle());
        infoTaskDTO.setDescription(task.getDescription());
        infoTaskDTO.setStatus(String.valueOf(task.getStatus()));
        infoTaskDTO.setPriority(String.valueOf(task.getPriority()));
        infoTaskDTO.setComments(task.getComments().stream().map(Comment::getComment).collect(Collectors.toList()));

        InfoPersonDto author = personAssembler.fromPersonToDto(task.getAuthor());
        List<InfoPersonDto> executors = new ArrayList<>();
        for (Person person : task.getExecutors()){
            executors.add(personAssembler.fromPersonToDto(person));
        }

        infoTaskDTO.setAuthor(author);
        infoTaskDTO.setExecutors(executors);

        return infoTaskDTO;

    }
}
