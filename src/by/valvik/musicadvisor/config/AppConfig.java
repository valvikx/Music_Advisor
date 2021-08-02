package by.valvik.musicadvisor.config;

import by.valvik.musicadvisor.constant.UserCommand;
import by.valvik.musicadvisor.domain.category.Category;
import by.valvik.musicadvisor.domain.playlist.PlayList;
import by.valvik.musicadvisor.tuple.Tuple;
import by.valvik.musicadvisor.annotation.Configuration;
import by.valvik.musicadvisor.annotation.Singleton;
import by.valvik.musicadvisor.domain.Item;
import by.valvik.musicadvisor.domain.Items;
import by.valvik.musicadvisor.domain.album.Album;
import by.valvik.musicadvisor.factory.Factory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Scanner;

@Configuration
public class AppConfig {

    private static final String NEW_RELEASES = "new-releases";

    private static final String ALBUMS = "albums";

    private static final String CATEGORIES = "categories";

    private static final String FEATURED_PLAYLISTS = "featured-playlists";

    private static final String PLAYLISTS = "playlists";

    @Singleton
    public Scanner scanner() {

        return new Scanner(System.in);

    }

    @Singleton
    public Factory<Class<? extends Item>, Type> typeTokenFactory() {

        Factory<Class<? extends Item>, Type> factory = Factory.of();

        factory.add(Album.class, new TypeToken<Items<Album>>() {}.getType());

        factory.add(Category.class, new TypeToken<Items<Category>>() {}.getType());

        factory.add(PlayList.class, new TypeToken<Items<PlayList>>() {}.getType());

        return factory;

    }

    @Singleton
    public Factory<UserCommand, Tuple<String, String>> sourceFactory() {

        Factory<UserCommand, Tuple<String, String>> factory = Factory.of();

        factory.add(UserCommand.NEW, new Tuple<>(NEW_RELEASES, ALBUMS));

        factory.add(UserCommand.CATEGORIES, new Tuple<>(CATEGORIES, CATEGORIES));

        factory.add(UserCommand.FEATURED, new Tuple<>(FEATURED_PLAYLISTS, PLAYLISTS));

        factory.add(UserCommand.PLAYLISTS, new Tuple<>(null, PLAYLISTS));

        return factory;

    }

}
