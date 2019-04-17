package pl.piomin.services.controller;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import pl.piomin.services.model.Person;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller("/persons")
@Validated
public class PersonController {

    List<Person> persons = new ArrayList<>();

    @Post
    public Person add(@Body Person person) {
        person.setId(persons.size() + 1);
        persons.add(person);
        return person;
    }

    @Get("/{id:4}")
    public Optional<Person> findById(Integer id) {
        return persons.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    @Get("{?max,offset}")
    public List<Person> findAll(@Nullable Integer max, @Nullable Integer offset) {
        return persons.stream()
                .skip(offset)
                .limit(max)
                .collect(Collectors.toList());
    }

}
