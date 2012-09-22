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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        List<Sentence> threeLastSentences = new ArrayList<Sentence>();
        //this is first
        threeLastSentences.add(corpus.getSentence(0));
        data = new CorpusDataComponent(threeLastSentences, corpus);
        mainWindow.addComponent(data);
        mainWindow.addListener(new Window.CloseListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void windowClose(CloseEvent e) {
                corpus.saveChanges();
            }
        });



    }
    


    public void sentenceChanged(int intValue) {
        List<Sentence> threeLastSentences = new ArrayList<Sentence>();
        if ((intValue - 3) >= 0) {
            threeLastSentences.add(corpus.getSentence(intValue - 3));
        }
        if ((intValue - 2) >= 0) {
            threeLastSentences.add(corpus.getSentence(intValue - 2));
        }
        if ((intValue - 1) >= 0) {
            threeLastSentences.add(corpus.getSentence(intValue - 1));
        }

        data.setSentences(threeLastSentences);
    }
    
    
}
