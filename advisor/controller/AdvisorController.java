package advisor.controller;

import advisor.command.Command;
import advisor.model.Model;
import advisor.repository.impl.Repository;
import advisor.view.Console;

import java.util.Locale;

import static advisor.view.Messages.*;

public class AdvisorController {

    private final Repository repository = new Repository();

    private final Model model = new Model();

    private final Console console = Console.getInstance();

    private Command command;

    public void letsGo() {

        AuthController authController = new AuthController(repository);

        CommandController commandController = new CommandController(repository);

        while (true) {

            // query command: [command name (required)] [category name (required for playlists)] [-page (optional)]
            // query command example: new
            // query command example: categories -page 10
            // query command example: playlists Hip Hop -page 10
            String[] commandArgs = console.getCommandArgs();

            if (isValid(commandArgs)) {

                if (command.equals(Command.EXIT)) {

                    break;

                } else if (command.equals(Command.AUTH)) {

                    if (model.isAuthorized()) {

                        console.displayMessage(ALREADY_LOGGED_IN);

                        continue;

                    }

                    authController.authorize(model);

                } else {

                    if (!model.isAuthorized()) {

                        console.displayMessage(PROVIDE_ACCESS);

                        continue;

                    }

                    commandController.execute(command, model);

                }

            }

        }

    }

    private boolean isValid(String[] commandArgs) {

        try {

            command = Command.valueOf(commandArgs[0].toUpperCase(Locale.ROOT));

            if (commandArgs.length == 2 && commandArgs[1].contains("-page")) {

                String[] args = commandArgs[1].split("-page");

                if (!args[0].isEmpty()) {

                    // query command example: playlists CATEGORY_NAME -page 10
                    model.addAttribute("categoryName", args[0].trim());

                }

                // query command example: new -page 10
                model.setLimit(Integer.parseInt(args[1].trim()));

            } else if (commandArgs.length == 2 && command.equals(Command.PLAYLISTS)) {

                // query command example: playlists Hip Hop
                model.addAttribute("categoryName", commandArgs[1]);

            } else if (commandArgs.length == 2) {

                throw new IllegalArgumentException();

            }

            return true;

        } catch (IllegalArgumentException e) {

            console.displayMessage(INVALID_COMMAND_ARGUMENTS);

        }

        return false;

    }

}
