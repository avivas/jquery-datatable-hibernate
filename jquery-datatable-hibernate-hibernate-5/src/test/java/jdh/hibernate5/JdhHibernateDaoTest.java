/*
 * ESTE COMPONENTE FUE REALIZADO BAJO LA METODOLOGIA DE DESARROLLO DE 
 * INFORMATICA & TECNOLOGIA Y SE ENCUENTRA PROTEGIDO POR LAS LEYES DE 
 * DERECHOS DE AUTOR.
 */
package jdh.hibernate5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.internal.SessionFactoryImpl;
import org.hsqldb.Database;
import org.hsqldb.Server;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import jdh.datatable.DataTableParameters;
import jdh.datatable.ProcessJqueryDataTableRequest;
import jdh.entities.User;


public class JdhHibernateDaoTest
{
    private static Server server;
    private static EntityManagerFactory entityManagerFactory;
    private static List<User> users = new ArrayList<>();

    @BeforeClass
    public static void init()
    {
        server = new Server();
        server.start();
        setEntityManagerFactory(Persistence.createEntityManagerFactory("JDH"));
        
        User user1 = new User();
        user1.setEmail("d@a");
        user1.setLogin("a");
        getUsers().add(user1);
        
        User user2 = new User();
        user2.setEmail("c@b");
        user2.setLogin("b");
        getUsers().add(user2);
        
        User user3 = new User();
        user3.setEmail("b@c");
        user3.setLogin("c");
        getUsers().add(user3);
        
        User user4 = new User();
        user4.setEmail("a@c");
        user4.setLogin("d");
        getUsers().add(user4);
        
        EntityManager entityManager = getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.persist(user4);
        entityManager.getTransaction().commit();
        entityManager.close();
    }    
    
    @Test
    public void emptyParameters()
    {
        JdhHibernateDao<User> dao = new JdhHibernateDao<>(User.class);
        dao.setSessionFactory( () -> ((SessionFactoryImpl)entityManagerFactory).openSession() );
        
        Map<String, String[]> parameters = new HashMap<>();
        
        DataTableParameters dataTableParameters = ProcessJqueryDataTableRequest.getDataTableParameters(parameters, null);
        
        List<User> users =  dao.search(dataTableParameters);
        
        Assert.assertEquals(users.size(), getUsers().size());
        Assert.assertArrayEquals(users.toArray(new User[users.size()]), getUsers().toArray(new User[getUsers().size()]));
        
        dao.getSessionFactory().getSession().close();
    }

    @Test
    public void orderByLoginAsc()
    {
        JdhHibernateDao<User> dao = new JdhHibernateDao<>(User.class);
        dao.setSessionFactory( () -> ((SessionFactoryImpl)entityManagerFactory).openSession() );
        
        Map<String, String[]> parameters = createDefaultMapParameters();
        
        DataTableParameters dataTableParameters = ProcessJqueryDataTableRequest.getDataTableParameters(parameters, null);
        
        List<User> users =  dao.search(dataTableParameters);
        
        Assert.assertEquals(users.size(), getUsers().size());
        Assert.assertArrayEquals(users.toArray(new User[users.size()]), getUsers().toArray(new User[getUsers().size()]));
        
        dao.getSessionFactory().getSession().close();
    }
    
    @Test
    public void orderByLoginDesc()
    {
        JdhHibernateDao<User> dao = new JdhHibernateDao<>(User.class);
        dao.setSessionFactory( () -> ((SessionFactoryImpl)entityManagerFactory).openSession() );
        
        Map<String, String[]> parameters = createDefaultMapParameters();
        parameters.put("order[0][dir]", new String[]{"desc"});
        
        DataTableParameters dataTableParameters = ProcessJqueryDataTableRequest.getDataTableParameters(parameters, null);
        
        List<User> users =  dao.search(dataTableParameters);
        
        List<User> userReverseOrder = new ArrayList<>(getUsers());
        Collections.reverse(userReverseOrder);
        
        Assert.assertEquals(users.size(), getUsers().size());
        Assert.assertArrayEquals(users.toArray(new User[users.size()]), userReverseOrder.toArray(new User[getUsers().size()]));
        
        dao.getSessionFactory().getSession().close();
    }
    
