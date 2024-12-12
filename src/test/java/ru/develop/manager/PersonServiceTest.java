package ru.develop.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.develop.manager.application.services.impls.PersonServiceImpl;
import ru.develop.manager.domain.Person;
import ru.develop.manager.domain.Task;
import ru.develop.manager.extern.repositories.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PersonServiceImpl personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPerson_shouldEncodePasswordAndSavePerson() {
        // Arrange
        Person person = new Person();
        person.setPassword("rawPassword");
        Person savedPerson = new Person();
        savedPerson.setId(1L);

        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(personRepository.save(person)).thenReturn(savedPerson);

        // Act
        Person createdPerson = personService.createPerson(person);

        // Assert
        assertNotNull(createdPerson);
        assertEquals(savedPerson.getId(), createdPerson.getId());
        verify(passwordEncoder, times(1)).encode("rawPassword");
        verify(personRepository, times(1)).save(person);
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails() {
        // Arrange
        String email = "test@example.com";
        Person person = new Person();
        person.setEmail(email);

        when(personRepository.findByEmail(email)).thenReturn(Optional.of(person));

        // Act
        UserDetails userDetails = personService.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        verify(personRepository, times(1)).findByEmail(email);
    }

    @Test
    void loadUserByUsername_shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        String email = "notfound@example.com";

        when(personRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> personService.loadUserByUsername(email));
        verify(personRepository, times(1)).findByEmail(email);
    }

    @Test
    void findByEmail_shouldReturnOptionalPerson() {
        // Arrange
        String email = "test@example.com";
        Person person = new Person();
        person.setEmail(email);

        when(personRepository.findByEmail(email)).thenReturn(Optional.of(person));

        // Act
        Optional<Person> result = personService.findByEmail(email);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        verify(personRepository, times(1)).findByEmail(email);
    }

    @Test
    void findById_shouldReturnOptionalPerson() {
        // Arrange
        Long id = 1L;
        Person person = new Person();
        person.setId(id);

        when(personRepository.findById(id)).thenReturn(Optional.of(person));

        // Act
        Optional<Person> result = personService.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(personRepository, times(1)).findById(id);
    }

    @Test
    void findById_shouldReturnEmptyOptionalIfPersonNotFound() {
        // Arrange
        Long id = 1L;

        when(personRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Person> result = personService.findById(id);

        // Assert
        assertFalse(result.isPresent());
        verify(personRepository, times(1)).findById(id);
    }

    @Test
    void getTasksId_shouldReturnTaskIdsForPerson() {
        // Arrange
        Long personId = 1L;
        Task task1 = new Task();
        task1.setId(101L);
        Task task2 = new Task();
        task2.setId(102L);
        Person person = new Person();
        person.setProgressTasks(Stream.of(task1, task2).collect(Collectors.toList()));

        when(personRepository.findById(personId)).thenReturn(Optional.of(person));

        // Act
        List<Long> taskIds = personService.getTasksId(personId);

        // Assert
        assertNotNull(taskIds);
        assertEquals(2, taskIds.size());
        assertTrue(taskIds.contains(101L));
        assertTrue(taskIds.contains(102L));
        verify(personRepository, times(1)).findById(personId);
    }

    @Test
    void getTasksId_shouldThrowExceptionIfPersonNotFound() {
        // Arrange
        Long personId = 1L;

        when(personRepository.findById(personId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NullPointerException.class, () -> personService.getTasksId(personId));
        verify(personRepository, times(1)).findById(personId);
    }
}
