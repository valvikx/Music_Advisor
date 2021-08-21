package by.valvik.musicadvisor.domain.category;

import by.valvik.musicadvisor.domain.ExternalUrls;
import by.valvik.musicadvisor.domain.Item;

import static by.valvik.musicadvisor.util.Props.getValue;

public class Category extends Item {

    public static final String KEY_CATEGORY = "category";

    public Category(String id, String name, ExternalUrls externalUrls) {

        super(id, name, externalUrls);

    }

    @Override
    public String presentation() {

        return getValue(KEY_CATEGORY).formatted(getName(), getId());
        
    }

}
