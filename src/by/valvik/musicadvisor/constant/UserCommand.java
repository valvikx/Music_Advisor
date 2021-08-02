package by.valvik.musicadvisor.constant;

public enum UserCommand {

    AUTH, FEATURED, NEW, PLAYLISTS, CATEGORIES, EXIT;

    public String getName() {

       return name().toLowerCase();

    }

}
