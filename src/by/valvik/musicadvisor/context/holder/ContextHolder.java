package by.valvik.musicadvisor.context.holder;

import by.valvik.musicadvisor.context.annotation.Inject;
import by.valvik.musicadvisor.context.annotation.Singleton;
import by.valvik.musicadvisor.domain.Response;
import by.valvik.musicadvisor.domain.Item;

@Singleton
public class ContextHolder {

    @Inject
    private ArgsHolder argsHolder;

    private String authHeader;

    private Response<? extends Item> response;

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

    public Response<? extends Item> getItems() {

        return response;

    }

    public void setItems(Response<? extends Item> response) {

        this.response = response;

    }

}
