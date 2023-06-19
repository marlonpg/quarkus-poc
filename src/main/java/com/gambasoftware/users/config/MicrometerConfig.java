package com.gambasoftware.users.config;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.config.MeterFilter;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Arrays;

@Singleton
public class MicrometerConfig {

    @ConfigProperty(name = "env")
    String env;

    @Produces
    @Singleton
    public MeterFilter configureForAll() {
        return MeterFilter.commonTags(Arrays.asList(Tag.of("env", env)));
    }
}
