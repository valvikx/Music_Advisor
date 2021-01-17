package advisor.view;

import advisor.domain.Category;

import java.util.List;
import java.util.Scanner;

public class Console {

    private static final Console INSTANCE = new Console();

    private final Scanner scanner = new Scanner(System.in);

    private Console() {
    }

    public static Console getInstance() {

        return INSTANCE;

    }

    public String[] getCommandArgs() {

        return scanner.nextLine().split("\\s+", 2);

    }

    public void displayMessage(String message) {

        System.out.println(message);

    }

    public void displayMessage(String template, Object... arg) {

        System.out.printf(template + '\n', arg);

    }

    public void displayEntries(List<? extends Category> responseList) {

        responseList.forEach(obj -> System.out.println(obj.presentation()));

    }

}



