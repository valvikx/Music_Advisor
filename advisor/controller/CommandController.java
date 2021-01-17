package advisor.controller;

import advisor.command.Command;
import advisor.command.impl.*;
import advisor.exception.AdvisorException;
import advisor.model.Model;
import advisor.repository.impl.Repository;
import advisor.view.Console;

import java.util.Map;

import static advisor.constant.Messages.INVALID_COMMAND_ARGUMENTS;
import static advisor.constant.Messages.PAGE;

public class CommandController {

    private final Map<Command, CommandImpl> commandFactory;

    private final Console console = Console.getInstance();

    private Command executeCommand;

    public CommandController(Repository repository) {

        commandFactory = Map.of(Command.NEW, new NewReleasesCommand(repository),
                                Command.FEATURED, new FeaturedPlaylistsCommand(repository),
                                Command.CATEGORIES, new CategoriesCommand(repository),
                                Command.PLAYLISTS, new PlaylistsCommand(repository));
    }

    public void execute(Command command, Model model) {

        try {

            Command pagingCommand = Command.NEXT;

            if (!command.equals(Command.PREV) && !command.equals(Command.NEXT)) {

                executeCommand = command;

                model.addAttribute("nextUrl", null);

                model.addAttribute("prevUrl", null);

                model.setExecute(false);

            } else {

                if (!model.isExecute()) {

                    throw new AdvisorException(INVALID_COMMAND_ARGUMENTS);

                }

                pagingCommand = command;

            }

            commandFactory.get(executeCommand).execute(model, pagingCommand);

            console.displayEntries(model.getEntries());

            console.displayMessage(PAGE,
                                   model.getAttribute("currentPage"),
                                   model.getAttribute("totalPages"));

        } catch (AdvisorException e) {

            console.displayMessage(e.getMessage());

        }

    }

}
