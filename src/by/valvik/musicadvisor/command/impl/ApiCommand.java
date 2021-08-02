package by.valvik.musicadvisor.command.impl;

import by.valvik.musicadvisor.command.Command;
import by.valvik.musicadvisor.constant.Status;
import by.valvik.musicadvisor.constant.UserCommand;
import by.valvik.musicadvisor.domain.Item;
import by.valvik.musicadvisor.dto.BrowseItem;
import by.valvik.musicadvisor.exception.ServiceException;
import by.valvik.musicadvisor.service.SpotifyService;
import by.valvik.musicadvisor.view.View;

import static by.valvik.musicadvisor.constant.Status.BAD_REQUEST;
import static by.valvik.musicadvisor.constant.Status.OK;

public abstract class ApiCommand<T extends Item> implements Command {

    private final SpotifyService service;

    private final View console;

    private final UserCommand userCommand;

    private final Class<T> entityClass;

    public ApiCommand(SpotifyService service, View console,
                      UserCommand userCommand, Class<T> entityClass) {

        this.service = service;

        this.console = console;

        this.userCommand = userCommand;

        this.entityClass = entityClass;

    }
    
    @Override
    public Status execute() {

        Status status = OK;

        try {

            BrowseItem browseItem = service.getBrowseItem(userCommand, entityClass);

            console.display(browseItem.items(), browseItem.currentPage(), browseItem.totalPages());

        } catch (ServiceException e) {

            status = BAD_REQUEST;

            console.display(e.getMessage());

        }

        return status;

    }

}
