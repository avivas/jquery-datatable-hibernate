
package jdh.hibernate5;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import jdh.datatable.SearchOrder;

import org.hibernate.Session;
import org.hibernate.query.Query;


public class JdhHibernateDao<T_ENTITY>
{
    private Class<T_ENTITY> clazz;
    private String allQuery;
    private String entityAlias;
    private String allQueryWithRelations;
    
    private JdhSessionFactory sessionFactory; 


    public JdhHibernateDao(Class<T_ENTITY> clazz)
    {
        this.entityAlias = "_" + clazz.getSimpleName();
        this.allQuery = "from " + clazz.getName() + " as " + entityAlias;
        this.clazz = clazz;
    }

    public List<T_ENTITY> search(int maxResults, int firstResult, SearchOrder searchOrder)
    {
        return search(allQuery,maxResults, firstResult,searchOrder);
    }
    
    public List<T_ENTITY> searchWithRelations(int maxResults, int firstResult, SearchOrder searchOrder)
    {
        createQueryWithRelations();
        return search(allQueryWithRelations,maxResults, firstResult,searchOrder);
    }

    private List<T_ENTITY> search(String baseQuery,int maxResults, int firstResult, SearchOrder searchOrder)
    {
        Session session = this.getCurrentSession();

        StringBuilder stringBuilder = new StringBuilder(baseQuery);
        
        if ((searchOrder.getSearchMap() != null) && !searchOrder.getSearchMap().isEmpty())
        {
            stringBuilder.append(" where ");
            Iterator<String> fields = searchOrder.getSearchMap().keySet().iterator();
            
            while (fields.hasNext())
            {
                String field = fields.next();
                if (searchOrder.getSearchMap().get(field) instanceof String)
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

                stringBuilder.append(field);
                stringBuilder.append("_PARAM");

                if (fields.hasNext())
                {
                    stringBuilder.append(" and ");
                }
            }
        }

        if (searchOrder.getField() != null)
        {
            stringBuilder.append(" order by ");
            stringBuilder.append(this.entityAlias);
            stringBuilder.append(".");
            stringBuilder.append(searchOrder.getField());
            stringBuilder.append(" ");
            stringBuilder.append((searchOrder.isAsc() ? "asc" : "desc"));
        }
        
        Query<T_ENTITY> query = session.createQuery(stringBuilder.toString(),getClazz());
        if ((searchOrder.getSearchMap() != null) && !searchOrder.getSearchMap().isEmpty())
        {
            for (String field : searchOrder.getSearchMap().keySet())
            {
                String parameterName = "_" + field + "_PARAM";
                Object value = searchOrder.getSearchMap().get(field);
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
        
        if (maxResults > 0)
        {
            query.setMaxResults(maxResults);
        }

        if (firstResult > -1)
        {
            query.setFirstResult(firstResult);
        }

        List<T_ENTITY> result = query.getResultList();

        return result;
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
