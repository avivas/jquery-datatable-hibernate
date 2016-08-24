
package jdh.datatable;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;


public class ProcessJqueryDataTableRequestTest
{
    @Test
    public void getDataTableParametersEmptyParametersWithoutMapNameFieldValue()
    {
        Map<String, String[]> parameters = new HashMap<String, String[]>();
                
        DataTableParameters dataTableParameters = ProcessJqueryDataTableRequest.getDataTableParameters(parameters, null);
        
        Assert.assertEquals(dataTableParameters.getFirstResult(), -1);
        Assert.assertEquals(dataTableParameters.getMaxResults(), -1);
        Assert.assertNull(dataTableParameters.getField());
        Assert.assertFalse(dataTableParameters.isAsc());
        Assert.assertTrue(dataTableParameters.getSearchMap().isEmpty());
    }
    
    @Test
    public void getDataTableParametersWithoutMapNameFieldValue()
    {
        Map<String, String[]> parameters = createDefaultMapParameters();
                
        DataTableParameters dataTableParameters = ProcessJqueryDataTableRequest.getDataTableParameters(parameters, null);
        
        Assert.assertEquals(dataTableParameters.getFirstResult(), 0);
        Assert.assertEquals(dataTableParameters.getMaxResults(), 10);
        Assert.assertEquals(dataTableParameters.getField(),"name");
        Assert.assertTrue(dataTableParameters.isAsc());
        Assert.assertTrue(dataTableParameters.getSearchMap().isEmpty());
    }
    
    @Test
    public void getDataTableParametersSearchNameWithoutMapNameFieldValue()
    {
        Map<String, String[]> parameters = createDefaultMapParameters();
        parameters.put("columns[1][search][value]", new String[]{"gr"});
                
        DataTableParameters dataTableParameters = ProcessJqueryDataTableRequest.getDataTableParameters(parameters, null);
        
        Assert.assertEquals(dataTableParameters.getFirstResult(), 0);
        Assert.assertEquals(dataTableParameters.getMaxResults(), 10);
        Assert.assertEquals(dataTableParameters.getField(),"name");
        Assert.assertTrue(dataTableParameters.isAsc());
        Assert.assertTrue(dataTableParameters.getSearchMap().containsKey("group"));
    }
    
    private class UserMapNameFieldValue implements MapNameFieldValue
    {
        private Map<String, String> mapFieldName = new HashMap<String, String>();
        
        public UserMapNameFieldValue()
        {
            mapFieldName.put("nameParameter", "name");
            mapFieldName.put("groupParameter", "group");
            mapFieldName.put("descriptionParameter", "description");
            mapFieldName.put("valueParameter", "value");
            mapFieldName.put("typeParameter", "type");
        }
        
        @Override
        public Object getSearchValue(String nameField, Object value)
        {
            return value;
        }
        
        @Override
        public String getEntityFieldName(String nameFieldRequest)
        {
            return mapFieldName.get(nameFieldRequest);
        }
    };
    
    @Test
    public void getDataTableParametersOrderInjection()
    {
        Map<String, String[]> parameters = createDefaultMapParameters();
        parameters.put("columns[0][data]", new String[]{"delete from User"});
        parameters.put("columns[1][data]", new String[]{"groupParameter"});       
        parameters.put("columns[2][data]", new String[]{"descriptionParameter"});
        parameters.put("columns[3][data]", new String[]{"valueParameter"});
        parameters.put("columns[4][data]", new String[]{"typeParameter"});
        
        UserMapNameFieldValue mapNameFieldValue = new UserMapNameFieldValue();
                
        DataTableParameters dataTableParameters = ProcessJqueryDataTableRequest.getDataTableParameters(parameters, mapNameFieldValue);
        
        Assert.assertNull(dataTableParameters.getField());
    }
    
