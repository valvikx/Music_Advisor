package by.valvik.musicadvisor.service;

import by.valvik.musicadvisor.constant.UserCommand;
import by.valvik.musicadvisor.domain.Item;
import by.valvik.musicadvisor.dto.BrowseItem;
import by.valvik.musicadvisor.exception.ServiceException;

public interface SpotifyService {

    <T extends Item> BrowseItem getBrowseItem(UserCommand command, Class<T> entityClass) throws ServiceException;

}
