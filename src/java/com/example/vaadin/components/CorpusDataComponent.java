/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.components;

import com.example.vaadin.components.popups.CreateAnaphoraPopUp;
import com.example.vaadin.components.tables.AnaphoraTable;
import com.example.vaadin.components.tables.PhraseTable;
import com.example.vaadin.components.tables.TokenTable;
import com.example.vaadin.corpusManager.NxtCorpusManager;
import com.vaadin.ui.PopupView;
import org.bushbank.bushbank.core.Sentence;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import org.bushbank.bushbank.core.Token;

/**
 *
 * @author Mato
 */
public final class CorpusDataComponent extends VerticalLayout {

    SentencesWriter sentences;
    TablesComponent tables;
    NxtCorpusManager corpus;
    List<Sentence> beforeSentences;
    Sentence thisSentence;
    Sentence afterSentence;
    PopupView popup;

    public CorpusDataComponent(List<Sentence> beforeSentences, Sentence thisSentence, Sentence afterSentence, NxtCorpusManager corpus) {
        this.corpus = corpus;
        this.beforeSentences = beforeSentences;
        this.thisSentence = thisSentence;
        this.afterSentence = afterSentence;
        sentences = new SentencesWriter(beforeSentences, thisSentence, afterSentence, this);
        tables = new TablesComponent(beforeSentences, thisSentence, afterSentence, corpus);

        addComponent(sentences);
        addComponent(tables);
    }

    public void setSentences(List<Sentence> beforeSentences, Sentence thisSentence, Sentence afterSentence) {
        this.beforeSentences = beforeSentences;
        this.thisSentence = thisSentence;
        this.afterSentence = afterSentence;
        sentences.setSentences(beforeSentences, thisSentence, afterSentence);
        tables.setSentences(beforeSentences, thisSentence, afterSentence);
    }

    public void createAnaphora(Token selectedPronoun, Token target) {
         popup= new PopupView(new CreateAnaphoraPopUp(selectedPronoun, target, this));
        popup.setStyleName("createAnaphoraPopup");
        addComponent(popup);
        popup.setHideOnMouseOut(false);
        popup.setPopupVisible(true);
    }

    public void approvedAnaphora(Token selectedPronoun, Token target) {
        Sentence pronounSentence = findTokenSentence(selectedPronoun);
        corpus.trySaveAnaphora(selectedPronoun, target, pronounSentence);
         popup.setPopupVisible(false);

    }
    
    public void notApprovedAnaphora() {
        popup.setPopupVisible(false);

    }
    
    private Sentence findTokenSentence(Token token) {
        
        for (Sentence s : beforeSentences) {
            for (Token t : s.getTokens()) {
                if (t.getID().equals(token.getID())) {
                    return s;
                } 
            }
        }

        for (Token t : thisSentence.getTokens()) {
            if (t.getID().equals(token.getID())) {
                return thisSentence;
            }
        }

        for (Token t : afterSentence.getTokens()) {
            if (t.getID().equals(token.getID())) {
                return afterSentence;
            }
            
        }
        
        return null;
    }
}
