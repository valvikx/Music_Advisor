package advisor.http;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;

import static advisor.view.Messages.*;

public class Server {

    private static final int PORT = 8080;

    private static final int STATUS_OK = 200;

    private HttpServer server;

    private String query;

    public void start() throws IOException {

        server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/", httpExchange -> {

                                URI uri = httpExchange.getRequestURI();

                                query = uri.getQuery();

                                String response = query == null ||
                                                  !query.contains(CODE) ? CODE_NOT_FOUND : GOT_THE_CODE;

                                httpExchange.sendResponseHeaders(STATUS_OK, response.length());

                                httpExchange.getResponseBody().write(response.getBytes());

                                httpExchange.getResponseBody().close();

        });

        server.setExecutor(null);

        server.start();

    }

    public void stop() {

        server.stop(10);

    }

    public String getQuery() {

        return query;

    }

}
