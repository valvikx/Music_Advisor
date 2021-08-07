package by.valvik.musicadvisor.constant;

public enum UserCommand {

    AUTH, FEATURED, NEW, PLAYLISTS, CATEGORIES, EXIT;

    public String getName() {

       return name().toLowerCase();

    }

    public static class Qualifier {

        public static final String AUTH = "auth";

        public static final String NEW = "new";

        public static final String CATEGORIES = "categories";

        public static final String FEATURED = "featured";

        public static final String PLAYLISTS = "playlists";

    }

}
