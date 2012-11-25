
package com.example.vaadin.user;

import com.example.vaadin.BushBank;
import com.example.vaadin.corpusManager.NxtCorpusManager;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        if(getUsers().contains(user)) {
            userOnline=user;
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
    
    public Set<String> getUsers() {
        File dir = new File(NxtCorpusManager.prefix); 
        File[] subDirs = dir.listFiles(new FileFilter() {  
            @Override
        public boolean accept(File pathname) {  
            return pathname.isDirectory();  
         }  
        }); 
        Set<String> subDirNames = new HashSet<String>();
        for (File subDir : subDirs) {  
        subDirNames.add(subDir.getName());
        System.out.println(subDir.getName());
        }  
        
        return subDirNames;
    }
    
    public String getUserOnline() {
        return userOnline;
    }


    
}
