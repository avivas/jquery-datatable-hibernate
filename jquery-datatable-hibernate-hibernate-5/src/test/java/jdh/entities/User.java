package jdh.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="USER")
public class User
{
    @GeneratedValue
    @Id
    private Long id;
    private String login;
    private String email;

    public Long getId()
    {
        return id;
    }
   
    public void setId(Long id)
    {
        this.id = id;
    }

    public String getLogin()
    {
        return login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public String getEmail()
    {
        return email;
    }

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
