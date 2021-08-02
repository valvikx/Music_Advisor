package by.valvik.musicadvisor.repository;

import by.valvik.musicadvisor.domain.Item;
import by.valvik.musicadvisor.domain.Items;
import by.valvik.musicadvisor.exception.RepositoryException;

import java.lang.reflect.Type;

public interface SpotifyRepository {

    <T extends Item> Items<T> getItems(String url, String itemsName, Type typeToken) throws RepositoryException;

}
