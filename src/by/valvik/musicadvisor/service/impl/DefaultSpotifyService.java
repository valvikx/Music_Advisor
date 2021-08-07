package by.valvik.musicadvisor.service.impl;

import by.valvik.musicadvisor.context.annotation.Inject;
import by.valvik.musicadvisor.context.annotation.Singleton;
import by.valvik.musicadvisor.constant.Direction;
import by.valvik.musicadvisor.constant.UserCommand;
import by.valvik.musicadvisor.domain.Item;
import by.valvik.musicadvisor.domain.Items;
import by.valvik.musicadvisor.dto.BrowseItem;
import by.valvik.musicadvisor.exception.RepositoryException;
import by.valvik.musicadvisor.exception.ServiceException;
import by.valvik.musicadvisor.factory.Factory;
import by.valvik.musicadvisor.context.holder.ContextHolder;
import by.valvik.musicadvisor.repository.SpotifyRepository;
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
import static java.lang.String.format;
import static java.util.Objects.nonNull;

@Singleton
public class DefaultSpotifyService implements SpotifyService {

    private static final String QUALIFIER_TYPE_TOKEN_FACTORY = "typeTokenFactory";

    private static final String QUALIFIER_SOURCE_FACTORY = "sourceFactory";

    @Inject
    private SpotifyRepository repository;

    @Inject
    private ContextHolder contextHolder;

    @Inject(qualifier = QUALIFIER_TYPE_TOKEN_FACTORY)
    private Factory<Class<? extends Item>, Type> typeTokenFactory;

    @Inject(qualifier = QUALIFIER_SOURCE_FACTORY)
    private Factory<UserCommand, Tuple<String, String>> sourceFactory;

    @Override
    public <T extends Item> BrowseItem getBrowseItem(UserCommand command, Class<T> entityClass) throws ServiceException {

        Tuple<String, String> resourceTuple = sourceFactory.get(command);

        Type typeToken = typeTokenFactory.get(entityClass);

        String url = getActualUrl(resourceTuple);

        try {

            Items<T> items = repository.getItems(url, resourceTuple.y(), typeToken);

            contextHolder.setItems(items);

            return toBrowseItem(items);

        } catch (RepositoryException e) {

            throw new ServiceException(e.getMessage());

        }

    }

    private String getActualUrl(Tuple<String, String> resourceTuple) throws ServiceException {

        Direction direction = contextHolder.getArgsHolder().getDirection();

        if (nonNull(direction)) {

            Items<? extends Item> items = contextHolder.getItems();

            if (Objects.equals(direction, NEXT) ) {

                if (nonNull(items) && nonNull(items.getNext())) {

                    return items.getNext();

                } else {

                    throw new ServiceException(getValue(KEY_COMMAND_CANNOT_BE_EXECUTED));

                }

            }

            if (Objects.equals(direction, PREV)) {

                if (nonNull(items) && nonNull(items.getPrevious())) {

                    return items.getPrevious();

                } else {

                    throw new ServiceException(getValue(KEY_COMMAND_CANNOT_BE_EXECUTED));

                }

            }

        }

        UserCommand command = contextHolder.getArgsHolder().getUserCommand();

        int limit = contextHolder.getArgsHolder().getLimit();

        String categoryName = contextHolder.getArgsHolder().getCategoryId();

        return Objects.equals(command, PLAYLISTS) ? getUrlTo(format(resourceTuple.x(), categoryName), limit)
                                                  : getUrlTo(resourceTuple.x(), limit);

    }

    private <T extends Item> BrowseItem toBrowseItem(Items<T> items) {

        int limit = contextHolder.getItems().getLimit();

        int offset = contextHolder.getItems().getOffset();

        int total = contextHolder.getItems().getTotal();

        int totalPages;

        if (total < limit || total == limit) {

            totalPages = 1;

        } else {

            int mod = floorMod(total, limit);

            totalPages = mod == 0 ? total / limit : total / limit + 1;

        }

        int currentPage = offset / limit + 1;


        return new BrowseItem(items.getItems(), totalPages, currentPage);

    }

}
