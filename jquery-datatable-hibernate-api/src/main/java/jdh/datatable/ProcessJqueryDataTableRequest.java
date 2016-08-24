package jdh.datatable;

import java.util.Map;
import java.util.TreeMap;

public final class ProcessJqueryDataTableRequest
{
    private static final String START = "start";
    private static final String LENGTH = "length";
    private static final String DATA2 = "][data]";
    private static final String COLUMNS = "columns[";
    private static final String DATA = "[data]";
    private static final String ASC = "asc";
    private static final String SEARCH_VALUE = "[search][value]";
    private static final String PARAMETER_NAME_COLUMN_ORDER_INDEX = "order[0][column]";
    private static final String PARAMETER_NAME_ORDER = "order[0][dir]";

    public static DataTableParameters getDataTableParameters(Map<String, String[]> parameters, MapNameFieldValue mapNameFieldValue)
    {
        DataTableParameters dataTableParameters = getOrder(parameters, mapNameFieldValue);
        Map<String, Object> searchMap = getSearchMap(parameters, mapNameFieldValue);
        dataTableParameters.setSearchMap(searchMap);
        
        String draw = getDraw(parameters);
        dataTableParameters.setDraw(draw);
        
        return dataTableParameters;
    }

    private static DataTableParameters getOrder(Map<String, String[]> parameters, MapNameFieldValue mapNameFieldValue)
    {
        int columnOrderIndex = getColumnOrderIndex(parameters);
        String columnOrderName = getColumnOrderName(parameters, columnOrderIndex);
        String order = getOrder(parameters);
        Boolean asc = (order == null) ? false : order.equals(ASC);
        int start = getStart(parameters);
        int length = getLength(parameters);

        if (mapNameFieldValue == null)
        {
            return new DataTableParameters(columnOrderName, asc, start, length);
        }
        else
        {
            return new DataTableParameters(mapNameFieldValue.getEntityFieldName(columnOrderName), asc, start, length);
        }
    }

    private static int getStart(Map<String, String[]> parameters)
    {
        String[] arrayStart = parameters.get(START);
        if(arrayStart == null)
        {
            return -1;
        }
        
        String start = parameters.get(START)[0];
        if (start != null && start.trim().length() == 0)
        {
            return -1;
        }
        return Integer.parseInt(start);
    }

    private static int getLength(Map<String, String[]> parameters)
    {
        String[] arrayLength = parameters.get(LENGTH);
        if(arrayLength == null)
        {
            return -1;
        }
        
        String length = arrayLength[0];
        if (length != null && length.trim().length() == 0)
        {
            return -1;
        }
        return Integer.parseInt(length);
    }

    private static Map<String, Object> getSearchMap(Map<String, String[]> parameters, MapNameFieldValue mapNameFieldValue)
    {
        Map<String, Object> searchMap = new TreeMap<String, Object>();

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
                    searchMap.put(fieldName, value);
                }
                else
                {
                    value = mapNameFieldValue.getSearchValue(fieldName, parameters.get(name)[0]);
                    searchMap.put(mapNameFieldValue.getEntityFieldName(fieldName), value);
                }
            }
        }
        return searchMap;
    }

    private static String getOrder(Map<String, String[]> parameters)
    {
        String[] arrayOrder = parameters.get(PARAMETER_NAME_ORDER);
        if(arrayOrder == null)
        {
            return null;
        }
        
        String order = arrayOrder[0];
        if (order != null && order.trim().length() == 0)
        {
            return null;
        }
        return order;
    }

    private static int getColumnOrderIndex(Map<String, String[]> parameters)
    {
        int columnOrderIndex = -1;
        String[] arrayColumnOrderIndex = parameters.get(PARAMETER_NAME_COLUMN_ORDER_INDEX);
        if (arrayColumnOrderIndex != null)
        {
            String stringColumnOrderIndex = arrayColumnOrderIndex[0];
            if (stringColumnOrderIndex != null && stringColumnOrderIndex.trim().length() > 0)
            {
                columnOrderIndex = Integer.parseInt(parameters.get(PARAMETER_NAME_COLUMN_ORDER_INDEX)[0]);
            }
        }
        return columnOrderIndex;
    }

    private static String getColumnOrderName(Map<String, String[]> parameters, int columnOrderIndex)
    {
        if (columnOrderIndex > -1)
        {
            return parameters.get(COLUMNS + columnOrderIndex + DATA2)[0];
        }
        return null;
    }
    
    private static String getDraw(Map<String, String[]> parameters)
    {
        String [] arrayDraw = parameters.get("draw");
        if(arrayDraw != null )
        {
            return arrayDraw[0];
        }
        return null;
    }
}
