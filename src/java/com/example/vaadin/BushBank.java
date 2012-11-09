/*
 * BushBank.java
 *
 * Created on September 15, 2012, 10:01 PM
 */
package com.example.vaadin;

import com.example.vaadin.components.CorpusDataComponent;
import com.example.vaadin.components.TopSlider;
import com.example.vaadin.corpusManager.NxtCorpusManager;
import com.vaadin.Application;
import com.vaadin.ui.*;
import com.vaadin.ui.Window.CloseEvent;
import java.util.ArrayList;
import java.util.List;
import org.bushbank.bushbank.core.Sentence;

/**
 *
 * @author Mato
 * @version
 */
public class BushBank extends Application {

    NxtCorpusManager corpus;
    TopSlider slider;
    CorpusDataComponent data;
    
    // STRINGS :
    private static String themeDirectory="mytheme";
    private static String praseXmlPath="NxtFiles/prase.xml";
    private static String observation="ff";
    //
    @Override
    public void init() {


        Window mainWindow = new Window("BushBank");
        setTheme(themeDirectory);
        setMainWindow(mainWindow);
        corpus = new NxtCorpusManager(praseXmlPath, observation,this);
        slider = new TopSlider(corpus.getSentenceCount(), this);
        mainWindow.addComponent(slider);
        //this is first
        Sentence thisSentence = corpus.getSentence(0);
        data = new CorpusDataComponent(null,thisSentence, corpus.getSentence(1), corpus);
        mainWindow.addComponent(data);
        mainWindow.addListener(new Window.CloseListener() {
            //to save corpus when exit was clicked
            private static final long serialVersionUID = 1L;
            @Override
            public void windowClose(CloseEvent e) {
                corpus.saveChanges();
            }
        });

    }
    


    public void sentenceChanged(int intValue) {
        List<Sentence> beforeSentences = new ArrayList<Sentence>();
        Sentence thisSentence=null;
        Sentence afterSentence=null;
        if ((intValue - 3) >= 0) {
            beforeSentences.add(corpus.getSentence(intValue - 3));
        }
        if ((intValue - 2) >= 0) {
            beforeSentences.add(corpus.getSentence(intValue - 2));
        }
        if ((intValue - 1) >= 0) {
            thisSentence=corpus.getSentence(intValue - 1);
        }
        
        if(intValue < corpus.getSentenceCount()) {
            afterSentence=corpus.getSentence(intValue);
        }
        data.setSentences(beforeSentences,thisSentence,afterSentence);
    }
    
    
}
