package by.valvik.musicadvisor.repository;

import by.valvik.musicadvisor.domain.Item;
import by.valvik.musicadvisor.domain.Response;
import by.valvik.musicadvisor.exception.RepositoryException;

import java.lang.reflect.Type;

public interface ResponseRepository {

    <T extends Item> Response<T> getResponse(String url, String itemsName, Type typeToken) throws RepositoryException;

}
