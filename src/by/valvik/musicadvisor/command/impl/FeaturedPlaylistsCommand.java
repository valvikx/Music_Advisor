package by.valvik.musicadvisor.command.impl;

import by.valvik.musicadvisor.annotation.Inject;
import by.valvik.musicadvisor.annotation.Singleton;
import by.valvik.musicadvisor.domain.playlist.PlayList;
import by.valvik.musicadvisor.service.SpotifyService;
import by.valvik.musicadvisor.view.View;

import static by.valvik.musicadvisor.constant.AppConstant.QUALIFIER_FEATURED_PLAYLISTS;
import static by.valvik.musicadvisor.constant.UserCommand.FEATURED;

@Singleton(qualifier = QUALIFIER_FEATURED_PLAYLISTS)
public class FeaturedPlaylistsCommand extends ApiCommand<PlayList> {

    @Inject
    public FeaturedPlaylistsCommand(SpotifyService service, View console) {

        super(service, console, FEATURED, PlayList.class);

    }

}
