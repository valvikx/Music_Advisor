package by.valvik.musicadvisor.controller;

import by.valvik.musicadvisor.context.annotation.Inject;
import by.valvik.musicadvisor.context.annotation.Singleton;
import by.valvik.musicadvisor.command.Command;
import by.valvik.musicadvisor.constant.Status;
import by.valvik.musicadvisor.constant.UserCommand;
import by.valvik.musicadvisor.context.ApplicationContext;
import by.valvik.musicadvisor.context.holder.ContextHolder;

import java.util.Objects;

import static by.valvik.musicadvisor.constant.AppConstant.COMMAND_QUALIFIER_ARGS;
import static by.valvik.musicadvisor.constant.AppConstant.COMMAND_QUALIFIER_GREETING;
import static by.valvik.musicadvisor.constant.Status.BAD_REQUEST;
import static by.valvik.musicadvisor.constant.UserCommand.EXIT;

@Singleton
public class AppController {

    @Inject(qualifier = COMMAND_QUALIFIER_GREETING)
    private Command greetingCommand;

    @Inject(qualifier = COMMAND_QUALIFIER_ARGS)
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

            Command command = context.getObject(userCommand.getName(), Command.class);

            command.execute();

        }

    }

}
