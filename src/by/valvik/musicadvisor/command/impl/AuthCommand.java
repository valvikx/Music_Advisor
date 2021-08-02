package by.valvik.musicadvisor.command.impl;

import by.valvik.musicadvisor.annotation.Inject;
import by.valvik.musicadvisor.annotation.Singleton;
import by.valvik.musicadvisor.command.Command;
import by.valvik.musicadvisor.constant.Status;
import by.valvik.musicadvisor.exception.ServiceException;
import by.valvik.musicadvisor.holder.ContextHolder;
import by.valvik.musicadvisor.http.AppHttpServer;
import by.valvik.musicadvisor.service.AuthService;
import by.valvik.musicadvisor.view.View;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static by.valvik.musicadvisor.constant.AppConstant.*;
import static by.valvik.musicadvisor.constant.Status.BAD_REQUEST;
import static by.valvik.musicadvisor.constant.Status.OK;
import static by.valvik.musicadvisor.util.Parsers.splitQuery;
import static by.valvik.musicadvisor.util.Props.getValue;
import static by.valvik.musicadvisor.util.Urls.getUrlToAuthorize;
import static java.util.Objects.nonNull;

@Singleton(qualifier = QUALIFIER_AUTH_COMMAND)
public class AuthCommand implements Command {

    private static final String KEY_ALREADY_LOGGED_IN = "already_logged_in";

    private static final String KEY_SUCCESS = "success";

    private static final String KEY_USE_LINK = "use_link";

    private static final String KEY_ACCESS_DENIED = "access_denied";

    private static final String KEY_CODE_RECEIVED = "code_received";

    private static final String KEY_MAKING_HTTP_REQUEST = "making_http_request";

    @Inject
    private View console;

    @Inject
    private ContextHolder contextHolder;

    @Inject
    private AppHttpServer server;

    @Inject
    private AuthService authService;

    @Override
    public Status execute() {

        if (nonNull(contextHolder.getAuthHeader())) {

            console.display(getValue(KEY_ALREADY_LOGGED_IN));

            return BAD_REQUEST;

        }

        Status status = OK;

        try {

            server.start();

            String url = getUrlToAuthorize();

            console.display(getValue(KEY_USE_LINK), url);

            server.stop();

            Map<String, List<String>> splintedQuery = splitQuery(server.getQuery());

            if (splintedQuery.isEmpty() || splintedQuery.containsKey(ERROR)) {

                console.display(getValue(KEY_ACCESS_DENIED));

                return BAD_REQUEST;

            }

            String authCode = splintedQuery.get(KEY_CODE).get(0);

            console.display(getValue(KEY_CODE_RECEIVED));

            console.display(getValue(KEY_MAKING_HTTP_REQUEST));

            String authHeader = authService.getAuthHeader(authCode);

            contextHolder.setAuthHeader(authHeader);

            console.display(getValue(KEY_SUCCESS));

        } catch (IOException | ServiceException e) {

            status = BAD_REQUEST;

            console.display(e.getMessage());

        }

        return status;

    }

}
