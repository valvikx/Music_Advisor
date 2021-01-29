package advisor.command.impl;

import advisor.command.Command;
import advisor.domain.Playlist;
import advisor.exception.AdvisorException;
import advisor.model.Model;
import advisor.repository.impl.AdvisorRepository;

import java.util.List;

public class PlaylistsCommand extends AbstractCommand {

    public PlaylistsCommand(AdvisorRepository advisorRepository) {

        super(advisorRepository);

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

        setPageParams(model);

        model.setExecute(true);

    }

}
