package ru.develop.manager.application.services.impls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.develop.manager.application.services.PersonService;
import ru.develop.manager.domain.Person;
import ru.develop.manager.domain.Task;
import ru.develop.manager.extern.repositories.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Документацию смотреть в интерфейсах
 */
@Service
@Slf4j
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public PersonServiceImpl(PersonRepository personRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Person createPerson(Person person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        Person createdPerson = personRepository.save(person);
        log.info("PersonService: Person with name {} {} is created", person.getFirstName(), person.getLastName());
        return createdPerson;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person person = personRepository
                .findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No user with email " + email));
        log.info("PersonService: UserDetails with email {} is found", email);
        return person;
    }

    @Override
    public Optional<Person> findByEmail(String email) {
        Optional<Person> optionalPerson =  personRepository.findByEmail(email);
        log.info("PersonService: Optional<Person> is found with email {}", email);
        return optionalPerson;
    }

    @Override
    public Optional<Person> findById(Long id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        log.info("PersonService: Optional<Person> is found by id {}", id);
        return optionalPerson;
    }

    @Override
    public List<Long> getTasksId(Long id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        Person person = optionalPerson.orElseThrow(() -> new NullPointerException("No person with id " + id));
        return person.getProgressTasks().stream().map(Task::getId).collect(Collectors.toList());
    }
}
