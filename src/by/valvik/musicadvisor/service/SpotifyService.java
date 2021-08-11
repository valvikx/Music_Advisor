package by.valvik.musicadvisor.service;

import by.valvik.musicadvisor.constant.UserCommand;
import by.valvik.musicadvisor.domain.Item;
import by.valvik.musicadvisor.dto.ItemDto;
import by.valvik.musicadvisor.exception.ServiceException;

public interface SpotifyService {

    <T extends Item> ItemDto getItem(UserCommand command, Class<T> entityClass) throws ServiceException;

}
