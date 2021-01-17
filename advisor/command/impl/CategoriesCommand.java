package advisor.command.impl;

import advisor.command.Command;
import advisor.domain.Category;
import advisor.exception.AdvisorException;
import advisor.model.Model;
import advisor.repository.impl.Repository;

import java.util.List;

public class CategoriesCommand extends CommandImpl {

    public CategoriesCommand(Repository repository) {

        super(repository);

    }

    @Override
    public void execute(Model model, Command pagingCommand) throws AdvisorException {

        String pagingUrl = getPagingUrl(model, pagingCommand);

        List<Category> categories = repository.getCategories(pagingUrl,
                                                             getLimit(model),
                                                             getAuthorizationHeader(model));

        model.setEntries(categories);

        setPagingUrls(model);

        setPageParameters(model);

        model.setExecute(true);

    }

}
