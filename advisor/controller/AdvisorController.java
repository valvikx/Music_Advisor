package advisor.controller;

import advisor.command.Command;
import advisor.model.Model;
import advisor.repository.impl.Repository;
import advisor.view.Console;

import java.util.Locale;

import static advisor.constant.Messages.*;

public class AdvisorController {

    private final Repository repository = new Repository();

    private final Model model = new Model();

    private final Console console = Console.getInstance();

    private Command command;

    public void letsGo() {

        AuthController authController = new AuthController(repository);

        CommandController commandController = new CommandController(repository);

        while (true) {

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

            if (command.equals(Command.PLAYLISTS) && commandArgs.length == 1) {

                throw new IllegalArgumentException(INVALID_COMMAND_ARGUMENTS);

            } else if (commandArgs.length == 2) {

                String[] tokens = commandArgs[1].split("\\s+");

                // command args example: [new -page 10]
                if ("-page".equals(tokens[0]) && tokens.length == 2) {

                    model.setLimit(Integer.parseInt(tokens[1]));

                } else if (command.equals(Command.PLAYLISTS)) {

                    // command args example: [playlists CATEGORY_NAME]
                    model.addAttribute("categoryName", tokens[0]);

                    // command args example: [playlists CATEGORY_NAME -page 10]
                    if (tokens.length == 3) {

                        model.setLimit(Integer.parseInt(tokens[2]));

                    }

                } else {

                    throw new IllegalArgumentException(INVALID_COMMAND_ARGUMENTS);

                }

            }

            return true;

        } catch (IllegalArgumentException e) {

            console.displayMessage(INVALID_COMMAND_ARGUMENTS);

        }

        return false;

    }

}
