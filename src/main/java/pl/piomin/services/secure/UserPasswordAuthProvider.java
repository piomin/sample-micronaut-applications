package pl.piomin.services.secure;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import io.reactivex.rxjava3.core.Flowable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;

import java.util.Collections;
import java.util.Optional;

@Singleton
public class UserPasswordAuthProvider implements AuthenticationProvider {

    @Inject
    UsersStore store;

    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> req) {
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
