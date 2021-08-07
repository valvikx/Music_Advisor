package by.valvik.musicadvisor.command.impl;

import by.valvik.musicadvisor.constant.UserCommand;
import by.valvik.musicadvisor.context.annotation.Inject;
import by.valvik.musicadvisor.context.annotation.Singleton;
import by.valvik.musicadvisor.domain.playlist.PlayList;
import by.valvik.musicadvisor.service.SpotifyService;
import by.valvik.musicadvisor.view.View;

import static by.valvik.musicadvisor.constant.UserCommand.FEATURED;

@Singleton(qualifier = UserCommand.Qualifier.FEATURED)
public class FeaturedPlaylistsCommand extends ApiCommand<PlayList> {

    @Inject
    public FeaturedPlaylistsCommand(SpotifyService service, View console) {

        super(service, console, FEATURED, PlayList.class);

    }

}
