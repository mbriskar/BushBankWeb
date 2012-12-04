package com.example.vaadin.user;

import com.example.vaadin.BushBank;
import com.example.vaadin.corpusManager.NxtCorpusManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mato
 */
public class UserManager {
    public static final String VALUES_SPLIT_CHAR = ":";

    private String userOnline;
    private BushBank app;

    public UserManager(BushBank app) {
        this.app = app;
    }

    public boolean checkLogin(String user, String password) {

        if (getUsers().contains(user) && getValues(user).get("password") != null && getValues(user).get("password").equals(password)) {
            userOnline = user;
            return true;
        }
        return false;
    }

    public void logout() {
        userOnline = null;
        app.logout();
    }

    public void reportPackageUse(String pack) {
        System.out.println(userOnline + ":" + pack);
    }

    public Set<String> getUsers() {
        //if needed, make a future object responsible for all the files
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



    public Map<String, String> getValues(String user) {
        Map<String,String> map = new HashMap<String,String>();
        try {
            FileInputStream fstream = new FileInputStream(NxtCorpusManager.prefix + "/" + user + "/" + "values.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String line;
            
            while ((line = br.readLine()) != null) {
                String[] splited = line.split(VALUES_SPLIT_CHAR);
                if(splited.length ==2) {
                    map.put(splited[0], splited[1]);
                }
            }
            

        } catch (IOException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);

        }
        return map;
    }

    public String getUserOnline() {
        return userOnline;
    }
}
