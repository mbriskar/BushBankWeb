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
import org.bushbank.bushbank.core.Sentence;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.util.List;

/**
 *
 * @author Mato
 */
public final class CorpusDataComponent extends VerticalLayout {

    Sentence sentence;
    Label sentenceString;
    TokenTable tokenTable;
    PhraseTable phraseTable;
    AnaphoraTable anaphoraTable;
    private static String sentenceCss = "sentence";
    private static String tableSpacingCss = "spacingTables";

    public CorpusDataComponent(List<Sentence> beforeSentences, NxtCorpusManager corpus) {
        Sentence thisSentence = beforeSentences.get(beforeSentences.size() - 1);
        sentence = thisSentence;
        tokenTable = new TokenTable(sentence.getTokens());
        phraseTable = new PhraseTable(sentence.getPhrases());
        anaphoraTable = new AnaphoraTable(beforeSentences, corpus);
        sentenceString = new Label(sentence.toString());
        sentenceString.setStyleName(sentenceCss);
        draw();
    }

    public void setSentences(List<Sentence> beforeSentences) {
        Sentence thisSentence = beforeSentences.get(beforeSentences.size() - 1);
        sentence = thisSentence;
        tokenTable.setTokens(sentence.getTokens());
        phraseTable.setPhrases(sentence.getPhrases());
        anaphoraTable.sentenceChanged(beforeSentences);
        sentenceString.setValue(sentence);
    }

    public void draw() {
        setSpacing(true);
        sentenceString.setWidth(Sizeable.SIZE_UNDEFINED, 0);
        addComponent(sentenceString);
        setComponentAlignment(sentenceString, Alignment.TOP_CENTER);

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
