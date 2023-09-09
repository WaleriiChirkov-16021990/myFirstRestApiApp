package org.chirkov.firstRestApp.repositories;

import org.chirkov.firstRestApp.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    //    поиск только по полю класса Person
//    List<Person> findPeopleByEmail(String email);

    //   поиск по имени
    List<Person> findPeopleByName(String name);

    //    поиск по имени с сортировкой по возрастанию возраста
    List<Person> findPeopleByNameOrderByAge(String name);

    //    поиск по первым символам строки имени
    List<Person> findPeopleByNameStartingWith(String startingWith);

    //    поиск по имени или почте
    List<Person> findPeopleByNameOrEmail(String name, String email);


    Optional<Person> findByEmail(String email);
}
