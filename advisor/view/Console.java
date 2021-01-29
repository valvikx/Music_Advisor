package advisor.view;

import advisor.domain.Category;

import java.util.List;
import java.util.Scanner;

import static advisor.view.Messages.PAGE;

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

    public void display(String message) {

        System.out.println(message);

    }

    public void display(String template, Object... arg) {

        System.out.printf(template + '\n', arg);

    }

    public void display(List<? extends Category> responseList,
                        String currentPage, String totalPages) {

        responseList.forEach(obj -> System.out.println(obj.presentation()));

        display(PAGE, currentPage, totalPages);

    }

}



