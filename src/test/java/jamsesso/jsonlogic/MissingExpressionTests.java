package jamsesso.jsonlogic;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MissingExpressionTests {
  private static final JsonLogic jsonLogic = new JsonLogic();

  @Test
  public void testMissing() throws JsonLogicException {
    Map<String, Object> data = new HashMap<String, Object>() {{
      put("a", "apple");
      put("c", "carrot");
    }};
    Object result = jsonLogic.apply("{\"missing\": [\"a\", \"b\"]}", data);

    assertEquals(1, ((List) result).size());
    assertEquals("b", ((List) result).get(0));
  }

  @Test
  public void testMissingSomeUnderThreshold() throws JsonLogicException {
    Map<String, Object> data = new HashMap<String, Object>() {{
      put("a", "apple");
      put("c", "carrot");
    }};
    Object result = jsonLogic.apply("{\"missing_some\": [1, [\"a\", \"b\"]]}", data);

    assertEquals(0, ((List) result).size());
  }

  @Test
  public void testMissingSomeOverThreshold() throws JsonLogicException {
    Map<String, Object> data = new HashMap<String, Object>() {{
      put("a", "apple");
    }};
    Object result = jsonLogic.apply("{\"missing_some\": [2, [\"a\", \"b\", \"c\"]]}", data);

    assertEquals(2, ((List) result).size());
    assertEquals("b", ((List) result).get(0));
    assertEquals("c", ((List) result).get(1));
  }

  @Test
  public void testMissingSomeComplexExpression() throws JsonLogicException {
    Map<String, Object> data = new HashMap<String, Object>() {{
      put("first_name", "Bruce");
      put("last_name", "Wayne");
    }};
    String json = "{\"if\" :[\n" +
                  "  {\"merge\": [\n" +
                  "    {\"missing\":[\"first_name\", \"last_name\"]},\n" +
                  "    {\"missing_some\":[1, [\"cell_phone\", \"home_phone\"] ]}\n" +
                  "  ]},\n" +
                  "  \"We require first name, last name, and one phone number.\",\n" +
                  "  \"OK to proceed\"\n" +
                  "]}";
    Object result = jsonLogic.apply(json, data);

    assertEquals("We require first name, last name, and one phone number.", result);
  }
}
