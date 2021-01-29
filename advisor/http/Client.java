package advisor.http;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Client {

    private static final Client INSTANCE = new Client();

    private final HttpClient client = HttpClient.newBuilder()
                                                .build();

    private Client() {}

    public static Client getInstance() {

        return INSTANCE;

    }

    public String get(String uri, String headerValue) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                                         .header("Authorization", headerValue)
                                         .uri(URI.create(uri))
                                         .GET()
                                         .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();

    }

    public String post(String uri, String body) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                                         .header("Content-Type", "application/x-www-form-urlencoded")
                                         .uri(URI.create(uri))
                                         .POST(HttpRequest.BodyPublishers.ofString(body))
                                         .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();

    }

}
