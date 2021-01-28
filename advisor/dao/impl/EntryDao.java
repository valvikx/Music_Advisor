package advisor.dao.impl;

import advisor.dao.IEntryDao;
import advisor.exception.AdvisorException;
import advisor.http.Client;
import advisor.json.JsonDocument;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EntryDao implements IEntryDao {

    private final Client client = Client.getInstance();

    private final JsonDocument jsonDocument = JsonDocument.getInstance();

    private String next;

    private String previous;

    private int total;

    private int offset;

    @Override
    public void createNewJson(String url, String authorizationHeader) throws AdvisorException {

        try {

            String json = getJson(url, authorizationHeader);

            validate(json);

        } catch (Exception e) {

            throw new AdvisorException(e.getMessage());

        }

    }

    @Override
    public <T> List<T> getEntries(String pagingObject, Function<JsonElement, T> converter) {

        jsonDocument.setJsonObject(pagingObject);

        previous = jsonDocument.getNullableStringValue("previous");

        next = jsonDocument.getNullableStringValue("next");

        total = jsonDocument.getIntValue("total");

        offset = jsonDocument.getIntValue("offset");

        JsonArray items = jsonDocument.getJsonArray("items");

        return serializeItems(items, converter);

    }

    @Override
    public String getNext() {

        return next;

    }

    @Override
    public String getPrevious() {

        return previous;

    }

    @Override
    public int getTotal() {

        return total;

    }

    @Override
    public int getOffset() {

        return offset;

    }

    private String getJson(String url, String authorizationHeader) throws AdvisorException {

        try {

            return client.get(url, authorizationHeader);

        } catch (Exception e) {

            throw new AdvisorException(e.getMessage());

        }

    }

    private void validate(String json) throws AdvisorException {

        jsonDocument.buildDOM(json);

        if (jsonDocument.hasMember("error")) {

            throw new AdvisorException(jsonDocument.setJsonObject("error")
                                                   .getStringValue("message"));

        }

    }

    private <T> List<T> serializeItems(JsonArray items, Function<JsonElement, T> converter) {

        return StreamSupport.stream(items.spliterator(), false)
                            .map(converter)
                            .collect(Collectors.toList());

    }

}
