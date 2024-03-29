package by.valvik.musicadvisor.repository.impl;

import by.valvik.musicadvisor.context.annotation.Inject;
import by.valvik.musicadvisor.context.annotation.Singleton;
import by.valvik.musicadvisor.domain.Item;
import by.valvik.musicadvisor.domain.Response;
import by.valvik.musicadvisor.domain.error.RegularError;
import by.valvik.musicadvisor.exception.ConfigurationException;
import by.valvik.musicadvisor.exception.RepositoryException;
import by.valvik.musicadvisor.exception.UtilException;
import by.valvik.musicadvisor.context.holder.ContextHolder;
import by.valvik.musicadvisor.http.AppHttpClient;
import by.valvik.musicadvisor.json.JsonHandler;
import by.valvik.musicadvisor.json.JsonMapper;
import by.valvik.musicadvisor.repository.ResponseRepository;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

import static by.valvik.musicadvisor.constant.AppConstant.ERROR;

@Singleton
public class SpotifyResponseRepository implements ResponseRepository {

    @Inject
    private ContextHolder contextHolder;

    @Inject
    private AppHttpClient client;

    @Inject
    private JsonHandler jsonHandler;

    @Inject
    private JsonMapper jsonMapper;

    @Override
    public <T extends Item> Response<T> getResponse(String url, String itemsName, Type typeToken) throws RepositoryException {

        String json;

        try {

            json = client.performGet(url, contextHolder.getAuthHeader());

        } catch (Exception e) {

            throw new ConfigurationException(e);

        }

        validate(json);

        JsonObject jsonObject = jsonHandler.getJsonObject(itemsName);

        return jsonMapper.toResponse(jsonObject, typeToken);

    }

    private void validate(String json) throws RepositoryException {

        try {

            if (jsonHandler.buildDOM(json).hasMember(ERROR)) {

                RegularError error = jsonMapper.toEntity(json, RegularError.class);

                throw new RepositoryException(error.getError().getMessage());

            }
            
        } catch (UtilException e) {

            throw new RepositoryException(e.getMessage());

        }

    }

}
