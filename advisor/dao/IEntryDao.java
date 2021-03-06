package advisor.dao;

import advisor.exception.AdvisorException;
import com.google.gson.JsonElement;

import java.util.List;
import java.util.function.Function;

public interface IEntryDao {

    void createNewJson(String url, String authorizationHeader) throws AdvisorException;

    <T> List<T> getEntries(String pagingObject,
                           Function<JsonElement, T> converter) throws AdvisorException;

    String getNext();

    String getPrevious();

    int getTotal();

    int getOffset();

}
