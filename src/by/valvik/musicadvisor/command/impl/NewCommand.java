package by.valvik.musicadvisor.command.impl;

import by.valvik.musicadvisor.annotation.Inject;
import by.valvik.musicadvisor.annotation.Singleton;
import by.valvik.musicadvisor.domain.album.Album;
import by.valvik.musicadvisor.service.SpotifyService;
import by.valvik.musicadvisor.view.View;

import static by.valvik.musicadvisor.constant.AppConstant.QUALIFIER_NEW_COMMAND;
import static by.valvik.musicadvisor.constant.UserCommand.NEW;

@Singleton(qualifier = QUALIFIER_NEW_COMMAND)
public class NewCommand extends ApiCommand<Album> {

    @Inject
    public NewCommand(SpotifyService service, View console) {

        super(service, console, NEW, Album.class);

    }

}
