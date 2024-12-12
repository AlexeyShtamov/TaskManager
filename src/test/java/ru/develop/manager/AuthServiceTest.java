package ru.develop.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import ru.develop.manager.application.services.PersonService;
import ru.develop.manager.application.services.impls.AuthServiceImpl;
import ru.develop.manager.domain.Person;
import ru.develop.manager.extern.DTOs.JwtRequest;
import ru.develop.manager.extern.DTOs.JwtResponse;
import ru.develop.manager.extern.exceptions.WrongDataException;
import ru.develop.manager.extern.utils.JwtTokenUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private PersonService personService;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAuthToken_shouldReturnJwtResponse() {
        // Arrange
        JwtRequest authRequest = new JwtRequest();
        UserDetails userDetails = mock(UserDetails.class);
        String token = "generatedToken";

        when(personService.loadUserByUsername(authRequest.getEmail())).thenReturn(userDetails);
        when(jwtTokenUtils.generateToken(userDetails)).thenReturn(token);

        // Act
        JwtResponse jwtResponse = authService.createAuthToken(authRequest);

        // Assert
        assertNotNull(jwtResponse);
        assertEquals(token, jwtResponse.getToken());
        verify(authenticationManager, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        verify(personService, times(1)).loadUserByUsername(authRequest.getEmail());
        verify(jwtTokenUtils, times(1)).generateToken(userDetails);
    }

    @Test
    void createAuthToken_shouldThrowBadCredentialsException() {
        // Arrange
        JwtRequest authRequest = new JwtRequest();

        doThrow(BadCredentialsException.class).when(authenticationManager)
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.createAuthToken(authRequest));
        verify(authenticationManager, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        verifyNoInteractions(personService);
        verifyNoInteractions(jwtTokenUtils);
    }

    @Test
    void createPerson_shouldThrowWrongDataException_whenPasswordsDoNotMatch() {
        // Arrange
        Person person = new Person();
        person.setPassword("password1");
        person.setRepeatPassword("password2");

        // Act & Assert
        WrongDataException exception = assertThrows(WrongDataException.class, () -> authService.createPerson(person));
        assertEquals("Your passwords don't match", exception.getMessage());
        verifyNoInteractions(personService);
    }

    @Test
    void createPerson_shouldThrowWrongDataException_whenEmailAlreadyExists() {
        // Arrange
        Person person = new Person();
        person.setPassword("password");
        person.setRepeatPassword("password");
        person.setEmail("test@example.com");

        when(personService.findByEmail(person.getEmail())).thenReturn(Optional.of(person));

        // Act & Assert
        WrongDataException exception = assertThrows(WrongDataException.class, () -> authService.createPerson(person));
        assertEquals("A user with the specified name already exists", exception.getMessage());
        verify(personService, times(1)).findByEmail(person.getEmail());
    }

    @Test
    void createPerson_shouldCreatePersonSuccessfully() throws WrongDataException {
        // Arrange
        Person person = new Person();
        person.setPassword("password");
        person.setRepeatPassword("password");
        person.setEmail("test@example.com");

        when(personService.findByEmail(person.getEmail())).thenReturn(Optional.empty());
        when(personService.createPerson(person)).thenReturn(person);

        // Act
        Person createdPerson = authService.createPerson(person);

        // Assert
        assertNotNull(createdPerson);
        verify(personService, times(1)).findByEmail(person.getEmail());
        verify(personService, times(1)).createPerson(person);
    }

    @Test
    void createAdmin_shouldThrowWrongDataException_whenPasswordsDoNotMatch() {
        // Arrange
        Person person = new Person();
        person.setPassword("password1");
        person.setRepeatPassword("password2");

        // Act & Assert
        WrongDataException exception = assertThrows(WrongDataException.class, () -> authService.createAdmin(person));
        assertEquals("Your passwords don't match", exception.getMessage());
        verifyNoInteractions(personService);
    }

    @Test
    void createAdmin_shouldThrowWrongDataException_whenEmailAlreadyExists() {
        // Arrange
        Person person = new Person();
        person.setPassword("password");
        person.setRepeatPassword("password");
        person.setEmail("admin@example.com");

        when(personService.findByEmail(person.getEmail())).thenReturn(Optional.of(person));

        // Act & Assert
        WrongDataException exception = assertThrows(WrongDataException.class, () -> authService.createAdmin(person));
        assertEquals("A user with the specified name already exists", exception.getMessage());
        verify(personService, times(1)).findByEmail(person.getEmail());
    }

    @Test
    void createAdmin_shouldCreateAdminSuccessfully() throws WrongDataException {
        // Arrange
        Person person = new Person();
        person.setPassword("password");
        person.setRepeatPassword("password");
        person.setEmail("admin@example.com");

        when(personService.findByEmail(person.getEmail())).thenReturn(Optional.empty());
        when(personService.createPerson(person)).thenReturn(person);

        // Act
        Person createdAdmin = authService.createAdmin(person);

        // Assert
        assertNotNull(createdAdmin);
        verify(personService, times(1)).findByEmail(person.getEmail());
        verify(personService, times(1)).createPerson(person);
    }
}
