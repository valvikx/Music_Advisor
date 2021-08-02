package by.valvik.musicadvisor.holder;

import by.valvik.musicadvisor.annotation.Inject;
import by.valvik.musicadvisor.annotation.Singleton;
import by.valvik.musicadvisor.domain.Items;
import by.valvik.musicadvisor.domain.Item;

@Singleton
public class ContextHolder {

    @Inject
    private ArgsHolder argsHolder;

    private String authHeader;

    private Items<? extends Item> items;

    public ArgsHolder getArgsHolder() {

        return argsHolder;

    }

    public void setArgsHolder(ArgsHolder argsHolder) {

        this.argsHolder = argsHolder;

    }

    public String getAuthHeader() {

        return authHeader;

    }

    public void setAuthHeader(String authHeader) {

        this.authHeader = authHeader;

    }

    public Items<? extends Item> getItems() {

        return items;

    }

    public void setItems(Items<? extends Item> items) {

        this.items = items;

    }

}
