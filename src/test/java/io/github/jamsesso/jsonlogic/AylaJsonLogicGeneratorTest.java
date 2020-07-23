package io.github.jamsesso.jsonlogic;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.github.jamsesso.jsonlogic.ast.JsonLogicArray;
import io.github.jamsesso.jsonlogic.ast.JsonLogicNode;
import io.github.jamsesso.jsonlogic.ast.JsonLogicNumber;
import io.github.jamsesso.jsonlogic.ast.JsonLogicOperation;
import io.github.jamsesso.jsonlogic.ast.JsonLogicPrimitive;
import io.github.jamsesso.jsonlogic.ast.JsonLogicString;
import io.github.jamsesso.jsonlogic.ast.JsonLogicVariable;
import io.github.jamsesso.jsonlogic.evaluator.expressions.AylaNumericComparisonExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.LogicExpression;
import io.github.jamsesso.jsonlogic.generator.AylaJsonLogicGenerator;

import static org.junit.Assert.assertEquals;

public class AylaJsonLogicGeneratorTest {

    @Test
    public void testGenerateEqualExpression() {
//        {
//            "eq": [
//            {
//                "get_prop": [{
//                    "prop": "temperature",
//                    "dsn": "VR000222222",
//                    "gw": "GW000222222"
//                }]
//            },
//            {
//                "val": 25.0
//            }]
//        }
        List<JsonLogicNode> logicNodes = new ArrayList<>();

        JsonLogicString key = new JsonLogicString("prop");
        JsonLogicPrimitive value = new JsonLogicString("temperature");
        JsonLogicVariable var = new JsonLogicVariable(key, value);
        logicNodes.add(var);

        key = new JsonLogicString("dsn");
        value = new JsonLogicString("VR000222222");
        var = new JsonLogicVariable(key, value);
        logicNodes.add(var);

        key = new JsonLogicString("gw");
        value = new JsonLogicString("GW000222222");
        var = new JsonLogicVariable(key, value);
        logicNodes.add(var);

        JsonLogicArray args = new JsonLogicArray(logicNodes);
        JsonLogicOperation getPropOp = new JsonLogicOperation("get_prop", args);

        value = new JsonLogicNumber(25.0);
        logicNodes = new ArrayList<>();
        logicNodes.add(getPropOp);
        logicNodes.add(value);
        args = new JsonLogicArray(logicNodes);

        String json = new AylaJsonLogicGenerator(AylaNumericComparisonExpression.EQ, args).generate();

        String expects = "{\"eq\":[{\"get_prop\":[{\"prop\":\"temperature\",\"dsn\":\"VR000222222\",\"gw\":\"GW000222222\"}]},{\"val\":25.0}]}";
        assertEquals("mismatched output", expects, json);
    }

    @Test
    public void testGenerateAndExpression() {
// {
//   "and": [
//     {
//       "gt": [
//         {
//           "get_prop": [{
//             "prop": "temperature",
//             "dsn": "VR000111111",
//             "gw": "GW000111111"
//           }]
//         },
//         {
//           "get_prop": [{
//             "prop": "temperature",
//             "dsn": "VR000222222",
//             "gw": "GW000222222"
//           }]
//         }

//       ]
//     },
//     {
//       "eq": [
//         {
//           "get_prop": [{
//             "prop": "temperature",
//             "dsn": "VR000222222",
//             "gw": "GW000222222"
//           }]
//         },
//         {
//           "val": 25.0
//         }
//       ]
//     }
//   ]
// }
        List<JsonLogicNode> logicNodes = new ArrayList<>();
        logicNodes.add(new JsonLogicVariable(new JsonLogicString("prop"), new JsonLogicString("temperature")));
        logicNodes.add(new JsonLogicVariable(new JsonLogicString("dsn"), new JsonLogicString("VR000111111")));
        logicNodes.add(new JsonLogicVariable(new JsonLogicString("gw"), new JsonLogicString("GW000111111")));
        JsonLogicOperation getProp1 = new JsonLogicOperation("get_prop", new JsonLogicArray(logicNodes));

        logicNodes = new ArrayList<>();
        logicNodes.add(new JsonLogicVariable(new JsonLogicString("prop"), new JsonLogicString("temperature")));
        logicNodes.add(new JsonLogicVariable(new JsonLogicString("dsn"), new JsonLogicString("VR000222222")));
        logicNodes.add(new JsonLogicVariable(new JsonLogicString("gw"), new JsonLogicString("GW000222222")));
        JsonLogicOperation getProp2 = new JsonLogicOperation("get_prop", new JsonLogicArray(logicNodes));

        logicNodes = new ArrayList<>();
        logicNodes.add(getProp1);
        logicNodes.add(getProp2);
        JsonLogicOperation gtOp = new JsonLogicOperation("gt", new JsonLogicArray(logicNodes));

        logicNodes = new ArrayList<>();
        logicNodes.add(getProp2);
        logicNodes.add(new JsonLogicNumber(25.0));
        JsonLogicOperation eqOp = new JsonLogicOperation("eq", new JsonLogicArray(logicNodes));

        logicNodes = new ArrayList<>();
        logicNodes.add(gtOp);
        logicNodes.add(eqOp);

        String json = new AylaJsonLogicGenerator(LogicExpression.AND, new JsonLogicArray(logicNodes)).generate();

        String expects = "{\"and\":[{\"gt\":[{\"get_prop\":[{\"prop\":\"temperature\",\"dsn\":\"VR000111111\",\"gw\":\"GW000111111\"}]},{\"get_prop\":[{\"prop\":\"temperature\",\"dsn\":\"VR000222222\",\"gw\":\"GW000222222\"}]}]},{\"eq\":[{\"get_prop\":[{\"prop\":\"temperature\",\"dsn\":\"VR000222222\",\"gw\":\"GW000222222\"}]},{\"val\":25.0}]}]}";
        assertEquals("mismatched output", expects, json);
    }
}