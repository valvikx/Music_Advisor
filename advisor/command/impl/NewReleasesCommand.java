package advisor.command.impl;

import advisor.command.Command;
import advisor.domain.Album;
import advisor.exception.AdvisorException;
import advisor.model.Model;
import advisor.repository.impl.Repository;

import java.util.List;

public class NewReleasesCommand extends CommandImpl {

    public NewReleasesCommand(Repository repository) {

        super(repository);

    }

    @Override
    public void execute(Model model, Command pagingCommand) throws AdvisorException {

        String pagingUrl = getPagingUrl(model, pagingCommand);

        List<Album> newReleases = repository.getNewReleases(pagingUrl,
                                                            getLimit(model),
                                                            getAuthorizationHeader(model));

        model.setEntries(newReleases);

        setPagingUrls(model);

        setPageParameters(model);

        model.setExecute(true);

    }

}
