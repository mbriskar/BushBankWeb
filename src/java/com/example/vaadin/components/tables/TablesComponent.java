/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.components.tables;

import com.example.vaadin.components.tables.AnaphoraTable;
import com.example.vaadin.corpusManager.NxtCorpusManager;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import org.bushbank.bushbank.core.Sentence;

/**
 *
 * @author Mato
 */
public class TablesComponent extends VerticalLayout{

    private final AnaphoraTable anaphoraTable;
    private static String tableSpacingCss = "spacingTables";
    
    public TablesComponent(List<Sentence> beforeSentences, Sentence selectedSentence, Sentence afterSentence, NxtCorpusManager corpus) {
        anaphoraTable = new AnaphoraTable(selectedSentence, corpus);
        drawTables();
    }

    public void setSentences(List<Sentence> beforeSentences, Sentence thisSentence, Sentence afterSentence) {
        anaphoraTable.sentenceChanged(thisSentence);
        removeAllComponents();
        drawTables();

    }
    
       private void drawTables() {
        HorizontalLayout topTables = new HorizontalLayout();
        topTables.setSizeFull();
        addComponent(topTables);
        setComponentAlignment(topTables, Alignment.MIDDLE_CENTER);

        //tables        
        topTables.addComponent(anaphoraTable);
        anaphoraTable.setSizeFull();
        topTables.setComponentAlignment(anaphoraTable, Alignment.TOP_CENTER);
    }
    
}
