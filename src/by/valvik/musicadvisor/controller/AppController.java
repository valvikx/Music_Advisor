package by.valvik.musicadvisor.controller;

import by.valvik.musicadvisor.annotation.Inject;
import by.valvik.musicadvisor.annotation.Singleton;
import by.valvik.musicadvisor.command.Command;
import by.valvik.musicadvisor.constant.Status;
import by.valvik.musicadvisor.constant.UserCommand;
import by.valvik.musicadvisor.context.ApplicationContext;
import by.valvik.musicadvisor.holder.ContextHolder;

import java.util.Objects;

import static by.valvik.musicadvisor.constant.AppConstant.QUALIFIER_ARGS_COMMAND;
import static by.valvik.musicadvisor.constant.AppConstant.QUALIFIER_GREETING_COMMAND;
import static by.valvik.musicadvisor.constant.Status.BAD_REQUEST;
import static by.valvik.musicadvisor.constant.UserCommand.EXIT;

@Singleton
public class AppController {

    @Inject(qualifier = QUALIFIER_GREETING_COMMAND)
    private Command greetingCommand;

    @Inject(qualifier = QUALIFIER_ARGS_COMMAND)
    private Command argsCommand;

    @Inject
    private ContextHolder contextHolder;

    public void start(ApplicationContext context) {

        greetingCommand.execute();

        while (true) {

            Status status = argsCommand.execute();

            if (status.equals(BAD_REQUEST)) {

                continue;

            }

            UserCommand userCommand = contextHolder.getArgsHolder().getUserCommand();

            if (Objects.equals(userCommand, EXIT)) {

                break;

            }

            Class<? extends Command> commandClass = context.getConfig()
                                                           .getImplClassByQualifier(Command.class, userCommand.getName())
                                                           .orElseThrow();

            Command command = context.getObject(commandClass);

            command.execute();

        }

    }

}
