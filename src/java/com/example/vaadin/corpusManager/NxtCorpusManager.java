/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.corpusManager;

import com.vaadin.ui.VerticalLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bushbank.bushbank.core.Sentence;
import org.bushbank.bushbank.nxt.NxtCorpus;
import org.bushbank.bushbank.nxt.NxtException;

/**
 *
 * @author Mato
 */
public class NxtCorpusManager {
     NxtCorpus corpus;
    public NxtCorpusManager(String metadataPath, String observationName) {
        try {
            corpus = new NxtCorpus(metadataPath,observationName);
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
}