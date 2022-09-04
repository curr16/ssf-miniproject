package vttp.ssf.miniproject.models;

import org.springframework.security.crypto.bcrypt.BCrypt;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class User {

    private String userName;
    private String email;
    private String password;

    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    //convert from Json to model objects
    public User create(String userName, String email, String password) {
        User u  = new User();
        u.setUserName(userName);
        u.setEmail(email);
        String encrpytedPW = BCrypt.hashpw(password, BCrypt.gensalt());
        u.setPassword(encrpytedPW);
        return u;
    }
    
    //convert model to Json object
    public JsonObject toJson(User u) {
        return Json.createObjectBuilder()
            .add("userName", userName )
            .add("email", email)
            .add("password", password)
            .build();
    }
    
    public static User create(JsonObject existingUser) {
        User ue = new User();
        ue.setUserName(existingUser.getString("userName"));
        ue.setEmail(existingUser.getString("email"));
        ue.setPassword(existingUser.getString("password"));
        return ue;
    }
}
