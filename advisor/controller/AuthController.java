package advisor.controller;

import advisor.exception.AdvisorException;
import advisor.http.Server;
import advisor.model.Model;
import advisor.repository.impl.AdvisorRepository;
import advisor.uri.UriBuilder;
import advisor.view.Console;

import static advisor.view.Messages.*;

public class AuthController {

    private final Server server = new Server();

    private final Console console = Console.getInstance();

    private final AdvisorRepository advisorRepository;

    public AuthController(AdvisorRepository advisorRepository) {

        this.advisorRepository = advisorRepository;

    }

    public void authorize(Model model) {

        try {

            server.start();

            String url = UriBuilder.getUrlToAuthorizationCode();

            console.displayMessage(USE_THIS_LINK, url);

            while (server.getQuery() == null) {

                Thread.sleep(10);

            }

            server.stop();

            if (server.getQuery() != null && server.getQuery().contains(CODE)) {

                console.displayMessage(CODE_RECEIVED);
                
            } else {

                throw new AdvisorException("Access denied");

            }

            console.displayMessage(MAKING_HTTP_REQUEST);

            String code = getCode(server.getQuery());

            model.addAttribute("authorizationHeader", advisorRepository.getAuthorizationHeader(code));

            model.setAuthorized(true);

            console.displayMessage(SUCCESS);

        } catch (Exception e) {

            console.displayMessage(e.getMessage());

        }

    }

    private String getCode(String query) {

        // code=NApCCg..BkWt
        String[] tokens = query.split("=");

        return tokens[1];

    }

}
