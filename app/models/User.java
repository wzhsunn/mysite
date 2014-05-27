package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import models.utils.Hash;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.data.validation.Constraints;

@Entity 
@Table(name="account")
public class User extends Model {
	private static final long serialVersionUID = 1L;

	@Id
    public Long id;
	
    @Required
    public String username;
    
    @Constraints.Required(message="密码不能为空")
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
    public static User authenticate(String username, String clearPassword) {
    	  // get the user with email only to keep the salt password
        User user = find.where().eq("username", username).findUnique();
        if (user != null) {
            // get the hash password from the salt + clear password
            if (Hash.checkPassword(clearPassword, user.password)) {
                return user;
            }
        }
        return null;
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
