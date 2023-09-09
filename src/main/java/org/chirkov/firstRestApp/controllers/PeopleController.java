package org.chirkov.firstRestApp.controllers;

import org.chirkov.firstRestApp.dto.PersonDTO;
import org.chirkov.firstRestApp.models.Person;
import org.chirkov.firstRestApp.services.PeopleService;
import org.chirkov.firstRestApp.util.PersonErrorResponse;
import org.chirkov.firstRestApp.util.PersonNotCreatedException;
import org.chirkov.firstRestApp.util.PersonNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;
    private final ModelMapper modelMapper;

    @Autowired
    public PeopleController(PeopleService peopleService, ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<PersonDTO> getPeople() {
        return peopleService.findAll().stream().map(this::convertToPersonDTO).collect(Collectors.toList()); //Jackson convert to JSON List<Person>
    }

    @GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable("id") int id) {
        return convertToPersonDTO(peopleService.findOne(id));
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handlerException(PersonNotFoundException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                "Person with this id wasn't found", System.currentTimeMillis()
        );
        // (В HTTP ответе тело ответа (response)) In the HTTP response,
        // the response body (response) and status in header.

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); //NOT_FOUND = 404 status
    }


    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            AtomicReference<StringBuilder> errorMessage = new AtomicReference<>(new StringBuilder());
            List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
            // собираем сообщение об ошибке для пользователя
            // collect error message for user
            for (FieldError error :
                    fieldErrorList) {
                errorMessage.get().append(error.getField()).append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new PersonNotCreatedException(errorMessage.toString());
        }
        peopleService.save(convertToPerson(person));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private Person convertToPerson(PersonDTO personDTO) {
//        ModelMapper modelMapper = new ModelMapper(); //full mapping DTO on my model {manual object creation}
        return modelMapper.map(personDTO, Person.class);
    }

//
//    private Person convertToPerson(PersonDTO personDTO) {
//        return new Person(
//                personDTO.getName(),
//                personDTO.getAge(),
//                personDTO.getEmail()
//        );
//    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handlerException(PersonNotCreatedException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        // (В HTTP ответе тело ответа (response)) In the HTTP response,
        // the response body (response) and status in header.

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); //BAD_REQUEST = 400 status
    }

    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }
}
