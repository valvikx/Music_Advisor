package by.valvik.musicadvisor.view.impl;

import by.valvik.musicadvisor.annotation.Inject;
import by.valvik.musicadvisor.annotation.Singleton;
import by.valvik.musicadvisor.domain.Item;
import by.valvik.musicadvisor.util.Props;
import by.valvik.musicadvisor.view.View;

import java.util.List;
import java.util.Scanner;

@Singleton
public class Console implements View {

    private static final String REGEX = "\\s+";

    private static final String KEY_PAGE = "page";

    @Inject
    private Scanner scanner;

    @Override
    public void display(String message) {

        System.out.println(message);

    }

    @Override
    public void displayLine(String message) {

        System.out.print(message);

    }

    @Override
    public void display(String template, Object... arg) {

        System.out.printf(template + "\n", arg);

    }

    @Override
    public <T extends Item> void display(List<T> items, int currentPage, int totalPages) {

        display(Props.getValue(KEY_PAGE), currentPage, totalPages);

        items.forEach(i -> display(i.presentation()));

    }

    @Override
    public String[] getArgs() {

        return scanner.nextLine().split(REGEX);

    }

}
