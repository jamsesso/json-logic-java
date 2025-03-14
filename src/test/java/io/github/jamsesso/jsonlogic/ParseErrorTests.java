package io.github.jamsesso.jsonlogic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ParseErrorTests {
  private static final JsonLogic jsonLogic = new JsonLogic();
  private static final Map<String, Object> empty = new HashMap<>();
  private static final Map<String, Object> arrayData = Collections.singletonMap("key",
      Stream.of(1, 2).collect(Collectors.toList()));

  private void testPath(String expression, String jsonPath, String msg) {
    testPath(expression, jsonPath, msg, empty);
  }
  private void testPath(String expression, String jsonPath, String msg, Map<String, Object> data) {
    try {
      jsonLogic.apply(expression, data);
      fail("Expected an exception at " + jsonPath);
    } catch (JsonLogicException e) {
      assertEquals(e.getMessage(), jsonPath, e.getJsonPath());
      assertEquals(msg, e.getMessage());
    }
  }

  @Test
  public void testJsonPath() {
    testPath("{}", "$", "objects must have exactly 1 key defined, found 0");
    testPath("{\"all\":[1]}","$.all","all expects exactly 2 arguments");
    testPath("{\"all\":[{\"some\":[]}, 1]}","$.all[0].some","some expects exactly 2 arguments");
    testPath("{\"all\":[[1], {\"none\":[]}]}","$.all[1].none","none expects exactly 2 arguments");
    testPath("{\"some\":[0, 1]}","$.some[0]","first argument to some must be a valid array");
    testPath("{\"none\":[1, 2]}","$.none[0]","first argument to none must be a valid array");
    testPath("{\"cat\":[\"foo\", {}]}","$.cat[1]","objects must have exactly 1 key defined, found 0");
    testPath("{\"==\":[0]}","$.==","equality expressions expect exactly 2 arguments");
    testPath("{\"filter\":[0]}","$.filter","filter expects exactly 2 arguments");
    testPath("{\"filter\":[1, 2]}","$.filter[0]","first argument to filter must be a valid array");
    testPath("{\"filter\":[\"foo\", {}]}","$.filter[1]","objects must have exactly 1 key defined, found 0");
    testPath("{\"if\":[{}]}","$.if[0]","objects must have exactly 1 key defined, found 0");
    testPath("{\"if\":[true,{}]}","$.if[1]","objects must have exactly 1 key defined, found 0");
    testPath("{\"if\":[false,1,{\"filter\":[1]}]}","$.if[2].filter","filter expects exactly 2 arguments");
    testPath("{\"if\":[false,1,true,{\"filter\":[1]}]}","$.if[3].filter","filter expects exactly 2 arguments");
    testPath("{\"!=\":[0]}","$.!=","equality expressions expect exactly 2 arguments");
    testPath("{\"log\":[]}","$.log","log operator requires exactly 1 argument");
    testPath("{\"and\":[]}","$.and","and operator expects at least 1 argument");
    testPath("{\"or\":[]}","$.or","or operator expects at least 1 argument");
    testPath("{\"map\":[1]}","$.map","map expects exactly 2 arguments");
    testPath("{\"+\":[1, {\"-\": [2, {\"*\": [3, {\"/\": [4, {\"log\":[]}]}]}]}]}","$.+[1].-[1].*[1]./[1].log","log operator requires exactly 1 argument");
    testPath("{\"%\":[1, {\"min\": [2, {\"max\": [3, {\"log\":[]}]}]}]}","$.%[1].min[1].max[1].log","log operator requires exactly 1 argument");
    testPath("{\"merge\":[1, 2, {\"log\":[]}]}","$.merge[2].log","log operator requires exactly 1 argument");
    testPath("{\"missing_some\":[1]}","$.missing_some","missing_some expects first argument to be an integer and the second argument to be an array");
    testPath("{\"missing_some\":[1, 2]}","$.missing_some","missing_some expects first argument to be an integer and the second argument to be an array");
    testPath("{\"missing\":[1, {\"log\":[]}]}","$.missing[1].log","log operator requires exactly 1 argument");
    testPath("{\"!\":[1, {\"log\":[]}]}","$.![1].log","log operator requires exactly 1 argument");
    testPath("{\"!!\":[{\"log\":[]}]}","$.!![0].log","log operator requires exactly 1 argument");
    testPath("{\"<\":[1]}","$.<","'<' requires at least 2 arguments");
    testPath("{\">\":[1]}","$.>","'>' requires at least 2 arguments");
    testPath("{\"<=\":[1]}","$.<=","'<=' requires at least 2 arguments");
    testPath("{\">=\":[1]}","$.>=","'>=' requires at least 2 arguments");
    testPath("{\">\":[1, {\"log\":[]}]}","$.>[1].log","log operator requires exactly 1 argument");
    testPath("{\">\":[0, 1, 2, \"three\", 4, 5, {\"log\":[]}]}","$.>[6].log","log operator requires exactly 1 argument");
    testPath("{\"reduce\":[1]}","$.reduce","reduce expects exactly 3 arguments");
    testPath("{\"reduce\":[{\"log\":[]}, 2, 3]}","$.reduce[0].log","log operator requires exactly 1 argument");
    testPath("{\"reduce\":[[1], {\"log\":[]}, 3]}","$.reduce[1].log","log operator requires exactly 1 argument");
    testPath("{\"reduce\":[1, 2, {\"log\":[]}]}","$.reduce[2].log","log operator requires exactly 1 argument");
    testPath("{\"===\":[1]}","$.===","equality expressions expect exactly 2 arguments");
    testPath("{\"!==\":[1]}","$.!==","equality expressions expect exactly 2 arguments");
    testPath("{\"substr\":[\"jsonlogic\"]}","$.substr","substr expects 2 or 3 arguments");
    testPath("{\"substr\":[\"jsonlogic\", \"one\"]}","$.substr[1]","second argument to substr must be a number");
    testPath("{\"substr\":[\"jsonlogic\", 1, \"two\"]}","$.substr[2]","third argument to substr must be an integer");
    testPath("{\"var\":[[1, 2]]}","$.var[0]","var first argument must be null, number, or string");
    testPath("{\"var\":\"key.foo\"}","$.var[0]","java.lang.NumberFormatException: For input string: \"foo\"", arrayData);
  }
}
