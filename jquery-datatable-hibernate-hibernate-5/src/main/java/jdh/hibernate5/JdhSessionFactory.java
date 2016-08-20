
package jdh.hibernate5;

import org.hibernate.Session;


public interface JdhSessionFactory
{
    public abstract Session getSession();
}
