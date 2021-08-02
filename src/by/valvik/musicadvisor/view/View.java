package by.valvik.musicadvisor.view;

import by.valvik.musicadvisor.domain.Item;

import java.util.List;

public interface View {

    void display(String message);

    void displayLine(String message);

    void display(String template, Object... arg);

    <T extends Item> void display(List<T> items, int currentPage, int totalPages);

    String[] getArgs();
    
}
