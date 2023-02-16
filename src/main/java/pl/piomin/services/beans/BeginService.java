package pl.piomin.services.beans;


import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class BeginService {

    @Inject
    MiddleService service;

    public void start(String correlationId) {
        service.setCorrelationId(correlationId);
    }

}
