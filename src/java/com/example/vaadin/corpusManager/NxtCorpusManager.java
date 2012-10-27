/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.corpusManager;

import com.example.vaadin.BushBank;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bushbank.bushbank.core.Anaphora;
import org.bushbank.bushbank.core.MissingToken;
import org.bushbank.bushbank.core.Phrase;
import org.bushbank.bushbank.core.Sentence;
import org.bushbank.bushbank.core.Token;
import org.bushbank.bushbank.nxt.NxtCorpus;
import org.bushbank.bushbank.nxt.NxtException;

/**
 *
 * @author Mato
 */
public class NxtCorpusManager {

    NxtCorpus corpus;
    BushBank application;

    public NxtCorpusManager(String metadataPath, String observationName, BushBank app) {
        try {
            corpus = new NxtCorpus(metadataPath, observationName);
            application=app;
        } catch (NxtException ex) {
            Logger.getLogger(NxtCorpusManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getSentenceCount() {
        return corpus.getSentences().size();
    }

    public Sentence getSentence(int position) {
        return corpus.getSentence(position);
    }

    public void trySaveAnaphora(Token pointer, Token target, Sentence anaphoraParentSentence) {

        Anaphora anaphora = new Anaphora("1");
        anaphora.setPointer(pointer);
        anaphora.setTarget(target);
        if(corpus.trySaveAnaphora(anaphora)) {
            anaphoraParentSentence.addAnaphora(anaphora);
            application.sentenceChanged(getSentencePosition(anaphoraParentSentence));
        }
        
    }

    private Phrase findPhrase(String phraseString, Sentence thisSentence) {
        for (Phrase p : thisSentence.getPhrases()) {
            if (p.toString().equals(phraseString)) {
                return p;
            }
        }
        return null;
    }

    private Token findToken(String tokenWord, List<Sentence> threeLastSentences) {
        for (Sentence s : threeLastSentences) {
            for (Token t : s.getTokens()) {
                if (t.toString().equals(tokenWord)) {
                    return t;
                }
            }

        }
        return null;
    }

    public void saveChanges() {
        corpus.save();
    }

    private int getSentencePosition(Sentence thisSentence) {
        Integer position = null;
        for (int i=0; i<getSentenceCount();i++) {
            Sentence s = getSentence(i);
            if(s.getID().equals(thisSentence.getID())) {
                position =i;
                break;
            }
        }
        if(position == null) {
            throw new IndexOutOfBoundsException();
        }
        return position+1;
    }



    public MissingToken createMissingToken(String wordForm,Sentence sentence) {
        MissingToken mt = new MissingToken("",wordForm);
        mt =corpus.trySaveMissingToken(mt, sentence);
        if(mt!=null) {
            sentence.add(mt);
        }
        
        return mt;
        
    }

    public void deleteObject(String id) {
        corpus.deleteObject(id);
    }
}