package by.valvik.musicadvisor.command.impl;


import by.valvik.musicadvisor.context.annotation.Inject;
import by.valvik.musicadvisor.context.annotation.Singleton;
import by.valvik.musicadvisor.command.Command;
import by.valvik.musicadvisor.constant.Status;
import by.valvik.musicadvisor.view.View;

import static by.valvik.musicadvisor.constant.AppConstant.QUALIFIER_START_COMMAND;
import static by.valvik.musicadvisor.constant.Status.OK;
import static by.valvik.musicadvisor.util.Props.getValue;

@Singleton(qualifier = QUALIFIER_START_COMMAND)
public class StartCommand implements Command {

    private static final String KEY_APP_HEADER = "app_header";

    @Inject
    private View console;

    @Override
    public Status execute() {

        console.displayln(getValue(KEY_APP_HEADER));

        return OK;

    }

}
