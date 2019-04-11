package pl.piomin.services.beans;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BeginService {

    @Inject
    MiddleService service;

    public void start(String correlationId) {
        service.setCorrelationId(correlationId);
    }

}
