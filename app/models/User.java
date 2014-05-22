package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity 
@Table(name="account")
public class User extends Model {
	private static final long serialVersionUID = 1L;

	@Id
    public Long id;
	
    @Constraints.Required(message="用户名不能为空")
    @Formats.NonEmpty
    public String username;
    
    @Constraints.Required(message="密码不能为空")
    @Formats.NonEmpty
    public String password;
    
    public String email;
    
    public String phone;
    
    public User(){
    	
    }
    // -- Queries
    
    public static Model.Finder<String,User> find = new Model.Finder<String,User>(String.class, User.class);
    
    /**
     * Retrieve all users.
     */
    public static List<User> findAll() {
        return find.all();
    }

    /**
     * Retrieve a User from email.
     */
    public static User findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }
    
    public static User findByUsername(String username){
    	return find.where().eq("username", username).findUnique();
    }
    /**
     * Authenticate a User.
     */
    public static User authenticate(String username, String password) {
        return find.where()
            .eq("username", username)
            .eq("password", password)
            .findUnique();
    }
    
    // --
    
    public String toString() {
        return "username[" + username + ", password[" + password +"]";
    }
    
    public String validate() {
        if(User.authenticate(username, password) == null) {
            return "用户名或密码错误。";
        }
        return null;
    }
}
