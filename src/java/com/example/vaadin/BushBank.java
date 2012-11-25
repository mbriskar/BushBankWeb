/*
 * BushBank.java
 *
 * Created on September 15, 2012, 10:01 PM
 */
package com.example.vaadin;

import com.example.vaadin.components.CorpusDataComponent;
import com.example.vaadin.components.TopSlider;
import com.example.vaadin.components.popups.DataComponentPopupManager;
import com.example.vaadin.corpusManager.NxtCorpusManager;
import com.example.vaadin.user.UserManager;
import com.example.vaadin.windows.LoginWindow;
import com.example.vaadin.windows.MainWindow;
import com.vaadin.Application;
import com.vaadin.terminal.ExternalResource;
import java.util.List;

/**
 *
 * @author Mato
 * @version
 */
public class BushBank extends Application {

    NxtCorpusManager corpus;
    MainWindow mainWindow;
    LoginWindow loginWindow;
    UserManager userManager;
    // STRINGS :
    private static String themeDirectory="mytheme";
    private static String praseXmlPath="NxtFiles";
    private static String observation="ff";
    //
    @Override
    public void init() {
        userManager= new UserManager(this);
        corpus = new NxtCorpusManager(this);
        loginWindow = new LoginWindow(userManager,this);
        setTheme(themeDirectory);
        setMainWindow(loginWindow);

    }
    
    public void sentenceChanged(int intValue) {
        mainWindow.sentenceChanged(intValue);
    }

    public void loginChecked() {
        List<String> availableCorpuses = NxtCorpusManager.getAvailableCorpuses(userManager.getUserOnline());
        if(availableCorpuses != null && availableCorpuses.size() != 0) {
             corpus.changeCorpus(userManager.getUserOnline() + "/" + availableCorpuses.get(0).toString(), observation);
        } else {
            loginWindow.showNotification("Nemáš priradený žiadny corpus");
            userManager.logout();
            return;
        }
        
        if(mainWindow == null) {
            mainWindow = new MainWindow(this, corpus,userManager);
        }

         getMainWindow().open(new ExternalResource(getURL()));
         setMainWindow(mainWindow);
    }

    public void logout() {
        if(getMainWindow() != loginWindow) {
           getMainWindow().open(new ExternalResource(getURL()));
           setMainWindow(loginWindow); 
        }
        
    }
    

    
}
