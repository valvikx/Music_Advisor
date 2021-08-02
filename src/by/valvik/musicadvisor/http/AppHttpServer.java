package by.valvik.musicadvisor.http;

import by.valvik.musicadvisor.exception.ConfigurationException;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static by.valvik.musicadvisor.constant.AppConstant.KEY_CODE;
import static by.valvik.musicadvisor.constant.Status.OK;
import static by.valvik.musicadvisor.util.Props.getValue;
import static java.util.concurrent.TimeUnit.SECONDS;

public class AppHttpServer {

    private static final int PORT = 8080;

    private static final int BACKLOG = 0;

    private static final String PATH = "/";

    private static final String KEY_GOT_CODE = "got_code";

    private static final String KEY_CODE_NOT_FOUND = "code_not_found";

    private static final int DELAY = 0;

    private static final int COUNT = 1;

    private HttpServer server;

    private ExecutorService executor;

    private CountDownLatch countDownLatch;

    private String query;

    public void start() throws IOException {

        try {

            server = HttpServer.create(new InetSocketAddress(PORT), BACKLOG);

            executor = Executors.newSingleThreadExecutor();

            countDownLatch = new CountDownLatch(COUNT);

            server.createContext(PATH, httpExchange -> {

                query = httpExchange.getRequestURI().getQuery();

                String response = query == null || !query.contains(KEY_CODE) ? getValue(KEY_CODE_NOT_FOUND)
                        : getValue(KEY_GOT_CODE);

                httpExchange.sendResponseHeaders(OK.getCode(), response.length());

                httpExchange.getResponseBody().write(response.getBytes());

                httpExchange.getResponseBody().close();

                countDownLatch.countDown();

            });

            server.setExecutor(executor);

            server.start();

        } catch (IOException e) {

            throw new ConfigurationException(e);

        }

    }

    public void stop(){

        try {

            countDownLatch.await();

            executor.shutdown();

            if (!executor.awaitTermination(1L, SECONDS)) {

                executor.shutdown();

            }

            server.stop(DELAY);

        } catch (InterruptedException e) {

            throw new ConfigurationException(e);

        }

    }

    public String getQuery() {

        return query;

    }

}
