package by.valvik.musicadvisor.json;

import by.valvik.musicadvisor.annotation.Singleton;
import by.valvik.musicadvisor.domain.Item;
import by.valvik.musicadvisor.domain.Items;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

@Singleton
public class JsonMapper {

    private final Gson gson;

    public JsonMapper() {

        gson = new GsonBuilder().setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create();

    }

    public <T> T toEntity(String json, Class<T> entityClass) {

        return gson.fromJson(json, entityClass);

    }

    public <T extends Item> Items<T> toItems(JsonElement jsonElement, Type typeToken) {

        return gson.fromJson(jsonElement,typeToken);

    }

}
