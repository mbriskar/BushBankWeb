/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.components;

import com.example.vaadin.components.tables.AnaphoraTable;
import com.example.vaadin.components.tables.PhraseTable;
import com.example.vaadin.components.tables.TokenTable;
import com.example.vaadin.corpusManager.NxtCorpusManager;
import com.vaadin.terminal.Sizeable;
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
    private final TokenTable tokenTable;
    private final PhraseTable phraseTable;
    private final AnaphoraTable anaphoraTable;
    
    private static String tableSpacingCss = "spacingTables";
    
    
    
    public TablesComponent(List<Sentence> beforeSentences, Sentence selectedSentence, Sentence afterSentence, NxtCorpusManager corpus) {
        tokenTable = new TokenTable(selectedSentence.getTokens());
        phraseTable = new PhraseTable(selectedSentence.getPhrases());
        anaphoraTable = new AnaphoraTable(beforeSentences, selectedSentence, afterSentence, corpus);
        drawTables();
    }

    void setSentences(List<Sentence> beforeSentences, Sentence thisSentence, Sentence afterSentence) {
        tokenTable.setTokens(thisSentence.getTokens());
        phraseTable.setPhrases(thisSentence.getPhrases());
        anaphoraTable.sentenceChanged(beforeSentences, thisSentence, afterSentence);
        removeAllComponents();
        drawTables();

    }
    
       private void drawTables() {
        //top tables
        HorizontalLayout topTables = new HorizontalLayout();
        topTables.setStyleName(tableSpacingCss);
        topTables.setWidth(Sizeable.SIZE_UNDEFINED, 0);
        topTables.setSpacing(true);
        addComponent(topTables);
        setComponentAlignment(topTables, Alignment.MIDDLE_CENTER);

        //tables        
        topTables.addComponent(tokenTable);
        topTables.setComponentAlignment(tokenTable, Alignment.TOP_LEFT);

        topTables.addComponent(phraseTable);
        topTables.setComponentAlignment(phraseTable, Alignment.TOP_CENTER);

        topTables.addComponent(anaphoraTable);
        topTables.setComponentAlignment(anaphoraTable, Alignment.TOP_RIGHT);
    }
    
}
