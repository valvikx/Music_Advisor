package advisor.domain;

public class Playlist extends Category {

    private final String externalUrl;

    public Playlist(String id, String name, String externalUrl) {

        super(id, name);

        this.externalUrl = externalUrl;

    }

    @Override
    public String presentation() {

        return super.presentation() + externalUrl + '\n';

    }

    public String presentation(String artists) {

        return super.presentation() + artists + externalUrl + '\n';

    }

}
