package io.github.jamsesso.jsonlogic.evaluator;

import io.github.jamsesso.jsonlogic.ast.JsonLogicArray;
import io.github.jamsesso.jsonlogic.ast.JsonLogicNode;

/**
 * Represents a container for handling and evaluating macro arguments in the context of JsonLogic operations.
 * This class provides utilities to evaluate specific arguments and determine the total number of arguments.
 */
public class MacroArguments {
  private final JsonLogicEvaluator evaluator;
  private final JsonLogicArray arguments;
  private final Object data;
  private final String jsonPath;
  public MacroArguments(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data, String jsonPath) {
    this.evaluator = evaluator;
    this.arguments = arguments;
    this.data = data;
    this.jsonPath = jsonPath;
  }

    /**
     * Evaluates the argument at the specified index using the provided evaluator, data, and JSON path context.
     * <p>
     * Macro implementations invoke this method to get the evaluated value of each argument they need to evaluate.
     * Macros can skip and ignore, OR evaluate each argument.
     * Macros can evaluate arguments in any order.
     * Macros can evaluate any argument multiple times.
     *
     * @param i the index of the argument to evaluate, starting at 0 and less than the total number of arguments
     * @return the result of the evaluation as an Object
     * @throws JsonLogicEvaluationException if an error occurs during evaluation
     */
  public Object evaluate(int i) throws JsonLogicEvaluationException {
    return evaluator.evaluate(arguments.get(i), data, String.format("%s[%d]", jsonPath, i));
  }

    /**
     * Retrieves the argument at the specified index.
     *
     * @param i the index of the argument to retrieve, starting at zero and less than the total number of arguments
     * @return the argument at the specified index as a {@code JsonLogicNode}
     */
  public JsonLogicNode get(int i) {
    return arguments.get(i);
  }

    /**
     * Get the total number of arguments.
     * Indexing in {@link #evaluate(int)} goes from zero to {@code size() - 1}.
     *
     * @return the total number of arguments
     */
  public int size(){
    return arguments.size();
  }
}
