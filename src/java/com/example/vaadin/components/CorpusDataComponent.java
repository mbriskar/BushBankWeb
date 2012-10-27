/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.components;


import com.example.vaadin.components.popups.CreateAnaphoraPopUp;
import com.example.vaadin.components.popups.MissingTokenPopUp;
import com.example.vaadin.corpusManager.NxtCorpusManager;
import com.vaadin.ui.Alignment;

import com.vaadin.ui.PopupView;
import org.bushbank.bushbank.core.Sentence;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;



import java.util.List;
import org.bushbank.bushbank.core.MissingToken;
import org.bushbank.bushbank.core.Token;

/**Main class responsible for data.
 *
 * @author Mato
 */
public final class CorpusDataComponent extends VerticalLayout {

    //2 main parts (the one responsible for writing the sentences, the second for the tables)
    SentencesWriter sentences;
    TablesComponent tables;
    
    NxtCorpusManager corpus;
    PopupView anaphoraPopup;
    PopupView missingTokenPopup;
    
    
    MissingToken createdToken;// we need to know if token was created just for the anapahora(to delete it if not approved)
    
    List<Sentence> beforeSentences;
    Sentence thisSentence;
    Sentence afterSentence;
    

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

   //called when clicked on the pronoun and target token
    public void createAnaphora(Token selectedPronoun, Token target) {
         anaphoraPopup= new PopupView(new CreateAnaphoraPopUp(selectedPronoun, target, this));
        anaphoraPopup.setStyleName("popupMiddle");
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
        missingTokenPopup.setStyleName("popupMiddle");
        addComponent(missingTokenPopup);
        
        this.setComponentAlignment(missingTokenPopup, Alignment.MIDDLE_CENTER);
        missingTokenPopup.setHideOnMouseOut(false);
        missingTokenPopup.setPopupVisible(true);
    }
    //called only from MissingToken popup
    public void approvedMissingToken(String wordForm, Token verbToken) {
        MissingToken token =corpus.createMissingToken(wordForm,thisSentence);
        missingTokenPopup.setPopupVisible(false);
        createdToken=token;
        createAnaphora(verbToken, token);
    }
    //called only from MissingToken popup
    public void notapprovedMissingToken() {
        missingTokenPopup.setPopupVisible(false);
    }
}
