package io.github.jamsesso.jsonlogic;

import io.github.jamsesso.jsonlogic.ast.*;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import io.github.jamsesso.jsonlogic.evaluator.expressions.InExpression;
import org.junit.Test;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class InExpressionTests {
  private static final JsonLogic jsonLogic = new JsonLogic();
  private static final String CODES = "[\"A\",\"B\",\"C\",\"D\",\"E\",\"F\",\"G\",\"H\","
    + "\"I\",\"J\",\"K\",\"L\",\"M\",\"N\",\"1\",\"A\"]";
  private static final String CODE_RULE = "{\"in\":[{\"var\":\"code\"}," + CODES + "]}";

  @Test
  public void indexedLookupPreservesLocalizedArgumentPath() throws JsonLogicException {
    Locale previous = Locale.getDefault(Locale.Category.FORMAT);
    try {
      Locale.setDefault(Locale.Category.FORMAT, Locale.forLanguageTag("ar-EG"));
      String expected = String.format("$.in[%d]", 0);
      try {
        jsonLogic.apply("{\"in\":[{\"undefined\":[]}," + CODES + "]}", null);
        fail("Expected an undefined operation error");
      } catch (JsonLogicException e) {
        assertEquals(expected, e.getJsonPath());
      }
    } finally {
      Locale.setDefault(Locale.Category.FORMAT, previous);
    }
  }

  @Test
  public void fixedStringListMatchesEachInputWithoutCoercion() throws JsonLogicException {
    assertEquals(true, jsonLogic.apply(CODE_RULE, Collections.singletonMap("code", "N")));
    assertEquals(false, jsonLogic.apply(CODE_RULE, Collections.singletonMap("code", "Z")));
    assertEquals(true, jsonLogic.apply(CODE_RULE, Collections.singletonMap("code", "1")));
    assertEquals(false, jsonLogic.apply(CODE_RULE, Collections.singletonMap("code", 1)));
    assertEquals(false, jsonLogic.apply(CODE_RULE, Collections.emptyMap()));
  }

  @Test
  public void expressionInsideListStillUsesCurrentData() throws JsonLogicException {
    String rule = CODE_RULE.replace("\"N\"", "{\"var\":\"allowed\"}");
    assertEquals(true, jsonLogic.apply(rule, Collections.singletonMap("code", "A")));
    assertEquals(false, jsonLogic.apply(rule, Collections.singletonMap("code", "N")));
    Map<String, String> data = new HashMap<>();
    data.put("code", "N");
    data.put("allowed", "N");
    assertEquals(true, jsonLogic.apply(rule, data));
  }

  @Test
  public void overriddenInReceivesOriginalStringsInOrder() throws JsonLogicException {
    JsonLogic logic = new JsonLogic();
    assertEquals(true, logic.apply(CODE_RULE, Collections.singletonMap("code", "N")));
    logic.addOperation("in", args -> args[1]);
    assertEquals(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H",
      "I", "J", "K", "L", "M", "N", "1", "A"), logic.apply(CODE_RULE, null));
  }

  @Test
  public void computedNonStringNeedleIsEvaluatedOnce() throws JsonLogicException {
    AtomicInteger calls = new AtomicInteger();
    JsonLogic logic = new JsonLogic().addOperation("next", args -> calls.incrementAndGet());
    String rule = "{\"in\":[{\"next\":[]}," + CODES + "]}";
    assertEquals(false, logic.apply(rule, null));
    assertEquals(1, calls.get());
  }

  @Test
  public void testStringIn() throws JsonLogicException {
    assertEquals(true, jsonLogic.apply("{\"in\": [\"race\", \"racecar\"]}", null));
  }

  @Test
  public void testStringNotIn() throws JsonLogicException {
    assertEquals(false, jsonLogic.apply("{\"in\": [\"race\", \"clouds\"]}", null));
    assertEquals(false, jsonLogic.apply("{\"in\": [null, \"clouds\"]}", null));
  }

  @Test
  public void testArrayIn() throws JsonLogicException {
    assertEquals(true, jsonLogic.apply("{\"in\": [1, [1, 2, 3]]}", null));
    assertEquals(true, jsonLogic.apply("{\"in\": [4.56, [1, 2, 3, 4.56]]}", null));
    assertEquals(true, jsonLogic.apply("{\"in\": [null, [1, 2, 3, null]]}", null));
  }

  @Test
  public void testArrayNotIn() throws JsonLogicException {
    assertEquals(false, jsonLogic.apply("{\"in\": [5, [1, 2, 3]]}", null));
    assertEquals(false, jsonLogic.apply("{\"in\": [null, [1, 2, 3]]}", null));
  }

  @Test
  public void testInVariableInt() throws JsonLogicException {
    Map data = Collections.singletonMap("list", Arrays.asList(1, 2, 3));
    assertEquals(true, jsonLogic.apply("{\"in\": [2, {\"var\": \"list\"}]}", data));
  }

  @Test
  public void readsTheWholeInputListEvenAfterFirstMatch() throws JsonLogicException {
    AtomicInteger reads = new AtomicInteger();
    List<Integer> items = new AbstractList<Integer>() {
      @Override
      public Integer get(int index) {
        reads.incrementAndGet();
        return 42;
      }

      @Override
      public int size() {
        return 2;
      }
    };
    assertEquals(true, jsonLogic.apply("{\"in\":[42,{\"var\":\"\"}]}", items));
    assertEquals(2, reads.get());
  }

  @Test
  public void editsThroughLiteralListViewsAffectTheNextEvaluation() throws JsonLogicException {
    JsonLogicOperation op = (JsonLogicOperation) JsonLogicParser.parse("{\"in\":[\"B\"," + CODES + "]}");
    JsonLogicEvaluator evaluator = new JsonLogicEvaluator(Collections.singletonList(InExpression.INSTANCE));
    JsonLogicArray literals = (JsonLogicArray) op.getArguments().get(1);
    assertEquals(true, evaluator.evaluate(op, null, "$"));

    ListIterator<JsonLogicNode> it = literals.listIterator(1);
    it.next();
    it.remove();
    assertEquals(false, evaluator.evaluate(op, null, "$"));
    it.add(new JsonLogicString("B"));
    assertEquals(true, evaluator.evaluate(op, null, "$"));
    literals.subList(1, 2).clear();
    assertEquals(false, evaluator.evaluate(op, null, "$"));
  }

  @Test
  public void indexedMatchStillEvaluatesAnAppendedArgument() throws JsonLogicException {
    JsonLogicOperation op = (JsonLogicOperation) JsonLogicParser.parse("{\"in\":[\"A\"," + CODES + "]}");
    JsonLogicEvaluator evaluator = new JsonLogicEvaluator(Collections.singletonList(InExpression.INSTANCE));
    JsonLogicArray args = op.getArguments();
    args.listIterator(args.size()).add(JsonLogicParser.parse("{\"undefined\":[]}"));
    try {
      evaluator.evaluate(op, null, "$");
      fail("Expected an undefined operation error in the appended argument");
    } catch (JsonLogicException e) {
      assertEquals(String.format("$.in[%d]", 2), e.getJsonPath());
    }
  }

  @Test(expected = IllegalStateException.class)
  public void laterListReadCanStillFailAfterAMatch() throws JsonLogicException {
    List<String> items = new AbstractList<String>() {
      @Override
      public String get(int index) {
        if (index == 0) return "found";
        throw new IllegalStateException("later read failed");
      }

      @Override
      public int size() { return 2; }
    };
    jsonLogic.apply("{\"in\":[\"found\",{\"var\":\"\"}]}", items);
  }

  @Test
  public void testNotInVariableInt() throws JsonLogicException {
    Map data = Collections.singletonMap("list", Arrays.asList(1, 2, 3));
    assertEquals(false, jsonLogic.apply("{\"in\": [4, {\"var\": \"list\"}]}", data));
    assertEquals(false, jsonLogic.apply("{\"in\": [4, {\"var\": \"list\"}]}", null));
  }

  @Test
  public void testAllVariables() throws JsonLogicException {
    Map data = Stream.of(new Object[][] {
      new Object[] {"list", Arrays.asList(1, 2, 3)},
      new Object[] {"value", 3}
    }).collect(Collectors.toMap(o -> o[0], o -> o[1]));

    assertEquals(true, jsonLogic.apply("{\"in\": [{\"var\": \"value\"}, {\"var\": \"list\"}]}", data));
    assertEquals(false, jsonLogic.apply("{\"in\": [{\"var\": \"value\"}, {\"var\": \"list\"}]}", null));
  }

  @Test
  public void testSingleArgument() throws JsonLogicException {
    assertFalse((boolean) jsonLogic.apply("{\"in\": [\"Spring\"]}", null));
  }

  @Test
  public void testBadSecondArgument() throws JsonLogicException {
    assertFalse((boolean) jsonLogic.apply("{\"in\": [\"Spring\", 3]}", null));
  }
}
