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
	setMainWindow(mainWindow);
        corpus = new NxtCorpusManager("NxtFiles/prase.xml", "ff");
        slider = new TopSlider(corpus.getSentenceCount(), this);
        mainWindow.addComponent(slider);
        data = new CorpusDataComponent(corpus.getSentence(0));
        mainWindow.addComponent(data);
        
        
    }

    public void sliderValueChanged(int intValue) {
        data.setSentence(corpus.getSentence(intValue -1));
    }

}
