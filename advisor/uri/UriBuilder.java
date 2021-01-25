package advisor.uri;

public class UriBuilder {

    private static final String ACCOUNTS_HOST = "https://accounts.spotify.com";

    private static final String API_HOST = "https://api.spotify.com";

    // should be replaced with an actual one
    private static final String CLIENT_ID = "8be3d324da104f468738b01ee36f4e12";

    // should be replaced with an actual one
    private static final String CLIENT_SECRET = "84e048bbbb3b497d84b96bb132f53607";

    private static final String REDIRECT_URI = "http://localhost:8080";

    private final StringBuilder uri;

    public UriBuilder() {

        this.uri = new StringBuilder();

    }

    public static String getUrlToAuthorizationCode() {

        return new UriBuilder()
                        .addHost(ACCOUNTS_HOST)
                        .addPath("authorize")
                        .query()
                        .addQueryParameter("client_id", CLIENT_ID)
                        .addQueryParameter("redirect_uri", REDIRECT_URI)
                        .addQueryParameter("response_type", "code")
                        .toUrl();

    }

    public static String getUrlToAccessTokens() {

        return new UriBuilder()
                            .addHost(ACCOUNTS_HOST)
                            .addPath("api")
                            .addPath("token")
                            .toUrl();

    }

    public static String getRequestToAccessTokens(String authorizationCode) {

        return new UriBuilder()
                        .addQueryParameter("grant_type", "authorization_code")
                        .addQueryParameter("code", authorizationCode)
                        .addQueryParameter("redirect_uri", REDIRECT_URI)
                        .addQueryParameter("client_id", CLIENT_ID)
                        .addQueryParameter("client_secret", CLIENT_SECRET)
                        .toUrl();

    }

    public static String getUrlTo(String resource, int limit) {

        return buildBrowseTabUri(resource)
                                    .query()
                                    .addQueryParameter("limit", Integer.toString(limit))
                                    .toUrl();

    }

    public static String getUrlToPlaylists(String resource, String id, int limit) {

        return buildBrowseTabUri(resource)
                                        .addPath(id)
                                        .addPath("playlists")
                                        .query()
                                        .addQueryParameter("limit", Integer.toString(limit))
                                        .toUrl();

    }

    private static UriBuilder buildBrowseTabUri(String path) {

        return new UriBuilder()
                        .addHost(API_HOST)
                        .addPath("v1")
                        .addPath("browse")
                        .addPath(path);


    }

    private UriBuilder addHost(String host) {

        uri.append(host);

        return this;

    }

    private UriBuilder addPath(String path) {

        uri.append("/");

        uri.append(path);

        return this;

    }

    private UriBuilder query() {
        
        uri.append("?");

        return this;

    }

    private UriBuilder addQueryParameter(String key, String value) {

        if (uri.toString().length() > 0) {

            uri.append("&");
            
        }

        uri.append(key);

        uri.append("=");

        uri.append(value);

        return this;

    }

    private String toUrl() {

        return uri.toString();

    }

}
