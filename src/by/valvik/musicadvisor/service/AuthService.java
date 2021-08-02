package by.valvik.musicadvisor.service;

import by.valvik.musicadvisor.exception.ServiceException;

public interface AuthService {

    String getAuthHeader(String authCode) throws ServiceException;

}
