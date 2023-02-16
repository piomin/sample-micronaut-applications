package pl.piomin.services.beans;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest
public class ClientScopeTests {

    ClientService client;
    ClientService client2;
    ClientService client3;

    public ClientScopeTests(@Named("client1") ClientService client,
                            @Named("client2") ClientService client2,
                            @Named("client3") ClientService client3) {
        this.client = client;
        this.client2 = client2;
        this.client3 = client3;
    }

    @Test
    public void testClient() {
        String url = client.connect();
        Assertions.assertEquals("http://localhost:8080", url);
        url = client2.connect();
        Assertions.assertEquals("http://localhost:8090", url);
        url = client3.connect();
        Assertions.assertEquals("http://localhost:8100", url);
    }
}
