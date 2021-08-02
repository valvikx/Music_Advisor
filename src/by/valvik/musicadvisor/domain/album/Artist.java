package by.valvik.musicadvisor.domain.album;

import by.valvik.musicadvisor.domain.ExternalUrls;
import by.valvik.musicadvisor.domain.Item;
import by.valvik.musicadvisor.util.Props;

import static java.lang.String.format;

public class Artist extends Item {

    private static final String ARTIST = "artist";

    public Artist() {


    }

    public Artist(String id, String name, ExternalUrls externalUrls) {

        super(id, name, externalUrls);

    }

    @Override
    public String presentation() {

        return format(Props.getValue(ARTIST), getName(), getExternalUrls().getSpotify());

    }

}
