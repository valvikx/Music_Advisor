package advisor.domain;

public class Category {

    private final String id;

    private final String name;

    public Category(String id, String name) {

        this.id = id;

        this.name = name;

    }

    public String presentation() {

        return name + '\n';

    }

    public String getName() {

        return name;

    }

    public String getId() {

        return id;

    }

}
