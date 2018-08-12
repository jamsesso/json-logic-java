package jamsesso.jsonlogic.evaluator.expressions;

import jamsesso.jsonlogic.ast.JsonLogicArray;
import jamsesso.jsonlogic.ast.JsonLogicNode;
import jamsesso.jsonlogic.ast.JsonLogicNodeType;
import jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import jamsesso.jsonlogic.evaluator.JsonLogicExpression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MergeExpression implements JsonLogicExpression {
  public static final MergeExpression INSTANCE = new MergeExpression();

  private MergeExpression() {
    // Use INSTANCE instead.
  }

  @Override
  public String key() {
    return "merge";
  }

  @Override
  public Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data)
    throws JsonLogicEvaluationException {
    JsonLogicArray[] arrays = new JsonLogicArray[arguments.size()];

    for (int i = 0; i < arguments.size(); i++) {
      JsonLogicNode node = arguments.get(i);

      if (JsonLogicNodeType.ARRAY.equals(node.getType())) {
        arrays[i] = (JsonLogicArray) node;
      }
      else {
        arrays[i] = new JsonLogicArray(Collections.singletonList(node));
      }
    }

    return evaluator.evaluate(merge(arrays), data);
  }

  private JsonLogicArray merge(JsonLogicArray... arrays) {
    List<JsonLogicNode> result = new ArrayList<>();

    for (JsonLogicArray array : arrays) {
      result.addAll(array);
    }

    return new JsonLogicArray(result);
  }
}