    @Test
    public void orderByEmail()
    {
        JdhHibernateDao<User> dao = new JdhHibernateDao<>(User.class);
        dao.setSessionFactory( () -> ((SessionFactoryImpl)entityManagerFactory).openSession() );
        
        Map<String, String[]> parameters = createDefaultMapParameters();
        parameters.put("order[0][column]", new String[]{"2"});
        
        DataTableParameters dataTableParameters = ProcessJqueryDataTableRequest.getDataTableParameters(parameters, null);
        
        List<User> users =  dao.search(dataTableParameters);
        
        Assert.assertEquals(users.size(), getUsers().size());
        List<User> userReverseOrder = new ArrayList<>(getUsers());
        Collections.reverse(userReverseOrder);
        Assert.assertArrayEquals(users.toArray(new User[users.size()]),userReverseOrder.toArray(new User[getUsers().size()]));
        
        dao.getSessionFactory().getSession().close();
    }
    
    @Test
    public void orderByEmailPaging()
    {
        JdhHibernateDao<User> dao = new JdhHibernateDao<>(User.class);
        dao.setSessionFactory( () -> ((SessionFactoryImpl)entityManagerFactory).openSession() );
        
        Map<String, String[]> parameters = createDefaultMapParameters();
        int lenght = 2;
        parameters.put("order[0][column]", new String[]{"0"});        
        parameters.put("length", new String[]{"" + lenght});
        
        for(int i = 0; i < 2; i++)
        {
            parameters.put("start", new String[]{"" + i*lenght});
            DataTableParameters dataTableParameters = ProcessJqueryDataTableRequest.getDataTableParameters(parameters, null);
            List<User> users =  dao.search(dataTableParameters);
            List<User> expecteds = getUsers().subList(i*lenght, (i+1)* lenght);
            
            Assert.assertEquals(users.size(),2);
            Assert.assertArrayEquals(users.toArray(new User[users.size()]),expecteds.toArray(new User[lenght]));
        }
        
        dao.getSessionFactory().getSession().close();
    }
    
    @AfterClass
    public static void shutdown()
    {
        server.shutdownCatalogs(Database.CLOSEMODE_NORMAL);
    }
    
    private static  Map<String, String[]> createDefaultMapParameters()
    {
        Map<String, String[]> parameters = new HashMap<String, String[]>();
        parameters.put("draw", new String[]{"1"});
        parameters.put("columns[0][data]", new String[]{"id"});
        parameters.put("columns[0][name]", new String[]{""});
        parameters.put("columns[0][searchable]", new String[]{"true"});
        parameters.put("columns[0][orderable]", new String[]{"true"});
        parameters.put("columns[0][search][value]", new String[]{""});
        parameters.put("columns[0][search][regex]", new String[]{"false"});
        
        parameters.put("columns[1][data]", new String[]{"login"});
        parameters.put("columns[1][name]", new String[]{"1"});
        parameters.put("columns[1][searchable]", new String[]{"true"});
        parameters.put("columns[1][orderable]", new String[]{"true"});
        parameters.put("columns[1][search][value]", new String[]{""});
        parameters.put("columns[1][search][regex]", new String[]{""});
        
        parameters.put("columns[2][data]", new String[]{"email"});
        parameters.put("columns[2][name]", new String[]{""});
        parameters.put("columns[2][searchable]", new String[]{"true"});
        parameters.put("columns[2][orderable]", new String[]{"true"});
        parameters.put("columns[2][search][value]", new String[]{""});
        parameters.put("columns[2][search][regex]", new String[]{"false"});
        
        parameters.put("order[0][column]", new String[]{"1"});
        parameters.put("order[0][dir]", new String[]{"asc"});
        parameters.put("start", new String[]{"0"});
        parameters.put("length", new String[]{"10"});
        parameters.put("search[value]", new String[]{""});
        parameters.put("search[regex]", new String[]{"false"});
        parameters.put("_", new String[]{"1471878720664"});
        
        return parameters;
    }

    public static void setUsers(List<User> users)
    {
        JdhHibernateDaoTest.users = users;
    }

    public static List<User> getUsers()
    {
        return users;
    }

    public static void setEntityManagerFactory(EntityManagerFactory entityManagerFactory)
    {
        JdhHibernateDaoTest.entityManagerFactory = entityManagerFactory;
    }

    public static EntityManagerFactory getEntityManagerFactory()
    {
        return entityManagerFactory;
    }
}
