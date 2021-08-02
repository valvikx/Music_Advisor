package by.valvik.musicadvisor.domain.category;

import by.valvik.musicadvisor.domain.ExternalUrls;
import by.valvik.musicadvisor.domain.Item;
import by.valvik.musicadvisor.util.Props;

import static java.lang.String.format;

public class Category extends Item {

    public static final String KEY_CATEGORY = "category";

    public Category(String id, String name, ExternalUrls externalUrls) {

        super(id, name, externalUrls);

    }

    @Override
    public String presentation() {

        return format(Props.getValue(KEY_CATEGORY), getName(), getId());
        
    }

}
