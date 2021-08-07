package by.valvik.musicadvisor.json;

import by.valvik.musicadvisor.context.annotation.Singleton;
import by.valvik.musicadvisor.exception.UtilException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import static com.google.gson.JsonParser.parseString;

@Singleton
public class JsonHandler {

    private JsonObject rootJsonObject;

    public JsonHandler buildDOM(String json) throws UtilException {

        try {

            rootJsonObject = parseString(json).getAsJsonObject();

            return this;

        } catch (JsonSyntaxException e) {

            throw new UtilException(e.getMessage());

        }

    }

    public JsonObject getJsonObject(String objectName) {

        return rootJsonObject.getAsJsonObject(objectName);

    }

    public boolean hasMember(String memberName) {

        return rootJsonObject.has(memberName);

    }

}
