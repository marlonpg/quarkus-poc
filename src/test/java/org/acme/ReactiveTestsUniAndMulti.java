package org.acme;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReactiveTestsUniAndMulti {

    @Test
    public void test_uni() {
        UniAssertSubscriber<String> tester = Uni.createFrom().item("test my")
                .onItem().transform(s -> s + " first Uni!")
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        tester.assertCompleted();
        assertEquals("test my first Uni!", tester.getItem());

    }

    @Test
    public void test_uni_error() {
        UniAssertSubscriber<String> tester = Uni.createFrom().item("error")
                .onItem().transform(this::getString)
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        tester.assertFailedWith(RuntimeException.class, "error");
    }

    @Test
    public void test_multi() {
        AssertSubscriber<Integer> integerMultiEmitterProcessor = Multi.createFrom().items(1, 2, 3, 4, 5)
                .onItem().transform(integer -> integer * 2)
                .subscribe().withSubscriber(AssertSubscriber.create(5));

        integerMultiEmitterProcessor.assertCompleted()
                .assertItems(2, 4, 6, 8, 10);
    }

    @Test
    public void test_multi_error() {
        AssertSubscriber<Integer> integerMultiEmitterProcessor = Multi.createFrom().items(1, 2, 3, 4, 5)
                .onItem().transform(integer -> integer * 2)
                .onItem().transform(integer -> {
                    if (integer == 8) {
                        throw new RuntimeException("Error");
                    }
                    return integer;
                })
                .subscribe().withSubscriber(AssertSubscriber.create(4));

        integerMultiEmitterProcessor.awaitFailure()
                .assertItems(2, 4, 6);
    }

    @Test
    public void test_multi_emitter() {
        AssertSubscriber<Integer> integerMultiEmitterProcessor = Multi.createFrom().items(1, 2, 3, 4, 5)
                .onItem().transform(integer -> integer * 2)
                .subscribe().withSubscriber(AssertSubscriber.create(5));

        integerMultiEmitterProcessor.assertCompleted()
                .assertItems(2, 4, 6, 8, 10);
    }

    @Test
    public void test_multi_to_uni() {
        UniAssertSubscriber<List<Integer>> unitList = Multi.createFrom()
                .items(1, 2, 3, 4, 5)
                .collect()
                .asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        unitList.assertCompleted();
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), unitList.getItem());
    }

    @Test
    public void test_multi_events() {

    }

    private String getString(String s) {
        if (s.equals("error")) {
            throw new RuntimeException("error");
        }
        return "success";
    }

    private Uni<String> getDelayedUni() {
        return Uni.createFrom().item("Delayed")
                .onItem().delayIt().by(Duration.ofSeconds(1));
    }
}
