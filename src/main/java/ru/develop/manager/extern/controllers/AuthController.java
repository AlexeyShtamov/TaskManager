package ru.develop.manager.extern.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.develop.manager.application.services.AuthService;
import ru.develop.manager.domain.Person;
import ru.develop.manager.extern.DTOs.personDTO.InfoPersonDto;
import ru.develop.manager.extern.DTOs.JwtRequest;
import ru.develop.manager.extern.DTOs.JwtResponse;
import ru.develop.manager.extern.DTOs.personDTO.RegisterPersonDTO;
import ru.develop.manager.extern.assemblers.PersonAssembler;
import ru.develop.manager.extern.exceptions.WrongDataException;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "auth_methods")
public class AuthController {

    private final AuthService authService;
    private final PersonAssembler personAssembler;

    @Operation(summary = "Для создания токена",
            description = "Необходимо в качестве тела запроса передать JwtRequest, который включает " +
                    "в себя почту и пароль")
    @PostMapping("/auth")
    public ResponseEntity<JwtResponse> createAuthToken(@RequestBody JwtRequest authRequest) {
        JwtResponse jwtResponse = authService.createAuthToken(authRequest);
        log.info("JwtResponse is gotten in AuthService");
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    @Operation(summary = "Регистрация нового пользователя",
            description = "Необходимо в качестве тела запроса передать RegisterPersonDTO")
    @PostMapping("/registration")
    public ResponseEntity<InfoPersonDto> createNewPerson(@RequestBody RegisterPersonDTO registerPersonDTO) throws WrongDataException {
        Person person = personAssembler.fromRegisterDtoToPerson(registerPersonDTO);
        Person createdPerson = authService.createPerson(person);
        return new ResponseEntity<>(personAssembler.fromPersonToDto(createdPerson), HttpStatus.OK);
    }


    @Operation(summary = "Создание нового админа",
            description = "Необходимо в качестве тела запроса передать RegisterPersonDTO")
    @PostMapping("/admin/registration")
    public ResponseEntity<InfoPersonDto> createNewAdmin(@RequestBody RegisterPersonDTO registerPersonDTO) throws WrongDataException {
        Person person = personAssembler.fromRegisterDtoToPerson(registerPersonDTO);
        Person createdPerson = authService.createAdmin(person);
        return new ResponseEntity<>(personAssembler.fromPersonToDto(createdPerson), HttpStatus.OK);
    }

}
