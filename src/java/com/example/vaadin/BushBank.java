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
    
    @Override
    public void init() {
	Window mainWindow = new Window("BushBank");
        setTheme("mytheme");
	setMainWindow(mainWindow);
        corpus = new NxtCorpusManager("NxtFiles/prase.xml", "ff");
        slider = new TopSlider(corpus.getSentenceCount(), this);
        mainWindow.addComponent(slider);
        List<Sentence> threeLastSentences = new ArrayList<Sentence>();
        //this is first
        threeLastSentences.add(corpus.getSentence(0));
        data = new CorpusDataComponent(threeLastSentences);
        mainWindow.addComponent(data);
        
        
    }

    public void sliderValueChanged(int intValue) {
        List<Sentence> threeLastSentences = new ArrayList<Sentence>();
        if((intValue -3) >=0) {
            threeLastSentences.add(corpus.getSentence(intValue -3));
        }
        if((intValue -2) >=0) { 
            threeLastSentences.add(corpus.getSentence(intValue -2));
        }
        if((intValue -1) >=0) { 
            threeLastSentences.add(corpus.getSentence(intValue -1));
        }
        
        data.setSentences(threeLastSentences);
    }

}
