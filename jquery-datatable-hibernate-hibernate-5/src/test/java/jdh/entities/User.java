/*
 * ESTE COMPONENTE FUE REALIZADO BAJO LA METODOLOGIA DE DESARROLLO DE 
 * INFORMATICA & TECNOLOGIA Y SE ENCUENTRA PROTEGIDO POR LAS LEYES DE 
 * DERECHOS DE AUTOR.
 */
package jdh.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Alejandro Vivas
 * @version 5.0.0 22/08/2016
 * @since 5.0.0 22/08/2016
 */
@Entity
@Table(name="USER")
public class User
{
    @GeneratedValue
    @Id
    private Long id;
    private String login;
    private String email;

    /**
     * Obtiene el id
     * @author Alejandro Vivas
     * @version 5.0.0 22/08/2016
     * @since 5.0.0 22/08/2016
     * @return el id
     */
    public Long getId()
    {
        return id;
    }

    /**
     * Modifica el id
     * @author Alejandro Vivas
     * @version 5.0.0 22/08/2016
     * @since 5.0.0 22/08/2016
     * @param id El nuevo id
     */
   
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * Obtiene el login
     * @author Alejandro Vivas
     * @version 5.0.0 22/08/2016
     * @since 5.0.0 22/08/2016
     * @return el login
     */
    public String getLogin()
    {
        return login;
    }

    /**
     * Modifica el login
     * @author Alejandro Vivas
     * @version 5.0.0 22/08/2016
     * @since 5.0.0 22/08/2016
     * @param login El nuevo login
     */
    public void setLogin(String login)
    {
        this.login = login;
    }

    /**
     * Obtiene el email
     * @author Alejandro Vivas
     * @version 5.0.0 22/08/2016
     * @since 5.0.0 22/08/2016
     * @return el email
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * Modifica el email
     * @author Alejandro Vivas
     * @version 5.0.0 22/08/2016
     * @since 5.0.0 22/08/2016
     * @param email El nuevo email
     */
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if(obj == null)
        {
            return false;
        }
        
        if( !(obj instanceof User) )
        {
            return true;
        }
        
        User user = (User)obj; 
        
        if(this == user)
        {
            return true;
        }
        
        return this.getEmail().equals(user.getEmail()) && this.getId().equals(user.getId()) && this.getLogin().equals(user.getLogin()) ;
    }
}
