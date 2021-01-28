package advisor.command.impl;

import advisor.command.Command;
import advisor.domain.Playlist;
import advisor.exception.AdvisorException;
import advisor.model.Model;
import advisor.repository.impl.AdvisorRepository;

import java.util.List;

public class FeaturedPlaylistsCommand extends CommandImpl {

    public FeaturedPlaylistsCommand(AdvisorRepository advisorRepository) {

        super(advisorRepository);

    }

    @Override
    public void execute(Model model, Command pagingCommand) throws AdvisorException {

        String pagingUrl = getPagingUrl(model, pagingCommand);

        List<Playlist> featuredPlaylists =
                repository.getFeaturedPlaylists(pagingUrl,
                                                getLimit(model),
                                                getAuthorizationHeader(model));

        model.setEntries(featuredPlaylists);

        setPageParams(model);

        model.setExecute(true);

    }

}
