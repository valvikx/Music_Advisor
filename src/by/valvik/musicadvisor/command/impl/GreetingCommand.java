package by.valvik.musicadvisor.command.impl;


import by.valvik.musicadvisor.context.annotation.Inject;
import by.valvik.musicadvisor.context.annotation.Singleton;
import by.valvik.musicadvisor.command.Command;
import by.valvik.musicadvisor.constant.Status;
import by.valvik.musicadvisor.view.View;

import static by.valvik.musicadvisor.constant.AppConstant.COMMAND_QUALIFIER_GREETING;
import static by.valvik.musicadvisor.constant.Status.OK;
import static by.valvik.musicadvisor.util.Props.getValue;

@Singleton(qualifier = COMMAND_QUALIFIER_GREETING)
public class GreetingCommand implements Command {

    private static final String KEY_GREETING = "greeting";

    @Inject
    private View console;

    @Override
    public Status execute() {

        console.display(getValue(KEY_GREETING));

        return OK;

    }

}
