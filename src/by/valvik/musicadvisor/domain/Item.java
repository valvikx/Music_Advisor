package by.valvik.musicadvisor.domain;

public abstract class Item {

    private String id;

    private String name;

    private ExternalUrls externalUrls;

    public Item(String id, String name, ExternalUrls externalUrls) {

        this.id = id;

        this.name = name;

        this.externalUrls = externalUrls;

    }

    public String getId() {

        return id;

    }

    public String getName() {

        return name;

    }

    public ExternalUrls getExternalUrls() {

        return externalUrls;

    }

    public abstract String presentation();

}
