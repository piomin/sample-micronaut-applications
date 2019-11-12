package pl.piomin.services.client;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import pl.piomin.services.model.Person;

@Client("/persons/reactive")
public interface PersonReactiveClient {

    @Post
    Single<Person> add(@Body Person person);

    @Get("/{id}")
    Maybe<Person> findById(@PathVariable Integer id);

    @Get(value = "/stream", produces = MediaType.APPLICATION_JSON_STREAM)
    Flowable<Person> findAllStream();

}
