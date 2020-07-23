package io.github.jamsesso.jsonlogic.generator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.jamsesso.jsonlogic.ast.JsonLogicArray;
import io.github.jamsesso.jsonlogic.ast.JsonLogicBoolean;
import io.github.jamsesso.jsonlogic.ast.JsonLogicNode;
import io.github.jamsesso.jsonlogic.ast.JsonLogicNumber;
import io.github.jamsesso.jsonlogic.ast.JsonLogicOperation;
import io.github.jamsesso.jsonlogic.ast.JsonLogicPrimitive;
import io.github.jamsesso.jsonlogic.ast.JsonLogicString;
import io.github.jamsesso.jsonlogic.ast.JsonLogicVariable;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicExpression;

public class AylaJsonLogicGenerator extends JsonLogicGenerator {

    public AylaJsonLogicGenerator(JsonLogicExpression expression, JsonLogicArray arguments) {
        super(expression, arguments);
    }

    @Override
    protected JsonElement generatePrimitive(JsonLogicPrimitive node) {
        String key = "val";
        JsonObject obj = new JsonObject();
        switch (node.getPrimitiveType()) {
            case STRING:
                obj.addProperty(key, ((JsonLogicString)node).getValue());
                break;

            case NUMBER:
                obj.addProperty(key, ((JsonLogicNumber)node).getValue());
                break;

            case BOOLEAN:
                obj.addProperty(key, ((JsonLogicBoolean)node).getValue());
                break;

            case NULL:
                obj.addProperty(key, "null");
        }

        return obj;
    }

    @Override
    protected JsonElement generateVariable(JsonLogicVariable variable) {
        // variable is using as key-value pair
        JsonObject obj = new JsonObject();
        JsonLogicString key = (JsonLogicString) variable.getKey();
        JsonLogicPrimitive value = (JsonLogicPrimitive) variable.getDefaultValue();
        switch (value.getPrimitiveType()) {
            case STRING:
                obj.addProperty(key.getValue(), ((JsonLogicString)value).getValue());
                break;

            case NUMBER:
                obj.addProperty(key.getValue(), ((JsonLogicNumber)value).getValue());
                break;

            case BOOLEAN:
                obj.addProperty(key.getValue(), ((JsonLogicBoolean)value).getValue());
                break;

            case NULL:
                obj.addProperty(key.getValue(), "null");
                break;

            default:
                throw new UnsupportedOperationException("unknown primitive type");
        }

        return obj;
    }

    @Override
    protected JsonElement generateOperation(JsonLogicOperation operation) {
        JsonLogicArray args = operation.getArguments();
        JsonObject value = new JsonObject();
        JsonArray arr = new JsonArray();
        for (JsonLogicNode arg : args) {
            switch (arg.getType()) {
                case VARIABLE:
                    JsonLogicVariable var = (JsonLogicVariable) arg;
                    String prop = ((JsonLogicString) var.getKey()).getValue();
                    JsonLogicPrimitive val = (JsonLogicPrimitive) var.getDefaultValue();
                    value.add(prop, super.generatePrimitive(val));
                    break;

                case OPERATION:
                    JsonLogicOperation op = (JsonLogicOperation) arg;
                    arr.add(generateOperation(op));
                    break;

                case PRIMITIVE:
                    arr.add(generatePrimitive((JsonLogicPrimitive)arg));
                    break;

                case ARRAY:
                    throw new UnsupportedOperationException(arg.getType() + " is not allowed now");
            }
        }

        if (value.size() > 0) {
            arr.add(value);
        }

        JsonObject obj = new JsonObject();
        obj.add(operation.getOperator(), arr);

        return obj;
    }
}
