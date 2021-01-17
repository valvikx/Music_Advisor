package advisor.model;

import advisor.domain.Category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {

    private final Map<String, String> attributes = new HashMap<>();

    private boolean isAuthorized;

    private boolean isExecute;

    private List<? extends Category> entries;

    private int limit = 5; //default value

    public void addAttribute(String key, String value) {

        attributes.put(key, value);

    }

    public String getAttribute(String key) {

        return attributes.getOrDefault(key, null);

    }

    public boolean isAuthorized() {

        return isAuthorized;

    }

    public void setAuthorized(boolean authorized) {

        isAuthorized = authorized;

    }

    public boolean isExecute() {

        return isExecute;

    }

    public void setExecute(boolean execute) {

        isExecute = execute;

    }

    public void setEntries(List<? extends Category> entries) {

        this.entries = entries;

    }

    public List<? extends Category> getEntries() {

        return entries;

    }

    public int getLimit() {

        return limit;

    }

    public void setLimit(int limit) {

        if (limit < 1) {

            this.limit = 1; // min value;

        } else {

            this.limit = Math.min(limit, 50); // max value;

        }

    }

}
