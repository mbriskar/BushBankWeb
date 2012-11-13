/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.components.popups;

import com.example.vaadin.components.CorpusDataComponent;
import com.example.vaadin.components.SentencesWriter;
import com.example.vaadin.corpusManager.NxtCorpusManager;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import org.bushbank.bushbank.core.MissingToken;
import org.bushbank.bushbank.core.Sentence;
import org.bushbank.bushbank.core.Token;

/**
 * Responsible for all the popups, which shows during the anaphora/missing token creation.
 *
 * @author Mato
 */
public class PopupManager extends VerticalLayout{
    public static final String POPUP_MIDDLE_CSS = "popupMiddle";
    PopupView anaphoraPopup;
    PopupView missingTokenPopup;

    private final NxtCorpusManager corpus;
    MissingToken createdToken;
    private Sentence afterSentence;
    private Sentence thisSentence;
    private List<Sentence> beforeSentences;
    private SentencesWriter sentencesWriter;
    
    public PopupManager(NxtCorpusManager corpus, SentencesWriter sentencesWriter, List<Sentence> beforeSentences, Sentence thisSentence, Sentence afterSentence) {
        this.beforeSentences=beforeSentences;
        this.sentencesWriter=sentencesWriter;
        this.thisSentence = thisSentence;
        this.afterSentence=afterSentence;
        this.corpus=corpus;
    }
    
    public void setSentences(final List<Sentence> beforeSentences, final Sentence thisSentence, final Sentence afterSentence) {
        this.beforeSentences=beforeSentences;
        this.thisSentence = thisSentence;
        this.afterSentence=afterSentence;
    }
    
    //called when clicked on the pronoun and target token. Called from the SentencesWriter.
    public void createAnaphora(Token selectedPronoun, Token target) {
        anaphoraPopup= new PopupView(new CreateAnaphoraPopUp(selectedPronoun, target, this));
        anaphoraPopup.setStyleName(POPUP_MIDDLE_CSS);
        addComponent(anaphoraPopup);
        anaphoraPopup.setHideOnMouseOut(false);
        anaphoraPopup.setPopupVisible(true);
    }

    //called only from Anaphora popup
    public void approvedAnaphora(Token selectedPronoun, Token target) {
         Sentence pronounSentence = findTokenSentence(selectedPronoun);
         corpus.trySaveAnaphora(selectedPronoun, target, pronounSentence);
         if ((target!=null) && (target == createdToken)) {
             createdToken = null;// we can dismiss variable now, because it was used
         }
         anaphoraPopup.setPopupVisible(false);

    }
     //called only from Anaphora popup
    public void notApprovedAnaphora() {
        if(createdToken != null) {
            corpus.deleteObject(createdToken.getID()); // delete it as it was not used
        }
        anaphoraPopup.setPopupVisible(false);
    }
    
    private Sentence findTokenSentence(Token token) {
        if(beforeSentences !=null) {
        for (Sentence s : beforeSentences) {
            for (Token t : s.getTokens()) {
                if (t.getID().equals(token.getID())) {
                    return s;
                } 
            }
        }
        }

        for (Token t : thisSentence.getTokens()) {
            if (t.getID().equals(token.getID())) {
                return thisSentence;
            }
        }
        if(afterSentence !=null) {
        for (Token t : afterSentence.getTokens()) {
            if (t.getID().equals(token.getID())) {
                return afterSentence;
            }
            
        }
        }
        return null;
    }

    //called when clicked on the verb
    public void createMissingTokenFor(Token token) {
       
        missingTokenPopup= new PopupView(new MissingTokenPopUp(this,token));
        missingTokenPopup.setStyleName(POPUP_MIDDLE_CSS);
        addComponent(missingTokenPopup);
        this.setComponentAlignment(missingTokenPopup, Alignment.MIDDLE_CENTER);
        missingTokenPopup.setHideOnMouseOut(false);
        missingTokenPopup.setPopupVisible(true);
    }
    //called only from MissingToken popup
    /*
    public void approvedMissingToken(String wordForm, Token verbToken) {
        MissingToken token =corpus.createMissingToken(wordForm,thisSentence);
        missingTokenPopup.setPopupVisible(false);
        createdToken=token;
        createAnaphora(verbToken, token);
    }
    */
    //called only from MissingToken popup
    public void notapprovedMissingToken() {
        missingTokenPopup.setPopupVisible(false);
    }

    void approvedMissingToken(Token verbToken) {
        MissingToken token =corpus.createMissingToken(verbToken,thisSentence);
        
        missingTokenPopup.setPopupVisible(false);
    }


    
}
