package org.chirkov.firstRestApp.controllers;

import org.chirkov.firstRestApp.models.Person;
import org.chirkov.firstRestApp.services.PeopleService;
import org.chirkov.firstRestApp.util.PersonErrorResponse;
import org.chirkov.firstRestApp.util.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping()
    public List<Person>  getPeople() {
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

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND) ; //NOT_FOUND = 404 status
    }
}
