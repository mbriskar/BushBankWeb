/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.components;


import com.example.vaadin.components.popups.PopupManager;
import com.example.vaadin.components.tables.TablesComponent;
import com.example.vaadin.corpusManager.NxtCorpusManager;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import org.bushbank.bushbank.core.Sentence;

/**Main class responsible for data.
 *
 * @author Mato
 */
public final class CorpusDataComponent extends VerticalLayout {
    public static final String POPUP_MIDDLE_CSS = "popupMiddle";

    //3 main parts (the one responsible for writing the sentences, the second for the tables, third is responsible for all the popups)
    SentencesWriter sentences;
    TablesComponent tables;
    PopupManager popupManager;
    
    
    List<Sentence> beforeSentences;
    Sentence thisSentence;
    Sentence afterSentence;
    

    public CorpusDataComponent(List<Sentence> beforeSentences, Sentence thisSentence, Sentence afterSentence, NxtCorpusManager corpus) {
        this.beforeSentences = beforeSentences;
        this.thisSentence = thisSentence;
        this.afterSentence = afterSentence;
        popupManager = new PopupManager(corpus, beforeSentences, thisSentence, afterSentence);
        sentences = new SentencesWriter(beforeSentences, thisSentence, afterSentence, popupManager);
        tables = new TablesComponent(beforeSentences, thisSentence, afterSentence, corpus);
        
        addComponent(popupManager);
        addComponent(sentences);
        addComponent(tables);
    }

    public void setSentences(List<Sentence> beforeSentences, Sentence thisSentence, Sentence afterSentence) {
        this.beforeSentences = beforeSentences;
        this.thisSentence = thisSentence;
        this.afterSentence = afterSentence;
        sentences.setSentences(beforeSentences, thisSentence, afterSentence);
        tables.setSentences(beforeSentences, thisSentence, afterSentence);
        popupManager.setSentences(beforeSentences, thisSentence, afterSentence);
    }

}
