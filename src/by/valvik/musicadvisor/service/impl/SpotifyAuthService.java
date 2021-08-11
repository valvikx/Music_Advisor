package by.valvik.musicadvisor.service.impl;

import by.valvik.musicadvisor.context.annotation.Inject;
import by.valvik.musicadvisor.context.annotation.Singleton;
import by.valvik.musicadvisor.domain.auth.Token;
import by.valvik.musicadvisor.exception.RepositoryException;
import by.valvik.musicadvisor.exception.ServiceException;
import by.valvik.musicadvisor.repository.AuthTokenRepository;
import by.valvik.musicadvisor.service.AuthService;

import static by.valvik.musicadvisor.constant.Delimiter.SPACE;
import static by.valvik.musicadvisor.util.Urls.getBodyToAccessToken;
import static by.valvik.musicadvisor.util.Urls.getUrlToToken;

@Singleton
public class SpotifyAuthService implements AuthService {

    @Inject
    private AuthTokenRepository repository;

    @Override
    public String getAuthHeader(String authCode) throws ServiceException {

        String url = getUrlToToken();

        String body = getBodyToAccessToken(authCode);

        try {

            Token token = repository.getToken(url, body);

            return token.getTokenType() + SPACE.getSign() + token.getAccessToken();

        } catch (RepositoryException e) {

            throw new ServiceException(e.getMessage());

        }

    }

}
