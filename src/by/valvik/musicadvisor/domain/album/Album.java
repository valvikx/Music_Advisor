package by.valvik.musicadvisor.domain.album;

import by.valvik.musicadvisor.domain.ExternalUrls;
import by.valvik.musicadvisor.domain.Item;

import java.util.List;

import static by.valvik.musicadvisor.constant.Delimiter.EMPTY;
import static by.valvik.musicadvisor.util.Props.getValue;
import static java.util.stream.Collectors.joining;

public class Album extends Item {

    private static final String KEY_ALBUM = "album";

    private List<Artist> artists;

    public Album(String id, String name, ExternalUrls externalUrls, List<Artist> artists) {

        super(id, name, externalUrls);

        this.artists = artists;

    }

    @Override
    public String presentation() {

        return getValue(KEY_ALBUM).formatted(getName(),
                                             getExternalUrls().getSpotify(),
                                             artists.stream()
                                                    .map(Artist::presentation)
                                                    .collect(joining(EMPTY.getSign())));

    }

}
