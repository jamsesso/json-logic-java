package io.github.jamsesso.jsonlogic.generator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import io.github.jamsesso.jsonlogic.ast.JsonLogicArray;
import io.github.jamsesso.jsonlogic.ast.JsonLogicBoolean;
import io.github.jamsesso.jsonlogic.ast.JsonLogicNode;
import io.github.jamsesso.jsonlogic.ast.JsonLogicNumber;
import io.github.jamsesso.jsonlogic.ast.JsonLogicOperation;
import io.github.jamsesso.jsonlogic.ast.JsonLogicPrimitive;
import io.github.jamsesso.jsonlogic.ast.JsonLogicString;
import io.github.jamsesso.jsonlogic.ast.JsonLogicVariable;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicExpression;

public class JsonLogicGenerator {

    final private JsonLogicExpression expression;
    final private JsonLogicArray arguments;

    public JsonLogicGenerator(JsonLogicExpression expression, JsonLogicArray arguments) {
        this.expression = expression;
        this.arguments = arguments;
    }

    public String generate() {
        JsonObject rootObject = new JsonObject();
        JsonArray rootArray = new JsonArray(arguments.size());

        for (JsonLogicNode node : arguments) {
            switch (node.getType()) {
                case PRIMITIVE:
                    rootArray.add(generatePrimitive((JsonLogicPrimitive) node));
                    break;

                case VARIABLE:
                    rootArray.add(generateVariable((JsonLogicVariable) node));
                    break;

                case OPERATION:
                    rootArray.add(generateOperation((JsonLogicOperation) node));
                    break;

                case ARRAY:
            }
        }

        rootObject.add(expression.key(), rootArray);

        return rootObject.toString();
    }

    protected JsonElement generatePrimitive(JsonLogicPrimitive node) {
        switch (node.getPrimitiveType()) {
            case STRING:
                return new JsonPrimitive(((JsonLogicString)node).getValue());

            case NUMBER:
                return new JsonPrimitive(((JsonLogicNumber)node).getValue());

            case BOOLEAN:
                return new JsonPrimitive(((JsonLogicBoolean)node).getValue());

            case NULL:
                return new JsonPrimitive("null");

            default:
                throw new UnsupportedOperationException("unknown primitive type");
        }
    }

    protected JsonElement generateVariable(JsonLogicVariable variable) {
        JsonObject obj = new JsonObject();
        JsonLogicString key = (JsonLogicString) variable.getKey();
        obj.addProperty("var", key.getValue());
        return obj;
    }

    protected JsonElement generateOperation(JsonLogicOperation operation) {
        JsonLogicArray args = operation.getArguments();
        JsonArray arr = new JsonArray(args.size());
        for (JsonLogicNode arg : args) {
            switch (arg.getType()) {
                case PRIMITIVE:
                    arr.add(generatePrimitive((JsonLogicPrimitive) arg));
                    break;

                case VARIABLE:
                    arr.add(generateVariable((JsonLogicVariable) arg));
                    break;

                case OPERATION:
                    break;

                case ARRAY:
            }
        }

        JsonObject obj = new JsonObject();
        obj.add(operation.getOperator(), arr);

        return obj;
    }
}
