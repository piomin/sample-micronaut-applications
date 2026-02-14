package pl.piomin.services.secure;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import io.reactivex.rxjava3.core.Flowable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import pl.piomin.services.model.Person;

import java.util.Collections;
import java.util.Optional;

// TODO - replace auth provider with a new feature
@Singleton
public class UserPasswordAuthProvider implements AuthenticationProvider<HttpRequest<?>> {

    @Inject
    UsersStore store;

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> req) {
        String username = req.getIdentity().toString();
        String password = req.getSecret().toString();
        if (password.equals(store.getUserPassword(username))) {
            final Authentication details = Authentication.build(username, Collections.singletonList(store.getUserRole(username)));
            return Flowable.just((AuthenticationResponse) () -> Optional.of(details));
        } else {
            return Flowable.just(new AuthenticationFailed());
        }
    }
}
