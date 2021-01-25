package advisor.repository.impl;

import advisor.dao.IAuthDao;
import advisor.dao.IEntryDao;
import advisor.dao.impl.EntryDao;
import advisor.dao.impl.AuthDao;
import advisor.domain.Album;
import advisor.domain.Category;
import advisor.domain.Playlist;
import advisor.exception.AdvisorException;
import advisor.json.JsonHelper;
import advisor.repository.IRepository;
import advisor.uri.UriBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static advisor.constant.Messages.UNKNOWN_CATEGORY_NAME;

public class Repository implements IRepository {

    private final IAuthDao authDao = new AuthDao();

    private final IEntryDao entryDao = new EntryDao();

    private final JsonHelper jsonHelper = JsonHelper.getInstance();

    private String url;

    @Override
    public String getAuthorizationHeader(String authorizationCode) throws AdvisorException {

        String url = UriBuilder.getUrlToAccessTokens();

        String query = UriBuilder.getRequestToAccessTokens(authorizationCode);

        Map<String, String> jsonMembers = authDao.getMembers(url, query);

        return jsonMembers.get("tokenType") + " " + jsonMembers.get("accessToken");

    }

    @Override
    public List<Album> getNewReleases(String pagingUrl,
                                      int limit,
                                      String authorizationHeader) throws AdvisorException {

        url = pagingUrl == null ? UriBuilder.getUrlTo("new-releases", limit) : pagingUrl;

        entryDao.buildJson(url, authorizationHeader);

        Function<JsonElement, Album> convertor = je -> new Album(getValueOfId(je),
                                                                 getValueOfName(je),
                                                                 getValueOfExternalUrl(je),
                                                                 getValuesOfArtists(je));

        return entryDao.getEntries("albums", convertor);

    }

    @Override
    public List<Playlist> getFeaturedPlaylists(String pagingUrl,
                                               int limit,
                                               String authorizationHeader) throws AdvisorException {

        url = pagingUrl == null ? UriBuilder.getUrlTo("featured-playlists", limit) : pagingUrl;

        return getPlaylistList(authorizationHeader, url);

    }

    @Override
    public List<Category> getCategories(String pagingUrl,
                                        int limit,
                                        String authorizationHeader) throws AdvisorException {

        url = pagingUrl == null ? UriBuilder.getUrlTo("categories", limit) : pagingUrl;

        entryDao.buildJson(url, authorizationHeader);

        Function<JsonElement, Category> convertor = je -> new Category(getValueOfId(je),
                                                                       getValueOfName(je));

        return entryDao.getEntries("categories", convertor);


    }

    @Override
    public List<Playlist> getCategoryPlaylists(String pagingUrl,
                                               int limit,
                                               String authorizationHeader,
                                               String categoryName) throws AdvisorException {

        if (pagingUrl != null) {

            url = pagingUrl;

        } else {

            List<Category> categories = getCategories(null, 50, authorizationHeader);

            String categoryId = getCategoryId(categories, categoryName);

            if (categoryId != null) {

                url = UriBuilder.getUrlToPlaylists("categories", categoryId, limit);

            }  else {

                throw new AdvisorException(UNKNOWN_CATEGORY_NAME);

            }

        }

        return getPlaylistList(authorizationHeader, url);

    }

    public String getPrevious() {

        return entryDao.getPrevious();

    }

    public String getNext() {

        return entryDao.getNext();

    }

    public int getTotal() {

        return entryDao.getTotal();

    }

    public int getOffset() {

        return entryDao.getOffset();

    }

    private List<Playlist> getPlaylistList(String authorizationHeader, String url) throws AdvisorException {

        entryDao.buildJson(url, authorizationHeader);

        Function<JsonElement, Playlist> convertor = je -> new Playlist(getValueOfId(je),
                                                                       getValueOfName(je),
                                                                       getValueOfExternalUrl(je));

        return entryDao.getEntries("playlists", convertor);

    }

    private List<String> getValuesOfArtists(JsonElement jsonElement) {

        return serializeArtists(jsonHelper
                                    .setJsonObject(jsonElement)
                                    .getJsonArray("artists"));

    }

    private String getValueOfName(JsonElement jsonElement) {

        return jsonHelper
                    .setJsonObject(jsonElement)
                    .getStringValue("name");

    }

    private String getValueOfId(JsonElement jsonElement) {

        return jsonHelper
                    .setJsonObject(jsonElement)
                    .getStringValue("id");

    }

    private String getValueOfExternalUrl(JsonElement jsonElement) {

        return jsonHelper
                    .setJsonObject(jsonElement)
                    .setJsonObject("external_urls")
                    .getStringValue("spotify");

    }

    private List<String> serializeArtists(JsonArray artists) {

        return StreamSupport
                        .stream(artists.spliterator(), false)
                        .map(this::getValueOfName)
                        .collect(Collectors.toList());

    }

    private String getCategoryId(List<Category> categories, String categoryName) {

        Category category = categories
                                .stream()
                                .filter(obj -> obj.getName().equals(categoryName))
                                .findFirst()
                                .orElse(null);

        return category != null ? category.getId() : null;

    }

}
