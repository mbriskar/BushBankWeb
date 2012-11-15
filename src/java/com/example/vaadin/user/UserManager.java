
package com.example.vaadin.user;

import com.example.vaadin.BushBank;

/**
 *
 * @author Mato
 */
public class UserManager {
     private String userOnline;
    private BushBank app;
     public UserManager(BushBank app) {
         this.app=app;
     }
    
    public boolean checkLogin(String user, String password) {
        if("admin".equals(user) && "admin".equals(password)) {
            userOnline="admin";
            return true;
        }
        return false;
    }
    
    public void logout() {
        userOnline=null;
        app.logout();
    }
    
    public void reportPackageUse(String pack) {
        
    }
    
    public String getUserOnline() {
        return userOnline;
    }


    
}
