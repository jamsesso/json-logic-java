package io.github.jamsesso.jsonlogic.evaluator;

/**
 * Represents a macro operation that can be evaluated within a JsonLogic context.
 * A macro is a reusable, parameterized logic component that operates on provided arguments
 * and evaluates to a resulting value.
 * <p>
 * The evaluation of a macro is carried out using a {@link MacroArguments} object, which provides
 * access to the arguments, their evaluated values, and contextual logic data.
 * <p>
 * The `evaluate` method is designed to be implemented by any class adhering to this interface,
 * allowing for custom logic evaluation handling.
 */
public interface Macro {
  Object evaluate(MacroArguments evaluator) throws JsonLogicEvaluationException;
}
