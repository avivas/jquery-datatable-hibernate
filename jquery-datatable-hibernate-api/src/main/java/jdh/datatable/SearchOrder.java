package jdh.datatable;

import java.util.Map;

public class SearchOrder
{
    private String field;
    private boolean asc = true;
    private Map<String, Object> searchMap;

    public SearchOrder(String field, boolean asc)
    {
        setField(field);
        setAsc(asc);
    }

    public String getField()
    {
        return field;
    }

    public void setField(String field)
    {
        this.field = field;
    }

    public boolean isAsc()
    {
        return asc;
    }

    public void setAsc(boolean asc)
    {
        this.asc = asc;
    }

    public Map<String, Object> getSearchMap()
    {
        return searchMap;
    }

    public void setSearchMap(Map<String, Object> searchMap)
    {
        this.searchMap = searchMap;
    }
}
