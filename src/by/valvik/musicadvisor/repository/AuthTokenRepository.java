package by.valvik.musicadvisor.repository;

import by.valvik.musicadvisor.domain.auth.Token;
import by.valvik.musicadvisor.exception.RepositoryException;

public interface AuthTokenRepository {

    Token getToken(String url, String body) throws RepositoryException;

}
