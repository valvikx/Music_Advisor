package by.valvik.musicadvisor.domain.playlist;

import by.valvik.musicadvisor.domain.ExternalUrls;
import by.valvik.musicadvisor.domain.Item;

import static by.valvik.musicadvisor.util.Props.getValue;

public class PlayList extends Item {

    private static final String KEY_PLAYLIST = "playlist";

    public PlayList(String id, String name, ExternalUrls externalUrl) {

        super(id, name, externalUrl);

    }

    @Override
    public String presentation() {

        return getValue(KEY_PLAYLIST).formatted(getName(),
                                                getExternalUrls().getSpotify());

    }

}
