package advisor.command.impl;

import advisor.command.Command;
import advisor.domain.Playlist;
import advisor.exception.AdvisorException;
import advisor.model.Model;
import advisor.repository.impl.Repository;

import java.util.List;

public class FeaturedPlaylistsCommand extends CommandImpl {

    public FeaturedPlaylistsCommand(Repository repository) {

        super(repository);

    }

    @Override
    public void execute(Model model, Command pagingCommand) throws AdvisorException {

        String pagingUrl = getPagingUrl(model, pagingCommand);

        List<Playlist> featuredPlaylists =
                repository.getFeaturedPlaylists(pagingUrl,
                                                getLimit(model),
                                                getAuthorizationHeader(model));

        model.setEntries(featuredPlaylists);

        setPagingUrls(model);

        setPageParameters(model);

        model.setExecute(true);

    }

}
