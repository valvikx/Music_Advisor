package advisor.command.impl;

import advisor.command.Command;
import advisor.domain.Playlist;
import advisor.exception.AdvisorException;
import advisor.model.Model;
import advisor.repository.impl.Repository;

import java.util.List;

public class PlaylistsCommand extends CommandImpl {

    public PlaylistsCommand(Repository repository) {

        super(repository);

    }

    @Override
    public void execute(Model model, Command pagingCommand) throws AdvisorException {

        String pagingUrl = getPagingUrl(model, pagingCommand);

        String categoryName = model.getAttribute("categoryName");

        List<Playlist> categoryPlaylists =
                        repository.getCategoryPlaylists(pagingUrl,
                                                        getLimit(model),
                                                        getAuthorizationHeader(model),
                                                        categoryName);

        model.setEntries(categoryPlaylists);

        setPagingUrls(model);

        setPageParameters(model);

        model.setExecute(true);

    }

}
