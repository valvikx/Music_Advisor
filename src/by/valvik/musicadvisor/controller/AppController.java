package by.valvik.musicadvisor.controller;

import by.valvik.musicadvisor.context.annotation.Inject;
import by.valvik.musicadvisor.context.annotation.Singleton;
import by.valvik.musicadvisor.command.Command;
import by.valvik.musicadvisor.constant.Status;
import by.valvik.musicadvisor.constant.UserCommand;
import by.valvik.musicadvisor.context.ApplicationContext;
import by.valvik.musicadvisor.context.holder.ContextHolder;

import java.util.Objects;

import static by.valvik.musicadvisor.constant.AppConstant.QUALIFIER_ARGS_COMMAND;
import static by.valvik.musicadvisor.constant.AppConstant.QUALIFIER_START_COMMAND;
import static by.valvik.musicadvisor.constant.Status.BAD_REQUEST;
import static by.valvik.musicadvisor.constant.UserCommand.EXIT;

@Singleton
public class AppController {

    @Inject
    ApplicationContext context;

    @Inject(qualifier = QUALIFIER_START_COMMAND)
    private Command greetingCommand;

    @Inject(qualifier = QUALIFIER_ARGS_COMMAND)
    private Command argsCommand;

    @Inject
    private ContextHolder contextHolder;

    public void start() {

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

            Command command = context.getObject(userCommand.getName(), Command.class);

            command.execute();

        }

    }

}
