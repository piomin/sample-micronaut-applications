package pl.piomin.services.controller;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.version.annotation.Version;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.validation.Validated;
import pl.piomin.services.model.Person;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller("/persons")
@Secured(SecurityRule.IS_ANONYMOUS)
@Validated
public class PersonController {

    List<Person> persons = new ArrayList<>();

    @Post
    public Person add(@Body @Valid Person person) {
        person.setId(persons.size() + 1);
        persons.add(person);
        return person;
    }

    @Get("/{id:4}")
    public Optional<Person> findById(@NotNull Integer id) {
        return persons.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    @Version("1")
    @Get("{?max,offset}")
    public List<Person> findAll(@Nullable Integer max,
                                @Nullable Integer offset) {
        return persons.stream()
                .skip(offset == null ? 0 : offset)
                .limit(max == null ? 10000 : max)
                .collect(Collectors.toList());
    }

    @Version("2")
    @Get("{?max,offset}")
    public List<Person> findAllV2(@NotNull Integer max, @NotNull Integer offset) {
        return persons.stream()
                .skip(offset == null ? 0 : offset)
                .limit(max == null ? 10000 : max)
                .collect(Collectors.toList());
    }

}
