package ru.develop.manager.extern.assemblers;

import org.springframework.stereotype.Component;
import ru.develop.manager.domain.Person;
import ru.develop.manager.domain.enums.Role;
import ru.develop.manager.extern.DTOs.personDTO.InfoPersonDto;
import ru.develop.manager.extern.DTOs.personDTO.RegisterPersonDTO;

@Component
public class PersonAssembler {


    public Person fromRegisterDtoToPerson(RegisterPersonDTO dto){
        Person person = new Person();
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setPassword(dto.getPassword());
        person.setRepeatPassword(dto.getRepeatPassword());
        person.setEmail(dto.getEmail());
        person.setRole(Role.ROLE_USER);
        return person;
    }

    public InfoPersonDto fromPersonToDto(Person person){
        InfoPersonDto infoPersonDto = new InfoPersonDto();
        infoPersonDto.setEmail(person.getEmail());
        infoPersonDto.setFirstName(person.getFirstName());
        infoPersonDto.setLastName(person.getLastName());
        return infoPersonDto;
    }
}
