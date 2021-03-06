
package jdh.hibernate5;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.hibernate.Session;
import org.hibernate.query.Query;

import jdh.datatable.DataTableParameters;


public class JdhHibernateDao<T_ENTITY>
{
    private Class<T_ENTITY> clazz;
    private String allQuery;
    private String countAllQuery;
    private String entityAlias;
    private String countEntityAlias;
    private String allQueryWithRelations;
    private JdhSessionFactory sessionFactory; 

    public JdhHibernateDao(Class<T_ENTITY> clazz)
    {
        this.entityAlias = "_" + clazz.getSimpleName();
        this.countEntityAlias = entityAlias;
        this.allQuery = "from " + clazz.getName() + " as " + entityAlias;
        this.countAllQuery = "select count(" + this.entityAlias + ") " + allQuery;
        this.clazz = clazz;
    }
    
    public JdhHibernateDao(String entityAlias,String allQuery,Class<T_ENTITY> clazz)
    {
        this.entityAlias = entityAlias;
        this.countEntityAlias = entityAlias;
        this.allQuery = allQuery;
        this.countAllQuery = "select count(" + this.countEntityAlias + ") " + allQuery;
        this.clazz = clazz;
    }
    
    public JdhHibernateDao(String entityAlias,String countEntityAlias,String allQuery,Class<T_ENTITY> clazz)
    {
        this.entityAlias = entityAlias;
        this.countEntityAlias = countEntityAlias;
        this.allQuery = allQuery;
        this.countAllQuery = "select count(" + this.countEntityAlias + ") " + allQuery;
        this.clazz = clazz;
    }

    public List<T_ENTITY> search(DataTableParameters dataTableParameters)
    {
        return search(allQuery,dataTableParameters);
    }
    
    public List<T_ENTITY> searchWithRelations(DataTableParameters dataTableParameters)
    {
        createQueryWithRelations();
        return search(allQueryWithRelations,dataTableParameters);
    }
    
    public Number count(DataTableParameters dataTableParameters)
    {
    	Session session = this.getCurrentSession();
    	Query<Number> query = createQuery(this.countAllQuery,dataTableParameters,session,Number.class,false);
    	return query.getSingleResult();
    }

    private List<T_ENTITY> search(String baseQuery,DataTableParameters dataTableParameters)
    {
        Session session = this.getCurrentSession();
        
        Query<T_ENTITY> query = createQuery(baseQuery,dataTableParameters,session,getClazz(),true);
        
        if (dataTableParameters.getMaxResults() > 0)
        {
            query.setMaxResults(dataTableParameters.getMaxResults());
        }

        if (dataTableParameters.getFirstResult() > -1)
        {
            query.setFirstResult(dataTableParameters.getFirstResult());
        }

        List<T_ENTITY> result = query.getResultList();

        return result;
    }
    
    private <TYPE> Query<TYPE> createQuery(String baseQuery,DataTableParameters dataTableParameters,Session session,Class<TYPE> clazz,boolean order)
    {
    	String stringQuery = createHqlQuery(baseQuery,dataTableParameters,order);
    	Query<TYPE> query = session.createQuery(stringQuery,clazz);
        if ((dataTableParameters.getSearchMap() != null) && !dataTableParameters.getSearchMap().isEmpty())
        {
            for (String field : dataTableParameters.getSearchMap().keySet())
            {
                String parameterName = "_" + field.replace('.', '_') + "_PARAM";
                Object value = dataTableParameters.getSearchMap().get(field);
                if (value instanceof String)
                {
                    query.setParameter(parameterName, "%" + value + "%");
                }
                else
                {
                    query.setParameter(parameterName, value);
                }
            }
        }
        return query;
    }
    
    private String createHqlQuery(String baseQuery,DataTableParameters dataTableParameters,boolean order)
    {
    	StringBuilder stringBuilder = new StringBuilder(baseQuery);
        
        if ((dataTableParameters.getSearchMap() != null) && !dataTableParameters.getSearchMap().isEmpty())
        {
            stringBuilder.append(" where ");
            Iterator<String> fields = dataTableParameters.getSearchMap().keySet().iterator();
            
            while (fields.hasNext())
            {
                String field = fields.next();
                if (dataTableParameters.getSearchMap().get(field) instanceof String)
                {
                    stringBuilder.append("str(");
                    stringBuilder.append(this.entityAlias);
                    stringBuilder.append(".");
                    stringBuilder.append(field);
                    stringBuilder.append(") like :_");
                }
                else
                {
                    stringBuilder.append(this.entityAlias);
                    stringBuilder.append(".");
                    stringBuilder.append(field);
                    stringBuilder.append(" = :_");
                }

                stringBuilder.append(field.replace('.', '_'));
                stringBuilder.append("_PARAM");

                if (fields.hasNext())
                {
                    stringBuilder.append(" and ");
                }
            }
        }
        
        if ((dataTableParameters.getSearchMap() == null) || dataTableParameters.getSearchMap().isEmpty() && !dataTableParameters.getCustomFilter().isEmpty() )
        {
            stringBuilder.append(" where ");
        }
        else
        {
        	stringBuilder.append(" ");
        }
        
        
        if (    (dataTableParameters.getSearchMap() != null) 
        	 && !dataTableParameters.getSearchMap().isEmpty() 
        	 && (dataTableParameters.getCustomFilter() != null )
        	 && !dataTableParameters.getCustomFilter().isEmpty() )
        {
            stringBuilder.append(" and ");
        }
        
        Iterator<String> customFilterIterator = dataTableParameters.getCustomFilter().keySet().iterator();
        while(customFilterIterator.hasNext())
        {
        	String field = customFilterIterator.next();
        	stringBuilder.append(field);
        	stringBuilder.append( dataTableParameters.getCustomFilter().get(field) );
        	
        	if(customFilterIterator.hasNext())
        	{
        		 stringBuilder.append(" and ");
        	}
        }

        if (dataTableParameters.getField() != null && order)
        {
            stringBuilder.append(" order by ");
            stringBuilder.append(this.entityAlias);
            stringBuilder.append(".");
            stringBuilder.append(dataTableParameters.getField());
            stringBuilder.append(" ");
            stringBuilder.append((dataTableParameters.isAsc() ? "asc" : "desc"));
        }
        
        return stringBuilder.toString();
    }

    private void createQueryWithRelations()
    {        
        if (this.allQueryWithRelations == null)
        {
            this.allQueryWithRelations = "from " + getClazz().getName() + " " + this.entityAlias;
            EntityManagerFactory entityManagerFactory = this.getCurrentSession().getSessionFactory();            
            Metamodel metamodel = entityManagerFactory.getMetamodel();
            EntityType<T_ENTITY> entityType = metamodel.entity(getClazz());
            
            for( Attribute<? super T_ENTITY, ?> attribute : entityType.getAttributes())
            {
                if( attribute.isAssociation() )
                {
                    this.allQueryWithRelations += "\n left join fetch " + this.entityAlias + "." + attribute.getName();
                }
            }
        }
    }

    public Class<T_ENTITY> getClazz()
    {
        return clazz;
    }

    public void setSessionFactory(JdhSessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    public JdhSessionFactory getSessionFactory()
    {
        return sessionFactory;
    }
    
    private Session getCurrentSession()
    {
        return getSessionFactory().getSession();
    }
}
