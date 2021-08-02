package by.valvik.musicadvisor.util;

import static by.valvik.musicadvisor.util.Props.getValue;
import static java.lang.String.format;

public final class Urls {

    private static final String KEY_AUTHORIZE_URL = "authorize_url";

    private static final String KEY_TOKEN_URL = "token_url";

    private static final String KEY_BROWSE_API_URL = "browse_api_url";

    private static final String KEY_CLIENT_ID = "client_id";

    private static final String KEY_REDIRECT_URI = "redirect_uri";

    public static final String KEY_TOKEN_BODY = "token_body";

    private static final String KEY_CLIENT_SECRET = "client_secret";

    public static final String PLAYLISTS_ENDPOINT = "categories/%s/playlists";

    private Urls() {

    }

    public static String getUrlToAuthorize() {

        return format(getValue(KEY_AUTHORIZE_URL),
                      getValue(KEY_CLIENT_ID),
                      getValue(KEY_REDIRECT_URI));

    }

    public static String getUrlToToken() {

        return getValue(KEY_TOKEN_URL);

    }

    public static String getBodyToAccessToken(String authCode) {

        return format(getValue(KEY_TOKEN_BODY),
                      authCode,
                      getValue(KEY_REDIRECT_URI),
                      getValue(KEY_CLIENT_ID),
                      getValue(KEY_CLIENT_SECRET));

    }

    public static String getUrlTo(String resource, int limit) {

        return format(getValue(KEY_BROWSE_API_URL), resource, limit);

    }

    public static String getUrlToPlaylists(String categoryId, int limit) {

        return format(getValue(KEY_BROWSE_API_URL), format(PLAYLISTS_ENDPOINT, categoryId), limit);

    }

}
