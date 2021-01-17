package advisor.dao.impl;

import advisor.dao.IJsonDao;
import advisor.exception.AdvisorException;
import advisor.http.Client;
import advisor.json.JsonHelper;

import java.util.Map;

public class JsonDao implements IJsonDao {

    private final Client client = Client.getInstance();

    private final JsonHelper jsonHelper = JsonHelper.getInstance();

    @Override
    public Map<String, String> getMembers(String url, String query) throws AdvisorException {

        try {

            String json = getJson(url, query);

            validate(json);

            return Map.of("accessToken", jsonHelper.getStringValue("access_token"),
                          "tokenType", jsonHelper.getStringValue("token_type"));

        } catch (AdvisorException e) {

            throw new AdvisorException(e.getMessage());

        }

    }

    private String getJson(String url, String query) throws AdvisorException {

        try {

            return client.post(url, query);

        } catch (Exception e) {

            throw new AdvisorException(e.getMessage());

        }

    }

    private void validate(String json) throws AdvisorException {

        jsonHelper.buildJson(json);

        if (jsonHelper.hasMember("error")) {

            throw new AdvisorException(jsonHelper.getStringValue("error_description"));

        }

    }

}
