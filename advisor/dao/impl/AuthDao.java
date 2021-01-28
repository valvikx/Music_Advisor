package advisor.dao.impl;

import advisor.dao.IAuthDao;
import advisor.exception.AdvisorException;
import advisor.http.Client;
import advisor.json.JsonDocument;

import java.util.Map;

public class AuthDao implements IAuthDao {

    private final Client client = Client.getInstance();

    private final JsonDocument jsonDocument = JsonDocument.getInstance();

    @Override
    public Map<String, String> getParams(String url, String query) throws AdvisorException {

        try {

            String json = getJson(url, query);

            validate(json);

            return Map.of("accessToken", jsonDocument.getStringValue("access_token"),
                          "tokenType", jsonDocument.getStringValue("token_type"));

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

        jsonDocument.buildDOM(json);

        if (jsonDocument.hasMember("error")) {

            throw new AdvisorException(jsonDocument.getStringValue("error_description"));

        }

    }

}
