package advisor.dao;

import advisor.exception.AdvisorException;

import java.util.Map;

public interface IAuthDao {

    Map<String, String> getParams(String url, String query) throws AdvisorException;

}
