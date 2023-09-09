package org.chirkov.firstRestApp.controllers;

import com.sun.istack.NotNull;
import org.chirkov.firstRestApp.models.Person;
import org.chirkov.firstRestApp.services.PeopleService;
import org.chirkov.firstRestApp.util.PersonErrorResponse;
import org.chirkov.firstRestApp.util.PersonNotCreatedException;
import org.chirkov.firstRestApp.util.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping()
    public List<Person> getPeople() {
        return peopleService.findAll(); //Jackson convert to JSON List<Person>
    }

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable("id") int id) {
        return peopleService.findOne(id);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handeleException(PersonNotFoundException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                "Person with this id wasn't found", System.currentTimeMillis()
        );
        // (В HTTP ответе тело ответа (response)) In the HTTP response,
        // the response body (response) and status in header.

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); //NOT_FOUND = 404 status
    }


    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid Person person, @NotNull BindingResult bindingResult) {
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
        peopleService.save(person);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handeleException(PersonNotCreatedException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        // (В HTTP ответе тело ответа (response)) In the HTTP response,
        // the response body (response) and status in header.

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); //BAD_REQUEST = 400 status
    }
}
