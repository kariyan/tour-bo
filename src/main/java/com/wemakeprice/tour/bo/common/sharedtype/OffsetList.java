package com.wemakeprice.tour.bo.common.sharedtype;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OffsetList<T> implements Iterable<T> {

    private List<T> results;
    private int totalCount;

    public OffsetList() {
        this.results = new ArrayList<>();
    }

    public OffsetList(int totalCount) {
        this();
        this.totalCount = totalCount;
    }

    public OffsetList(List<T> results, int totalCount) {
        this.results = results;
        this.totalCount = totalCount;
    }

    @Override
    public Iterator<T> iterator() {
        return results.iterator();
    }

    public int size() {
        return (results != null) ? results.size() : 0;
    }

    @JsonIgnore
    public T getFirst() {
        return (!results.isEmpty()) ? results.get(0) : null;
    }

    public T get(int index) {
        return (results != null) ? results.get(index) : null;
    }

    public void add(T result) {
        results.add(result);
    }

    @JsonIgnore
    public boolean isEmpty() {
        return (results == null || results.isEmpty());
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
