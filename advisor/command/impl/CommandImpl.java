package advisor.command.impl;

import advisor.command.Command;
import advisor.command.ICommand;
import advisor.exception.AdvisorException;
import advisor.model.Model;
import advisor.repository.impl.Repository;

import static advisor.constant.Messages.NO_MORE_PAGES;

public abstract class CommandImpl implements ICommand {

    protected final Repository repository;

    public CommandImpl(Repository repository) {

        this.repository = repository;

    }

    protected String getAuthorizationHeader(Model model) {

        return model.getAttribute("authorizationHeader");

    }

    protected int getLimit(Model model) {

        return model.getLimit();

    }

    protected String getPagingUrl(Model model, Command pagingCommand) throws AdvisorException {

        String pagingUrl = pagingCommand.equals(Command.NEXT)
                                            ? model.getAttribute("nextUrl")
                                            : model.getAttribute("prevUrl");

        if (model.isExecute() && pagingUrl == null) {

            throw new AdvisorException(NO_MORE_PAGES);

        }

        return pagingUrl;

    }

    protected void setPagingUrls(Model model) {

        model.addAttribute("prevUrl", repository.getPrevUrl());

        model.addAttribute("nextUrl", repository.getNextUrl());
    }

    protected void setPageParameters(Model model) {

        int currentPage = repository.getOffset() / model.getLimit() + 1;

        int totalPages = (int) Math.ceil((double) repository.getTotal() / model.getLimit());

        model.addAttribute("currentPage", Integer.toString(currentPage));

        model.addAttribute("totalPages", Integer.toString(totalPages));

    }

}
