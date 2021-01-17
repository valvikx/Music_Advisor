package advisor.dao;

import advisor.exception.AdvisorException;
import com.google.gson.JsonElement;

import java.util.List;
import java.util.function.Function;

public interface IEntryDao {

    void buildJson(String url, String authorizationHeader) throws AdvisorException;

    <T> List<T> getEntries(String pagingObject,
                           Function<JsonElement, T> converter) throws AdvisorException;

}
