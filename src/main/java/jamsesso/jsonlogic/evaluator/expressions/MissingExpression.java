package jamsesso.jsonlogic.evaluator.expressions;

import jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import jamsesso.jsonlogic.utils.ArrayLike;
import jamsesso.jsonlogic.utils.MapLike;

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
    if (!MapLike.isEligible(data)) {
      throw new JsonLogicEvaluationException(key() + " only works when the data is a map");
    }

    if (isSome && (!ArrayLike.isEligible(arguments.get(1)) || !(arguments.get(0) instanceof Double))) {
      throw new JsonLogicEvaluationException("missing_some expects first argument to be an integer and the second " +
                                             "argument to be an array");
    }

    Map map = new MapLike(data);
    List options = isSome ? new ArrayLike(arguments.get(1)) : arguments;
    Set providedKeys = map.keySet();
    Set requiredKeys = new HashSet<>(options);

    requiredKeys.removeAll(providedKeys); // Keys that I need but do not have

    if (isSome && options.size() - requiredKeys.size() >= ((Double) arguments.get(0)).intValue()) {
      return Collections.EMPTY_LIST;
    }

    return new ArrayList<>(requiredKeys);
  }
}
