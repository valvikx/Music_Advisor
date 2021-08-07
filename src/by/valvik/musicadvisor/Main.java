package by.valvik.musicadvisor;

import by.valvik.musicadvisor.context.ApplicationContext;
import by.valvik.musicadvisor.controller.AppController;

public class Main {

    private static final String PACKAGES_PREFIX = "by.valvik.musicadvisor";

    public static void main(String[] args) {

        ApplicationContext context = Application.run(PACKAGES_PREFIX);

        AppController controller = context.getObject(AppController.class);

        controller.start();

    }

}