    @Test
    public void getDataTableParametersSearchOrderInjection()
    {
        Map<String, String[]> parameters = createDefaultMapParameters();
        parameters.put("columns[0][data]", new String[]{"delete from User"});
        parameters.put("columns[1][data]", new String[]{"delete from User"});       
        parameters.put("columns[2][data]", new String[]{"descriptionParameter"});
        parameters.put("columns[3][data]", new String[]{"valueParameter"});
        parameters.put("columns[4][data]", new String[]{"typeParameter"});
        parameters.put("columns[1][search][value]", new String[]{"update User set login='qaz'"});
        
        UserMapNameFieldValue mapNameFieldValue = new UserMapNameFieldValue();
                
        DataTableParameters dataTableParameters = ProcessJqueryDataTableRequest.getDataTableParameters(parameters, mapNameFieldValue);
        
        Assert.assertNull(dataTableParameters.getField());
        Assert.assertTrue(dataTableParameters.getSearchMap().isEmpty());
    }
    
    @Test
    public void getDataTableParametersDraw()
    {
        Map<String, String[]> parameters = createDefaultMapParameters();
        DataTableParameters dataTableParameters = ProcessJqueryDataTableRequest.getDataTableParameters(parameters, null);
        Assert.assertEquals(1, dataTableParameters.getDraw());
    }
    
    private static  Map<String, String[]> createDefaultMapParameters()
    {
        Map<String, String[]> parameters = new HashMap<String, String[]>();
        parameters.put("draw", new String[]{"1"});
        parameters.put("columns[0][data]", new String[]{"name"});
        parameters.put("columns[0][name]", new String[]{""});
        parameters.put("columns[0][searchable]", new String[]{"true"});
        parameters.put("columns[0][orderable]", new String[]{"true"});
        parameters.put("columns[0][search][value]", new String[]{""});
        parameters.put("columns[0][search][regex]", new String[]{"false"});
        
        parameters.put("columns[1][data]", new String[]{"group"});
        parameters.put("columns[1][name]", new String[]{"1"});
        parameters.put("columns[1][searchable]", new String[]{"true"});
        parameters.put("columns[1][orderable]", new String[]{"true"});
        parameters.put("columns[1][search][value]", new String[]{""});
        parameters.put("columns[1][search][regex]", new String[]{""});
        
        parameters.put("columns[2][data]", new String[]{"description"});
        parameters.put("columns[2][name]", new String[]{""});
        parameters.put("columns[2][searchable]", new String[]{"true"});
        parameters.put("columns[2][orderable]", new String[]{"true"});
        parameters.put("columns[2][search][value]", new String[]{""});
        parameters.put("columns[2][search][regex]", new String[]{"false"});
        
        parameters.put("columns[3][data]", new String[]{"value"});
        parameters.put("columns[3][name]", new String[]{""});
        parameters.put("columns[3][searchable]", new String[]{"true"});
        parameters.put("columns[3][orderable]", new String[]{"true"});
        parameters.put("columns[3][search][value]", new String[]{""});        
        parameters.put("columns[3][search][regex]", new String[]{"false"});
        
        parameters.put("columns[4][data]", new String[]{"type"});
        parameters.put("columns[4][name]", new String[]{""});
        parameters.put("columns[4][searchable]", new String[]{"true"});
        parameters.put("columns[4][orderable]", new String[]{"true"});
        parameters.put("columns[4][search][value]", new String[]{""});
        parameters.put("columns[4][search][regex]", new String[]{"false"});
        
        parameters.put("columns[5][data]", new String[]{""});
        parameters.put("columns[5][name]", new String[]{""});
        parameters.put("columns[5][searchable]", new String[]{"true"});
        parameters.put("columns[5][orderable]", new String[]{"false"});
        parameters.put("columns[5][search][value]", new String[]{""});
        parameters.put("columns[5][search][regex]", new String[]{"false"});
        
        parameters.put("order[0][column]", new String[]{"0"});
        parameters.put("order[0][dir]", new String[]{"asc"});
        parameters.put("start", new String[]{"0"});
        parameters.put("length", new String[]{"10"});
        parameters.put("search[value]", new String[]{""});
        parameters.put("search[regex]", new String[]{"false"});
        parameters.put("_", new String[]{"1471878720664"});
        
        return parameters;
    }
}
