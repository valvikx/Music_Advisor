package advisor.dao;

import advisor.exception.AdvisorException;

import java.util.Map;

public interface IAuthDao {

    Map<String, String> getMembers(String url, String query) throws AdvisorException;

}
