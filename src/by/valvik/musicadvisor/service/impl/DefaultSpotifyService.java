package by.valvik.musicadvisor.service.impl;

import by.valvik.musicadvisor.constant.Direction;
import by.valvik.musicadvisor.constant.UserCommand;
import by.valvik.musicadvisor.context.annotation.Inject;
import by.valvik.musicadvisor.context.annotation.Singleton;
import by.valvik.musicadvisor.context.holder.ContextHolder;
import by.valvik.musicadvisor.domain.Item;
import by.valvik.musicadvisor.domain.Response;
import by.valvik.musicadvisor.dto.ItemDto;
import by.valvik.musicadvisor.exception.RepositoryException;
import by.valvik.musicadvisor.exception.ServiceException;
import by.valvik.musicadvisor.factory.Factory;
import by.valvik.musicadvisor.repository.ResponseRepository;
import by.valvik.musicadvisor.service.SpotifyService;
import by.valvik.musicadvisor.tuple.Tuple;

import java.lang.reflect.Type;
import java.util.Objects;

import static by.valvik.musicadvisor.constant.AppConstant.KEY_COMMAND_CANNOT_BE_EXECUTED;
import static by.valvik.musicadvisor.constant.Direction.NEXT;
import static by.valvik.musicadvisor.constant.Direction.PREV;
import static by.valvik.musicadvisor.constant.UserCommand.PLAYLISTS;
import static by.valvik.musicadvisor.util.Props.getValue;
import static by.valvik.musicadvisor.util.Urls.getUrlTo;
import static java.lang.Math.floorMod;
import static java.util.Objects.nonNull;

@Singleton
public class DefaultSpotifyService implements SpotifyService {

    private static final String QUALIFIER_TYPE_TOKEN_FACTORY = "typeTokenFactory";

    private static final String QUALIFIER_RESOURCE_FACTORY = "resourceFactory";

    @Inject
    private ResponseRepository repository;

    @Inject
    private ContextHolder contextHolder;

    @Inject(qualifier = QUALIFIER_TYPE_TOKEN_FACTORY)
    private Factory<Class<? extends Item>, Type> typeTokenFactory;

    @Inject(qualifier = QUALIFIER_RESOURCE_FACTORY)
    private Factory<UserCommand, Tuple<String, String>> resourceFactory;

    @Override
    public <T extends Item> ItemDto getItem(UserCommand command, Class<T> entityClass) throws ServiceException {

        Tuple<String, String> resources = resourceFactory.get(command);

        Type typeToken = typeTokenFactory.get(entityClass);

        String url = getActualUrl(resources.first());

        try {

            Response<T> response = repository.getResponse(url, resources.second(), typeToken);

            contextHolder.setResponse(response);

            return toItemDto(response);

        } catch (RepositoryException e) {

            throw new ServiceException(e.getMessage());

        }

    }

    private String getActualUrl(String resource) throws ServiceException {

        Direction direction = contextHolder.getArgsHolder().getDirection();

        if (nonNull(direction)) {

            Response<? extends Item> response = contextHolder.getResponse();

            if (Objects.equals(direction, NEXT) ) {

                if (nonNull(response) && nonNull(response.getNext())) {

                    return response.getNext();

                } else {

                    throw new ServiceException(getValue(KEY_COMMAND_CANNOT_BE_EXECUTED));

                }

            }

            if (Objects.equals(direction, PREV)) {

                if (nonNull(response) && nonNull(response.getPrevious())) {

                    return response.getPrevious();

                } else {

                    throw new ServiceException(getValue(KEY_COMMAND_CANNOT_BE_EXECUTED));

                }

            }

        }

        UserCommand command = contextHolder.getArgsHolder().getUserCommand();

        int limit = contextHolder.getArgsHolder().getLimit();

        String categoryName = contextHolder.getArgsHolder().getCategoryId();

        return Objects.equals(command, PLAYLISTS) ? getUrlTo(resource.formatted(categoryName), limit)
                                                  : getUrlTo(resource, limit);

    }

    private <T extends Item> ItemDto toItemDto(Response<T> response) {

        int limit = contextHolder.getResponse().getLimit();

        int offset = contextHolder.getResponse().getOffset();

        int total = contextHolder.getResponse().getTotal();

        int totalPages;

        if (total < limit || total == limit) {

            totalPages = 1;

        } else {

            int mod = floorMod(total, limit);

            totalPages = mod == 0 ? total / limit : total / limit + 1;

        }

        int currentPage = offset / limit + 1;

        return new ItemDto(response.getItems(), totalPages, currentPage);

    }

}
