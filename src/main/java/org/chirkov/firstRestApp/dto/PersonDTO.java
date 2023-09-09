package org.chirkov.firstRestApp.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PersonDTO { // not connected from dataBase, forever

    @NotEmpty(message = "Name should not be empty!")
    @Size(min = 3, max = 33, message = "First name should 3-33 characters long!")
    private String name;

    @Range(min = 1, max = 333, message = "Age should be between 1 - 333 and ")
    private int age;

    @NotEmpty(message = "Email should not be empty!")
    @Email(message = "Email should be valid")
    private String email;
}
