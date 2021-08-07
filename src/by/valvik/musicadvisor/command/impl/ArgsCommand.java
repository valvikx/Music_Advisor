package by.valvik.musicadvisor.command.impl;

import by.valvik.musicadvisor.context.annotation.Inject;
import by.valvik.musicadvisor.context.annotation.Singleton;
import by.valvik.musicadvisor.command.Command;
import by.valvik.musicadvisor.constant.Status;
import by.valvik.musicadvisor.exception.UtilException;
import by.valvik.musicadvisor.context.holder.ArgsHolder;
import by.valvik.musicadvisor.context.holder.ContextHolder;
import by.valvik.musicadvisor.view.View;

import java.util.Objects;

import static by.valvik.musicadvisor.constant.AppConstant.KEY_COMMAND_CANNOT_BE_EXECUTED;
import static by.valvik.musicadvisor.constant.AppConstant.COMMAND_QUALIFIER_ARGS;
import static by.valvik.musicadvisor.constant.Status.BAD_REQUEST;
import static by.valvik.musicadvisor.constant.Status.OK;
import static by.valvik.musicadvisor.constant.UserCommand.AUTH;
import static by.valvik.musicadvisor.constant.UserCommand.EXIT;
import static by.valvik.musicadvisor.util.Parsers.collectArgs;
import static by.valvik.musicadvisor.util.Props.getValue;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Singleton(qualifier = COMMAND_QUALIFIER_ARGS)
public class ArgsCommand implements Command {

    private static final String KEY_ENTER_COMMAND = "enter_command";

    private static final String KEY_AUTH_COMMANDS = "auth_commands";

    private static final String KEY_MAIN_COMMANDS = "main_commands";

    private static final String KEY_PROVIDE_ACCESS = "provide_access";

    private static final String KEY_BYE = "bye";

    @Inject
    private View console;

    @Inject
    private ContextHolder contextHolder;

    @Override
    public Status execute() {

        console.display(getValue(KEY_ENTER_COMMAND));

        console.displayLine(nonNull(contextHolder.getAuthHeader()) ? getValue(KEY_MAIN_COMMANDS)
                                                                   : getValue(KEY_AUTH_COMMANDS));

        Status status = OK;

        try {

            ArgsHolder argsHolder = collectArgs(console.getArgs());

            if (nonNull(argsHolder.getUserCommand())) {

                contextHolder.setArgsHolder(argsHolder);

            } else if (nonNull(argsHolder.getDirection())) {

                contextHolder.getArgsHolder().setDirection(argsHolder.getDirection());

            }

            if (Objects.equals(argsHolder.getUserCommand(), EXIT)) {

                console.display(getValue(KEY_BYE));

                return status;

            }

            argsHolder = contextHolder.getArgsHolder();

            if ((Objects.equals(argsHolder.getUserCommand(), AUTH) || isNull(argsHolder.getUserCommand())) &&
                nonNull(argsHolder.getDirection())) {

                console.display(getValue(KEY_COMMAND_CANNOT_BE_EXECUTED));

                return BAD_REQUEST;

            }

            if (!Objects.equals(argsHolder.getUserCommand(), AUTH) && isNull(contextHolder.getAuthHeader())) {

                console.display(getValue(KEY_PROVIDE_ACCESS));

                return BAD_REQUEST;

            }

        } catch (UtilException e) {

            status = BAD_REQUEST;

            console.display(e.getMessage());

        }

        return status;
        
    }

}
