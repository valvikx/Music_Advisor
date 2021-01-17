package advisor.dao.impl;

import advisor.dao.IEntryDao;
import advisor.exception.AdvisorException;
import advisor.http.Client;
import advisor.json.JsonHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EntryDao implements IEntryDao {

    private final Client client = Client.getInstance();

    private final JsonHelper jsonHelper = JsonHelper.getInstance();

    private String nextUrl;

    private String prevUrl;

    private int total;

    private int offset;

    public void buildJson(String url, String authorizationHeader) throws AdvisorException {

        try {

            String json = client.get(url, authorizationHeader);

            validate(json);

        } catch (Exception e) {

            throw new AdvisorException(e.getMessage());

        }

    }

    @Override
    public <T> List<T> getEntries(String pagingObject, Function<JsonElement, T> converter) {

        jsonHelper.setJsonObject(pagingObject);

        prevUrl = getValueOfPrev();

        nextUrl = getValueOfNext();

        total = getValueOfTotal();

        offset = getValueOfOffset();

        JsonArray items = jsonHelper.getJsonArray("items");

        return serializeItems(items, converter);


    }

    public String getNextUrl() {

        return nextUrl;

    }

    public String getPrevUrl() {

        return prevUrl;

    }

    public int getTotal() {

        return total;

    }

    public int getOffset() {

        return offset;

    }

    private void validate(String json) throws AdvisorException {

        jsonHelper.buildJson(json);

        if (jsonHelper.hasMember("error")) {

            throw new AdvisorException(jsonHelper
                                            .setJsonObject("error")
                                            .getStringValue("message"));

        }

    }

    private <T> List<T> serializeItems(JsonArray items, Function<JsonElement, T> converter) {

        return StreamSupport
                        .stream(items.spliterator(), false)
                        .map(converter)
                        .collect(Collectors.toList());


    }

    private String getValueOfNext() {

        return jsonHelper.isMemberNull("next")
                                        ? null
                                        : jsonHelper.getStringValue("next");

    }

    private String getValueOfPrev() {

        return jsonHelper.isMemberNull("previous")
                            ? null
                            : jsonHelper.getStringValue("previous");

    }

    private int getValueOfTotal() {

        return jsonHelper.getIntValue("total");

    }

    private int getValueOfOffset() {

        return jsonHelper.getIntValue("offset");

    }

}
