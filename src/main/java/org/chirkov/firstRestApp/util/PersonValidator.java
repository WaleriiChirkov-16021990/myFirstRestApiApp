package org.chirkov.firstRestApp.util;

import lombok.Getter;
import lombok.Setter;
import org.chirkov.firstRestApp.models.Person;
import org.chirkov.firstRestApp.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Getter
@Component
@Setter
public class PersonValidator implements Validator {

//    private final PersonDAO personDAO;
    private final PeopleService peopleService;
    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        //есть ли человек с таким email в базу данных
        if (peopleService.findByEmail(person.getEmail()).isPresent()) {
            errors.rejectValue("email","", "This email is already taken");
        }

    }

}
