package jamsesso.jsonlogic.evaluator.expressions;

import jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import jamsesso.jsonlogic.utils.IndexedStructure;

import java.util.*;

public class MissingExpression implements PreEvaluatedArgumentsExpression {
  public static final MissingExpression ALL = new MissingExpression(false);
  public static final MissingExpression SOME = new MissingExpression(true);

  private final boolean isSome;

  private MissingExpression(boolean isSome) {
    this.isSome = isSome;
  }

  @Override
  public String key() {
    return isSome ? "missing_some" : "missing";
  }

  @Override
  public Object evaluate(List arguments, Object data) throws JsonLogicEvaluationException {
    if (!(data instanceof Map)) {
      throw new JsonLogicEvaluationException(key() + " only works when the data is a map");
    }

    if (isSome && (!IndexedStructure.isEligible(arguments.get(1)) || !(arguments.get(0) instanceof Double))) {
      throw new JsonLogicEvaluationException("missing_some expects first argument to be an integer and the second " +
                                             "argument to be an array");
    }

    List options = isSome ? new IndexedStructure(arguments.get(1)) : arguments;
    Set providedKeys = ((Map) data).keySet();
    Set requiredKeys = new HashSet<>(options);

    requiredKeys.removeAll(providedKeys); // Keys that I need but do not have

    if (isSome && options.size() - requiredKeys.size() >= ((Double) arguments.get(0)).intValue()) {
      return Collections.EMPTY_LIST;
    }

    return new ArrayList<>(requiredKeys);
  }
}
