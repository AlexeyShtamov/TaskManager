package ru.develop.manager.application.services.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.develop.manager.application.services.AuthService;
import ru.develop.manager.application.services.PersonService;
import ru.develop.manager.domain.Person;
import ru.develop.manager.extern.DTOs.JwtRequest;
import ru.develop.manager.extern.DTOs.JwtResponse;
import ru.develop.manager.extern.exceptions.WrongDataException;
import ru.develop.manager.extern.utils.JwtTokenUtils;


/**
 * Документацию смотреть в интерфейсах
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final PersonService personService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;


    @Override
    public JwtResponse createAuthToken(JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Неправильный логин или пароль");
        }
        UserDetails userDetails = personService.loadUserByUsername(authRequest.getEmail());
        String token = jwtTokenUtils.generateToken(userDetails);

        return new JwtResponse(token);
    }

    @Override
    public Person createPerson(Person person) throws WrongDataException {
        if (!person.getPassword().equals(person.getRepeatPassword())) {
            throw new WrongDataException("Ваши пароли не совпадают");
        }

        if (personService.findByEmail(person.getEmail()).isPresent()) {
            throw new WrongDataException("Пользователь с указанным именем уже существует");
        }
        return personService.createPerson(person);
    }

    @Override
    public Person createAdmin(Person person) throws WrongDataException {
        if (!person.getPassword().equals(person.getRepeatPassword())) {
            throw new WrongDataException("Ваши пароли не совпадают");
        }

        if (personService.findByEmail(person.getEmail()).isPresent()) {
            throw new WrongDataException("Пользователь с указанным именем уже существует");
        }
        return personService.createPerson(person);
    }
}
