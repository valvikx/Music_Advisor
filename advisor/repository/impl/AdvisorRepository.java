package advisor.repository.impl;

import advisor.dao.IAuthDao;
import advisor.dao.IEntryDao;
import advisor.dao.impl.AuthDao;
import advisor.dao.impl.EntryDao;
import advisor.domain.Album;
import advisor.domain.Category;
import advisor.domain.Playlist;
import advisor.exception.AdvisorException;
import advisor.json.JsonDocument;
import advisor.repository.IRepository;
import advisor.url.UrlBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AdvisorRepository implements IRepository {

    private final IAuthDao authDao = new AuthDao();

    private final IEntryDao entryDao = new EntryDao();

    private final JsonDocument jsonDocument = JsonDocument.getInstance();

    private String url;

    @Override
    public String getAuthorizationHeader(String authorizationCode) throws AdvisorException {

        String url = UrlBuilder.getUrlToAccessTokens();

        String query = UrlBuilder.getRequestToAccessTokens(authorizationCode);

        Map<String, String> params = authDao.getParams(url, query);

        return params.get("tokenType") + " " + params.get("accessToken");

    }

    @Override
    public List<Album> getNewReleases(String pagingUrl,
                                      int limit,
                                      String authorizationHeader) throws AdvisorException {

        url = pagingUrl == null ? UrlBuilder.getUrlTo("new-releases", limit) : pagingUrl;

        entryDao.createNewJson(url, authorizationHeader);

        Function<JsonElement, Album> convertor = je -> new Album(getIdValue(je),
                                                                 getNameValue(je),
                                                                 getVExternalUrlValue(je),
                                                                 getArtistsValues(je));

        return entryDao.getEntries("albums", convertor);

    }

    @Override
    public List<Playlist> getFeaturedPlaylists(String pagingUrl,
                                               int limit,
                                               String authorizationHeader) throws AdvisorException {

        url = pagingUrl == null ? UrlBuilder.getUrlTo("featured-playlists", limit) : pagingUrl;

        return getPlaylistList(authorizationHeader, url);

    }

    @Override
    public List<Category> getCategories(String pagingUrl,
                                        int limit,
                                        String authorizationHeader) throws AdvisorException {

        url = pagingUrl == null ? UrlBuilder.getUrlTo("categories", limit) : pagingUrl;

        entryDao.createNewJson(url, authorizationHeader);

        Function<JsonElement, Category> convertor = je -> new Category(getIdValue(je),
                                                                       getNameValue(je));

        return entryDao.getEntries("categories", convertor);

    }

    @Override
    public List<Playlist> getCategoryPlaylists(String pagingUrl,
                                               int limit,
                                               String authorizationHeader,
                                               String categoryName) throws AdvisorException {

        if (pagingUrl == null) {

            List<Category> categories = getCategories(null, 50, authorizationHeader);

            String categoryId = getCategoryId(categories, categoryName);

            if (categoryId != null) {

                url = UrlBuilder.getUrlToPlaylists("categories", categoryId, limit);

            }  else {

                throw new AdvisorException("Unknown category name.");

            }

        } else {

            url = pagingUrl;

        }

        return getPlaylistList(authorizationHeader, url);

    }
    @Override
    public String getPrevious() {

        return entryDao.getPrevious();

    }

    @Override
    public String getNext() {

        return entryDao.getNext();

    }

    @Override
    public int getTotal() {

        return entryDao.getTotal();

    }

    @Override
    public int getOffset() {

        return entryDao.getOffset();

    }

    private List<Playlist> getPlaylistList(String authorizationHeader, String url) throws AdvisorException {

        entryDao.createNewJson(url, authorizationHeader);

        Function<JsonElement, Playlist> convertor = je -> new Playlist(getIdValue(je),
                                                                       getNameValue(je),
                                                                       getVExternalUrlValue(je));

        return entryDao.getEntries("playlists", convertor);

    }

    private List<String> getArtistsValues(JsonElement jsonElement) {

        return serializeArtists(jsonDocument.setJsonObject(jsonElement)
                                            .getJsonArray("artists"));

    }

    private String getNameValue(JsonElement jsonElement) {

        return jsonDocument.setJsonObject(jsonElement)
                           .getStringValue("name");

    }

    private String getIdValue(JsonElement jsonElement) {

        return jsonDocument.setJsonObject(jsonElement)
                           .getStringValue("id");

    }

    private String getVExternalUrlValue(JsonElement jsonElement) {

        return jsonDocument.setJsonObject(jsonElement)
                           .setJsonObject("external_urls")
                           .getStringValue("spotify");

    }

    private List<String> serializeArtists(JsonArray artists) {

        return StreamSupport.stream(artists.spliterator(), false)
                            .map(this::getNameValue)
                            .collect(Collectors.toList());

    }

    private String getCategoryId(List<Category> categories, String categoryName) {

        Optional<String> categoryId = categories.stream()
                                                .filter(obj -> obj.getName().equals(categoryName))
                                                .map(Category::getId)
                                                .findFirst();

        return categoryId.orElse(null);

    }

}
