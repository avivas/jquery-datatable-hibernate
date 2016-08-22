package jdh.datatable;

import java.util.Map;

public class DataTableParameters
{
    //TODO AGREGAR PARMETRO DE LIKE 'VALOR%' o '%VALOR'
    private int maxResults;
    private int firstResult;
    private String field;
    private boolean asc = true;
    private Map<String, Object> searchMap;

    public DataTableParameters(String field, boolean asc,int firstResult,int maxResults)
    {
        setField(field);
        setAsc(asc);
        setMaxResults(maxResults);
        setFirstResult(firstResult);
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

    public int getMaxResults()
    {
        return maxResults;
    }

    public void setMaxResults(int maxResults)
    {
        this.maxResults = maxResults;
    }

    public int getFirstResult()
    {
        return firstResult;
    }

    public void setFirstResult(int firstResult)
    {
        this.firstResult = firstResult;
    }    
}
