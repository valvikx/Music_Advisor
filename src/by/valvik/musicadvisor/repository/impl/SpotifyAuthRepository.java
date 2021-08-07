package by.valvik.musicadvisor.repository.impl;

import by.valvik.musicadvisor.context.annotation.Inject;
import by.valvik.musicadvisor.context.annotation.Singleton;
import by.valvik.musicadvisor.domain.error.AuthError;
import by.valvik.musicadvisor.domain.auth.Token;
import by.valvik.musicadvisor.exception.ConfigurationException;
import by.valvik.musicadvisor.exception.RepositoryException;
import by.valvik.musicadvisor.exception.UtilException;
import by.valvik.musicadvisor.http.AppHttpClient;
import by.valvik.musicadvisor.json.JsonHandler;
import by.valvik.musicadvisor.json.JsonMapper;
import by.valvik.musicadvisor.repository.AuthRepository;

import static by.valvik.musicadvisor.constant.AppConstant.ERROR;

@Singleton
public class SpotifyAuthRepository implements AuthRepository {

    @Inject
    private AppHttpClient client;

    @Inject
    private JsonHandler jsonHandler;

    @Inject
    private JsonMapper jsonMapper;

    @Override
    public Token getToken(String url, String body) throws RepositoryException {

        String json;

        try {

            json = client.performPost(url, body);

        } catch (Exception e) {

            throw new ConfigurationException(e);

        }

        validate(json);

        return jsonMapper.toEntity(json, Token.class);

    }

    private void validate(String json) throws RepositoryException {

        try {

            if (jsonHandler.buildDOM(json).hasMember(ERROR)) {

                AuthError authError = jsonMapper.toEntity(json, AuthError.class);

                throw new RepositoryException(authError.getErrorDescription());

            }

        } catch (UtilException e) {

            throw new RepositoryException(e.getMessage());

        }

    }

}
