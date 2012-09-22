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

    public void trySaveAnaphora(String tokenWord, String phraseWords, List<Sentence> threeLastSentences) {
        //Token and selected phrase
        Sentence thisSentence = threeLastSentences.get(threeLastSentences.size() - 1);
        Token token = findToken(tokenWord, threeLastSentences);
        Phrase phrase = findPhrase(phraseWords, thisSentence);
        Anaphora anaphora = new Anaphora("1", token, phrase);
        if(corpus.trySaveAnaphora(anaphora)) {
            thisSentence.addAnaphora(anaphora);
            application.sentenceChanged(getSentencePosition(thisSentence));
        }
        
    }

    public void trySaveAnaphora(String wordForm, String phraseWords, Sentence sentence) {
        //missingToken and selected phrase
        MissingToken token = new MissingToken(wordForm, wordForm);
        Phrase phrase = findPhrase(phraseWords, sentence);
        Anaphora anaphora = new Anaphora("id", token, phrase);
        if (corpus.trySaveAnaphoraWithUnsavedMissingToken(anaphora, sentence)) {
             sentence.addAnaphora(anaphora);
             application.sentenceChanged(getSentencePosition(sentence));
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

    public void deleteAnaphora(Anaphora selectedAnaphora) {
        corpus.deleteObject(selectedAnaphora.getId());
    }
}