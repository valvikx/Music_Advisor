package by.valvik.musicadvisor.view.impl;

import by.valvik.musicadvisor.context.annotation.Inject;
import by.valvik.musicadvisor.context.annotation.Singleton;
import by.valvik.musicadvisor.domain.Item;
import by.valvik.musicadvisor.view.View;

import java.util.List;
import java.util.Scanner;

import static by.valvik.musicadvisor.util.Props.getValue;

@Singleton
public class Console implements View {

    private static final String REGEX = "\\s+";

    private static final String KEY_PAGE = "page";

    @Inject
    private Scanner scanner;

    @Override
    public void displayln(String message) {

        System.out.println(message);

    }

    @Override
    public void display(String message) {

        System.out.print(message);

    }

    @Override
    public void displayf(String template, Object... arg) {

        System.out.printf(template + "\n", arg);

    }

    @Override
    public <T extends Item> void displayList(List<T> items, int currentPage, int totalPages) {

        displayf(getValue(KEY_PAGE), currentPage, totalPages);

        items.forEach(i -> displayln(i.presentation()));

    }

    @Override
    public String[] getArgs() {

        return scanner.nextLine().split(REGEX);

    }

}
