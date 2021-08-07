package by.valvik.musicadvisor.command.impl;

import by.valvik.musicadvisor.constant.UserCommand;
import by.valvik.musicadvisor.context.annotation.Inject;
import by.valvik.musicadvisor.context.annotation.Singleton;
import by.valvik.musicadvisor.domain.album.Album;
import by.valvik.musicadvisor.service.SpotifyService;
import by.valvik.musicadvisor.view.View;

import static by.valvik.musicadvisor.constant.UserCommand.NEW;

@Singleton(qualifier = UserCommand.Qualifier.NEW)
public class NewCommand extends ApiCommand<Album> {

    @Inject
    public NewCommand(SpotifyService service, View console) {

        super(service, console, NEW, Album.class);

    }

}
