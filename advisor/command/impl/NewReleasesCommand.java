package advisor.command.impl;

import advisor.command.Command;
import advisor.domain.Album;
import advisor.exception.AdvisorException;
import advisor.model.Model;
import advisor.repository.impl.AdvisorRepository;

import java.util.List;

public class NewReleasesCommand extends CommandImpl {

    public NewReleasesCommand(AdvisorRepository advisorRepository) {

        super(advisorRepository);

    }

    @Override
    public void execute(Model model, Command pagingCommand) throws AdvisorException {

        String pagingUrl = getPagingUrl(model, pagingCommand);

        List<Album> newReleases = repository.getNewReleases(pagingUrl,
                                                            getLimit(model),
                                                            getAuthorizationHeader(model));

        model.setEntries(newReleases);

        setPageParams(model);

        model.setExecute(true);

    }

}
