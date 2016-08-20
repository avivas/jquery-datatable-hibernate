package jdh.datatable;

import java.util.Map;
import java.util.TreeMap;

public final class ProcessJqueryDataTableRequest
{
    private static final String DATA2 = "][data]";
    private static final String COLUMNS = "columns[";
    private static final String DATA = "[data]";
    private static final String ASC = "asc";
    private static final String SEARCH_VALUE = "[search][value]";
    private static final String PARAMETER_NAME_COLUMN_ORDER_INDEX = "order[0][column]";
    private static final String PARAMETER_NAME_ORDER = "order[0][dir]";

    public static SearchOrder getSearchOrder(Map<String, String[]> parameters, MapNameFieldValue mapNameFieldValue)
    {
        SearchOrder searchOrder = getOrder(parameters, mapNameFieldValue);
        Map<String, Object> searchMap = getSearchMap(parameters, mapNameFieldValue);
        searchOrder.setSearchMap(searchMap);
        return searchOrder;
    }

    private static SearchOrder getOrder(Map<String, String[]> parameters, MapNameFieldValue mapNameFieldValue)
    {
        int columnOrderIndex = Integer.parseInt(parameters.get(PARAMETER_NAME_COLUMN_ORDER_INDEX)[0]);
        String columnOrderName = parameters.get(COLUMNS + columnOrderIndex + DATA2)[0];

        String order = parameters.get(PARAMETER_NAME_ORDER)[0];

        if (mapNameFieldValue == null)
        {
            return new SearchOrder(columnOrderName, order.equals(ASC));
        }
        else
        {
            return new SearchOrder(mapNameFieldValue.getEntityFieldName(columnOrderName), order.equals(ASC));
        }
    }

    private static Map<String, Object> getSearchMap(Map<String, String[]> parameters, MapNameFieldValue mapNameFieldValue)
    {
        Map<String, Object> filter = new TreeMap<String, Object>();

        for (String name : parameters.keySet())
        {
            if (name.endsWith(SEARCH_VALUE) && parameters.get(name)[0].length() > 0)
            {
                String parameterNameFieldName = name.substring(0, name.indexOf(SEARCH_VALUE)) + DATA;
                String fieldName = parameters.get(parameterNameFieldName)[0];
                Object value;
                if (mapNameFieldValue == null)
                {
                    value = parameters.get(name)[0];
                }
                else
                {
                    value = mapNameFieldValue.getSearchValue(fieldName, parameters.get(name)[0]);
                }

                filter.put(fieldName, value);
            }
        }
        return filter;
    }
}
