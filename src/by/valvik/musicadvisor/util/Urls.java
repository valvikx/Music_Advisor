package by.valvik.musicadvisor.util;

import static by.valvik.musicadvisor.util.Props.getValue;

public final class Urls {

    private static final String KEY_AUTHORIZE_URL = "authorize_url";

    private static final String KEY_TOKEN_URL = "token_url";

    private static final String KEY_BROWSE_API_URL = "browse_api_url";

    private static final String KEY_CLIENT_ID = "client_id";

    private static final String KEY_REDIRECT_URI = "redirect_uri";

    public static final String KEY_TOKEN_BODY = "token_body";

    private static final String KEY_CLIENT_SECRET = "client_secret";

    private Urls() {

    }

    public static String getUrlToAuthorize() {

        return getValue(KEY_AUTHORIZE_URL).formatted(getValue(KEY_CLIENT_ID), getValue(KEY_REDIRECT_URI));

    }

    public static String getUrlToToken() {

        return getValue(KEY_TOKEN_URL);

    }

    public static String getBodyToAccessToken(String authCode) {

        return getValue(KEY_TOKEN_BODY).formatted(authCode,
                                                  getValue(KEY_REDIRECT_URI),
                                                  getValue(KEY_CLIENT_ID),
                                                  getValue(KEY_CLIENT_SECRET));

    }

    public static String getUrlTo(String resource, int limit) {

        return getValue(KEY_BROWSE_API_URL).formatted(resource, limit);

    }

}
