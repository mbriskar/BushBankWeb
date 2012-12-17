/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.components.popups;

import com.example.vaadin.windows.MainWindow;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.PopupView.Content;
import com.vaadin.ui.VerticalLayout;
import java.util.List;

/**
 *
 * @author Mato
 */
@SuppressWarnings("serial")

public class SelectCorpusPopUp implements Content,
        Property.ValueChangeListener{
    public static final String SELECT_CORPUS_TEXT_STRING = "Vyber korpus";



    MainWindow mw ;
    private VerticalLayout root = new VerticalLayout();


    public SelectCorpusPopUp(List<String> corpusesAvailable,MainWindow window)  {
        mw=window;
        root.setSizeUndefined();
        root.setSpacing(true);
        root.setMargin(true);
            
        ComboBox l = new ComboBox(SELECT_CORPUS_TEXT_STRING);
        for (int i = 0; i < corpusesAvailable.size(); i++) {
            l.addItem(corpusesAvailable.get(i));
        }


        l.setImmediate(true);
        l.addListener(this);
        root.addComponent(l);

        
    }

   
    
    @Override
    public String getMinimizedValueAsHTML() {
        return "";
    }
     
    public void valueChange(ValueChangeEvent event) {
        mw.selectedCorpus((String)event.getProperty().toString());
    }
 

    @Override
    public Component getPopupComponent() {
        return root;
    }
}

