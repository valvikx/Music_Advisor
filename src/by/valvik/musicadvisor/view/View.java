package by.valvik.musicadvisor.view;

import by.valvik.musicadvisor.domain.Item;

import java.util.List;

public interface View {

    void displayln(String message);

    void display(String message);

    void displayf(String template, Object... arg);

    <T extends Item> void displayList(List<T> items, int currentPage, int totalPages);

    String[] getArgs();
    
}
