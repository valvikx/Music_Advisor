package advisor.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonHelper {

    private static final JsonHelper INSTANCE = new JsonHelper();

    private JsonObject jsonObject;

    private JsonHelper() {}

    public static JsonHelper getInstance() {

        return INSTANCE;

    }

    public void buildDOM(String json) {

        jsonObject = JsonParser.parseString(json).getAsJsonObject();

    }

    public JsonHelper setJsonObject(String objectName) {

        jsonObject = jsonObject.getAsJsonObject(objectName);

        return this;

    }

    public JsonHelper setJsonObject(JsonElement jsonElement) {

        jsonObject = jsonElement.getAsJsonObject();

        return this;

    }

    public String getStringValue(String memberName) {

        return jsonObject.get(memberName).getAsString();

    }

    public int getIntValue(String memberName) {

        return jsonObject.get(memberName).getAsInt();

    }

    public JsonArray getJsonArray(String memberName) {

        return jsonObject.getAsJsonArray(memberName);

    }

    public boolean hasMember(String memberName) {

        return jsonObject.has(memberName);

    }

    public boolean isMemberNull(String memberName) {

        return jsonObject.get(memberName).isJsonNull();

    }

}
