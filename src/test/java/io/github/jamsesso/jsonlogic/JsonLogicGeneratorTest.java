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
import io.github.jamsesso.jsonlogic.evaluator.expressions.LogicExpression;
import io.github.jamsesso.jsonlogic.generator.JsonLogicGenerator;

import static org.junit.Assert.assertEquals;

public class JsonLogicGeneratorTest {

    public static final String TAG = "JsonLogicGenerator";

    @Test
    public void testGenerateAndExpression() {
//        {
//            "and": [
//            {
//                "<": [
//                    {
//                        "var": "temp"
//                    },
//                    110.0
//                ]
//            },
//            {
//                "==": [
//                    {
//                        "var": "pie.filling"
//                    },
//                    "apple"
//                ]
//            }]
//        }
        JsonLogicString key = new JsonLogicString("temp");
        JsonLogicPrimitive value = new JsonLogicNumber(110.0);
        JsonLogicVariable var = new JsonLogicVariable(key, value);
        List<JsonLogicNode> logicNodes = new ArrayList<>();
        logicNodes.add(var);
        logicNodes.add(value);
        JsonLogicArray args = new JsonLogicArray(logicNodes);
        JsonLogicOperation lessThanOp = new JsonLogicOperation("<", args);

        key = new JsonLogicString("pie.filling");
        value = new JsonLogicString("apple");
        var = new JsonLogicVariable(key, value);
        logicNodes = new ArrayList<>();
        logicNodes.add(var);
        logicNodes.add(value);
        args = new JsonLogicArray(logicNodes);
        JsonLogicOperation equalityOp = new JsonLogicOperation("==", args);

        LogicExpression andExpression = LogicExpression.AND;
        logicNodes = new ArrayList<>();
        logicNodes.add(lessThanOp);
        logicNodes.add(equalityOp);
        args = new JsonLogicArray(logicNodes);

        String json = new JsonLogicGenerator(andExpression, args).generate();

        String expects = "{\"and\":[{\"<\":[{\"var\":\"temp\"},110.0]},{\"==\":[{\"var\":\"pie.filling\"},\"apple\"]}]}";
        assertEquals("mismatched output", expects, json);
    }
}