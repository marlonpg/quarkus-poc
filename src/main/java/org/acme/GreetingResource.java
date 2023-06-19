package org.acme;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    private final MeterRegistry registry;

    public GreetingResource(MeterRegistry registry) {
        this.registry = registry;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        registry.counter("hello.request.counter").increment();
        return "Hello from RESTEasy Reactive";
    }
}
