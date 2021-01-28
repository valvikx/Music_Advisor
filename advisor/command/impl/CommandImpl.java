package advisor.command.impl;

import advisor.command.Command;
import advisor.command.ICommand;
import advisor.exception.AdvisorException;
import advisor.model.Model;
import advisor.repository.IRepository;
import advisor.repository.impl.AdvisorRepository;

public abstract class CommandImpl implements ICommand {

    protected final IRepository repository;

    public CommandImpl(AdvisorRepository advisorRepository) {

        this.repository = advisorRepository;

    }

    protected String getAuthorizationHeader(Model model) {

        return model.getAttribute("authorizationHeader");

    }

    protected int getLimit(Model model) {

        return model.getLimit();

    }

    protected String getPagingUrl(Model model,
                                  Command pagingCommand) throws AdvisorException {

        String pagingUrl = pagingCommand.equals(Command.NEXT) ? model.getAttribute("next")
                                                              : model.getAttribute("previous");

        if (model.isExecute() && pagingUrl == null) {

            throw new AdvisorException("No more pages.");

        }

        return pagingUrl;

    }

    protected void setPageParams(Model model) {

        model.addAttribute("previous", repository.getPrevious());

        model.addAttribute("next", repository.getNext());

        int currentPage = repository.getOffset() / model.getLimit() + 1;

        int totalPages = (int) Math.ceil((double) repository.getTotal() / model.getLimit());

        model.addAttribute("currentPage", Integer.toString(currentPage));

        model.addAttribute("totalPages", Integer.toString(totalPages));

    }

}
