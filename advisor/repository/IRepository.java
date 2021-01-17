package advisor.repository;

import advisor.domain.Album;
import advisor.domain.Category;
import advisor.domain.Playlist;
import advisor.exception.AdvisorException;

import java.util.List;

public interface IRepository {

    String getAuthorizationHeader(String authorizationCode) throws AdvisorException;

    List<Album> getNewReleases(String pagingUrl,
                               int limit,
                               String authorizationHeader) throws AdvisorException;

    List<Playlist> getFeaturedPlaylists(String pagingUrl,
                                        int limit,
                                        String authorizationHeader) throws AdvisorException;

    List<Category> getCategories(String pagingUrl,
                                 int limit,
                                 String authorizationHeader) throws AdvisorException;

    List<Playlist> getCategoryPlaylists(String pagingUrl,
                                        int limit,
                                        String authorizationHeader,
                                        String categoryName) throws AdvisorException;

}
