package advisor.domain;

import java.util.List;

public class Album extends Playlist {

    private final List<String> artists;

    public Album(String id, String name, String externalUrl, List<String> artists) {

        super(id, name, externalUrl);

        this.artists = artists;

    }

    @Override
    public String presentation() {

        String artistString = String.join(", ", artists);

        artistString = "[" + artistString + "]" + '\n';

        return super.presentation(artistString);

    }

}
