package io.github.jamsesso.jsonlogic;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The CustomMacroTests class contains unit tests for adding and using custom operations and macros
 * with the JsonLogic library. It demonstrates extending JsonLogic by defining and applying custom
 * behaviors to evaluate logical expressions.
 * <p>
 * This test specifically adds a custom operation "side_effect" and a custom macro "unless". The "side_effect"
 * operation sets a flag when invoked, demonstrating side-effect-based behavior. The "unless" macro evaluates
 * a conditional expression that executes an operation only if the condition is false.
 * <p>
 * The test verifies:
 * - Whether the "side_effect" operation is called based on the "unless" macro's condition.
 * - Various scenarios for the conditional logic handled by the "unless" macro.
 * <p>
 * Exceptions:
 * - JsonLogicException: Thrown if there is an error during the initialization or evaluation of JSON logic expressions.
 * - JsonLogicEvaluationException: Thrown if there are issues during the evaluation of arguments or expressions.
 */
public class CustomMacroTests {
    private static final JsonLogic jsonLogic = new JsonLogic();

    @Test
    public void testCustomMacro() throws JsonLogicException {
        final AtomicBoolean called = new AtomicBoolean(false);
        jsonLogic.addOperation("side_effect", args -> {
                    called.set(true);
                    return null;
                }
        ).addMacro("unless", args -> {
            if (args.size() > 0) {
                final boolean condition = JsonLogic.truthy(args.evaluate(0));
                if (args.size() > 1 && !condition) {
                    return args.evaluate(1);
                } else {
                    return null;
                }
            }
            return null;
        });
        called.set(false);
        jsonLogic.apply("{\"unless\": [ false , {\"side_effect\": 0}]}", null);
        Assert.assertThat(called.get(), Is.is(true));

        called.set(false);
        jsonLogic.apply("{\"unless\": [ true , {\"side_effect\": 0}]}", null);
        Assert.assertThat(called.get(), Is.is(false));
    }
}
