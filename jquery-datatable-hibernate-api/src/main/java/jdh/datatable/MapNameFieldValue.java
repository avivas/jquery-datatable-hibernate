package jdh.datatable;


public interface MapNameFieldValue
{
    public abstract String getEntityFieldName(String nameFieldRequest);

    public abstract Object getSearchValue(String nameField,Object value);
}
