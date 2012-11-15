/*
 * BushBank.java
 *
 * Created on September 15, 2012, 10:01 PM
 */
package com.example.vaadin;

import com.example.vaadin.components.CorpusDataComponent;
import com.example.vaadin.components.TopSlider;
import com.example.vaadin.corpusManager.NxtCorpusManager;
import com.example.vaadin.user.UserManager;
import com.example.vaadin.windows.LoginWindow;
import com.example.vaadin.windows.MainWindow;
import com.vaadin.Application;
import com.vaadin.terminal.ExternalResource;

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
    private static String praseXmlPath="NxtFiles/prase.xml";
    private static String observation="ff";
    //
    @Override
    public void init() {
        userManager= new UserManager(this);
        corpus = new NxtCorpusManager(praseXmlPath, observation,this);
        loginWindow = new LoginWindow(userManager,this);
        setTheme(themeDirectory);
        setMainWindow(loginWindow);

    }
    
    public void sentenceChanged(int intValue) {
        mainWindow.sentenceChanged(intValue);
    }

    public void loginChecked() {
        if(mainWindow == null) {
            mainWindow = new MainWindow(this,corpus.getSentenceCount(), corpus,userManager);
        }
         getMainWindow().open(new ExternalResource(getURL()));
         setMainWindow(mainWindow);
    }

    public void logout() {
        
        getMainWindow().open(new ExternalResource(getURL()));
        setMainWindow(loginWindow);
    }
    


   
    
    
}
