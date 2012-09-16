/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.components;

import com.example.vaadin.components.tables.PhraseTable;
import com.example.vaadin.components.tables.TokenTable;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import org.bushbank.bushbank.core.Sentence;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Mato
 */
public final class CorpusDataComponent extends VerticalLayout {

    Sentence sentence;
    Label sentenceString;
    TokenTable tokenTable;
    PhraseTable phraseTable;

    public CorpusDataComponent(Sentence s) {
        sentence = s;
        tokenTable = new TokenTable(s.getTokens());
        phraseTable = new PhraseTable(s.getPhrases());
        sentenceString= new Label(sentence.toString());
        draw();

    }

    public void setSentence(Sentence s) {
        sentence = s;
        tokenTable.setTokens(s.getTokens());
        phraseTable.setPhrases(s.getPhrases());
        sentenceString.setValue(sentence);
        
    }

    public void draw() {


        setSpacing(true);
        sentenceString = new Label(sentence.toString());
        sentenceString.setWidth(Sizeable.SIZE_UNDEFINED, 0);
        addComponent(sentenceString);
        setComponentAlignment(sentenceString, Alignment.TOP_CENTER);


        //top tables
        HorizontalLayout topTables = new HorizontalLayout();
        //topTables.setSizeFull();
        topTables.setWidth(Sizeable.SIZE_UNDEFINED, 0);
        topTables.setSpacing(true);
        addComponent(topTables);

        //tables  
        topTables.addComponent(tokenTable);
        topTables.setComponentAlignment(tokenTable, Alignment.TOP_LEFT);
        

        topTables.addComponent(phraseTable);
        topTables.setComponentAlignment(phraseTable, Alignment.TOP_CENTER);
        
        
        
        


    }
}
