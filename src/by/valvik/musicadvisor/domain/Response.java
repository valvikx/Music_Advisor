package by.valvik.musicadvisor.domain;

import java.util.List;

public class Response<T extends Item> {

    private List<T> items;

    private Integer limit;

    private String next;

    private Integer offset;

    private String previous;

    private Integer total;

    public List<T> getItems() {

        return items;

    }

    public Integer getLimit() {

        return limit;

    }

    public String getNext() {

        return next;

    }

    public Integer getOffset() {

        return offset;

    }

    public String getPrevious() {

        return previous;

    }

    public Integer getTotal() {

        return total;

    }

}
