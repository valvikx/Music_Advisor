package by.valvik.musicadvisor.command.impl;

import by.valvik.musicadvisor.constant.UserCommand;
import by.valvik.musicadvisor.context.annotation.Inject;
import by.valvik.musicadvisor.context.annotation.Singleton;
import by.valvik.musicadvisor.domain.playlist.PlayList;
import by.valvik.musicadvisor.service.SpotifyService;
import by.valvik.musicadvisor.view.View;

import static by.valvik.musicadvisor.constant.UserCommand.PLAYLISTS;

@Singleton(qualifier = UserCommand.Qualifier.PLAYLISTS)
public class CategoryPlaylistsCommand extends ApiCommand<PlayList> {

    @Inject
    public CategoryPlaylistsCommand(SpotifyService service, View console) {

        super(service, console, PLAYLISTS, PlayList.class);

    }

}
