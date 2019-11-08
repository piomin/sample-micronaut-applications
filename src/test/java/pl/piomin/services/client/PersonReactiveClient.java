package pl.piomin.services.client;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Flowable;
import pl.piomin.services.model.Person;

@Client("/persons/reactive")
public interface PersonReactiveClient {

    @Get(produces = MediaType.APPLICATION_JSON_STREAM)
    Flowable<Person> findAll();

}
