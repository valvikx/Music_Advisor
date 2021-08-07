package by.valvik.musicadvisor.http;

import by.valvik.musicadvisor.context.annotation.Singleton;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import static by.valvik.musicadvisor.constant.AppConstant.AUTHORIZATION;

@Singleton
public class AppHttpClient {

    private static final String CONTENT_TYPE = "content-type";

    private static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

    private final HttpClient client;

    public AppHttpClient() {

        this.client = HttpClient.newBuilder().build();

    }

    public String performGet(String uri, String authorizationHeader) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                                         .header(AUTHORIZATION, authorizationHeader)
                                         .uri(URI.create(uri))
                                         .GET()
                                         .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        return response.body();

    }

    public String performPost(String uri, String body) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                                         .header(CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED)
                                         .uri(URI.create(uri))
                                         .POST(BodyPublishers.ofString(body))
                                         .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        return response.body();

    }

}
