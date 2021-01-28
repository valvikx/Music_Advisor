package advisor.controller;

import advisor.command.Command;
import advisor.command.impl.*;
import advisor.exception.AdvisorException;
import advisor.model.Model;
import advisor.repository.impl.AdvisorRepository;
import advisor.view.Console;

import java.util.Map;

import static advisor.view.Messages.INVALID_COMMAND_ARGUMENTS;
import static advisor.view.Messages.PAGE;

public class CommandController {

    private final Map<Command, CommandImpl> commandFactory;

    private final Console console = Console.getInstance();

    private Command executeCommand;

    public CommandController(AdvisorRepository advisorRepository) {

        commandFactory = Map.of(Command.NEW, new NewReleasesCommand(advisorRepository),
                                Command.FEATURED, new FeaturedPlaylistsCommand(advisorRepository),
                                Command.CATEGORIES, new CategoriesCommand(advisorRepository),
                                Command.PLAYLISTS, new PlaylistsCommand(advisorRepository));
    }

    public void execute(Command command, Model model) {

        try {

            Command pagingCommand = Command.NEXT;

            if (!command.equals(Command.PREV) && !command.equals(Command.NEXT)) {

                executeCommand = command;

                model.addAttribute("next", null);

                model.addAttribute("previous", null);

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
