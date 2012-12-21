/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.components;

import com.example.vaadin.components.popups.DataComponentPopupManager;
import com.vaadin.event.LayoutEvents;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bushbank.bushbank.core.MissingToken;
import org.bushbank.bushbank.core.Phrase;
import org.bushbank.bushbank.core.Sentence;
import org.bushbank.bushbank.core.Token;

/**
 *
 * @author Mato
 */
public class SentencesWriter extends VerticalLayout {

    Sentence sentence;
    List<Sentence> beforeSentences;
    Sentence afterSentence;
    private static String sentenceCss = "sentence";
    private static String beforeSentenceTitleCss = "sentenceTitle";
    private static String bottomPaddingCss = "bottomPadding";
    private static String pronounCss = "pronoun";
    private static String verbCss = "verb";
    private static String sentenceTokenCss = "sentenceToken";
    private static String selectedPronounCss = "selectedPronoun";
    DataComponentPopupManager data;
    private Label selectedPronoun = null;


    private static Set<String> pronouns = new HashSet<String>() {
        {
            add("on");
            add("ona");
            add("ten");
            add("tá");
            add("to");
        }
    };
    
    
    /*
     * Responsible for writing all the sentences (2 before, 1 current, 1 after).
     * Also reacts to words clicking.
     */

    public SentencesWriter(List<Sentence> beforeSentences, Sentence thisSentence, Sentence afterSentence,DataComponentPopupManager data) {
        this.data=data;
        sentence = thisSentence;
        this.beforeSentences = beforeSentences;
        this.afterSentence = afterSentence;
        writeSentences();
    }

    public void setSentences(List<Sentence> beforeSentences, Sentence thisSentence, Sentence afterSentence) {
        sentence = thisSentence;
        this.beforeSentences = beforeSentences;
        this.afterSentence = afterSentence;
        removeAllComponents();
        selectedPronoun = null;
        writeSentences();

    }

    private void writeSentences() {
        //before sentences labels
        if (beforeSentences != null) {
            int counter = 1;
            VerticalLayout beforeSentenceVerLayout = new VerticalLayout();
            beforeSentenceVerLayout.setStyleName(bottomPaddingCss);
            addComponent(beforeSentenceVerLayout);
            setComponentAlignment(beforeSentenceVerLayout, Alignment.TOP_CENTER);
            
            for (Sentence s : beforeSentences) {
                CssLayout sentenceLayout = new CssLayout();
                sentenceLayout.addListener(new LayoutEvents.LayoutClickListener() {
                @Override
                public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                    if (event.getChildComponent() instanceof Label) {
                        pronounsTargetTokenSelected(event);
                    }
                }
            });
                beforeSentenceVerLayout.addComponent(sentenceLayout);
                
                Label beforeSentence = new Label(counter + ". predošlá veta:   ");
                beforeSentence.setSizeUndefined();
                beforeSentence.setStyleName(beforeSentenceTitleCss);
                sentenceLayout.addComponent(beforeSentence);
                for (Token t : s.getNotMissingTokens()) {
                    Label sentenceToken = new Label(t.getWordForm());
                    sentenceToken.setData(t);
                    sentenceToken.setSizeUndefined();
                    sentenceLayout.addComponent(sentenceToken);
                   // sentenceLayout.setComponentAlignment(sentenceToken, Alignment.BOTTOM_CENTER);
                    sentenceToken.setStyleName(sentenceTokenCss);

                }
                counter++;
            }
        }

        //selected sentence label
        CssLayout thisSentenceLayout = new CssLayout();
        
        final Set<String> selectableVerbIDs = getSelectableVerbs(sentence);
        
        thisSentenceLayout.addListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (event.getChildComponent() instanceof Label) {
                    Label label = (Label) event.getChildComponent();
                    String selectedWord=(String) label.getValue();
                    if (isPronoun((String) selectedWord)) { //if it is a pronoun
                        if ((selectedPronoun != null) && (label.equals(selectedPronoun)) ) {
                            getWindow().showNotification("Zámeno odznačené.");
                            pronounsCanceled(label);
                        } else {
                            pronounsClicked(event);
                        }
                    } else if ((selectedPronoun != null)){
                        pronounsTargetTokenSelected(event);
                    } else if(selectableVerbIDs.contains(((Token)label.getData()).getID())){
                        // if no pronoun selected but verb

                            verbSelected(label);

                        
                    }
                }
            }

           
        });

        thisSentenceLayout.setStyleName(bottomPaddingCss);
        Label beforeSentence = new Label("Vybratá veta:   ");
         beforeSentence.setSizeUndefined();
         beforeSentence.setStyleName(beforeSentenceTitleCss);
        thisSentenceLayout.addComponent(beforeSentence);
        //thisSentenceLayout.setComponentAlignment(beforeSentence, Alignment.BOTTOM_CENTER);
        for (Token t : sentence.getTokens()) {
            Label sentenceToken;
            if(t instanceof MissingToken) {
                sentenceToken = new Label("(MT:\"" +  t.getID() + "\")");
            } else {
                sentenceToken = new Label(t.getWordForm());
            }
            sentenceToken.setData(t);
            sentenceToken.setSizeUndefined();
            thisSentenceLayout.addComponent(sentenceToken);
            //thisSentenceLayout.setComponentAlignment(sentenceToken, Alignment.BOTTOM_CENTER);
            if (isPronoun((String)sentenceToken.getValue())) {
                sentenceToken.setStyleName(pronounCss);
            } else if (selectableVerbIDs.contains(((Token)sentenceToken.getData()).getID())){
                sentenceToken.setStyleName(verbCss);
            } else {
                sentenceToken.setStyleName(sentenceTokenCss);
            }

        }
        
        


        addComponent(thisSentenceLayout);

        //after sentence label
        if (afterSentence != null) {
            CssLayout afterSentenceLayout = new CssLayout();
            afterSentenceLayout.setStyleName(bottomPaddingCss);
            addComponent(afterSentenceLayout);
            Label beforeAfterSentence = new Label("Nasledujúca veta:   ");
            beforeAfterSentence.setSizeUndefined();
            afterSentenceLayout.addComponent(beforeAfterSentence);
            beforeAfterSentence.setStyleName(beforeSentenceTitleCss);
            afterSentenceLayout.addListener(new LayoutEvents.LayoutClickListener() {
                @Override
                public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                    if (event.getChildComponent() instanceof Label) {
                        pronounsTargetTokenSelected(event);
                    }
                }
            });
            for (Token t : afterSentence.getNotMissingTokens()) {
                Label sentenceToken = new Label(t.getWordForm());
                sentenceToken.setData(t);
                sentenceToken.setSizeUndefined();
                afterSentenceLayout.addComponent(sentenceToken);
               // afterSentenceLayout.setComponentAlignment(sentenceToken, Alignment.BOTTOM_CENTER);
                sentenceToken.setStyleName(sentenceTokenCss);
            }

        }
    }

    public void pronounsClicked(LayoutEvents.LayoutClickEvent event) {
        Label label = (Label) event.getChildComponent();
        label.setStyleName(selectedPronounCss);
        selectedPronoun = label;
        getWindow().showNotification("Zámeno označené. Pre vytvorenie anafory označ odkazované slovo.");
    }

    private void pronounsTargetTokenSelected(LayoutEvents.LayoutClickEvent event) {
        Label label = (Label) event.getChildComponent();
        Token target =(Token)label.getData();        
        if (selectedPronoun != null) {
            data.createAnaphora((Token)selectedPronoun.getData(),target);
            pronounsCanceled(selectedPronoun);
            
        }

    }
    
    public boolean isPronoun(String pronoun) {
        if(pronoun == null) {
            return false;
        }
       if( pronouns.contains(pronoun) || (pronoun.contains("MT:") )) {
           return true;
       } else {
           return false;
       }

    }

     private void verbSelected(Label label) {
          data.createMissingTokenFor((Token)label.getData());
     }

     
    
    
    private void pronounsCanceled(Label label) {
       // getWindow().showNotification("Zámeno odznačené.");
        selectedPronoun = null;
        label.setStyleName(pronounCss);
    }

    public void addPronoun(String pronoun) {
        pronouns.add(pronoun);
    }
    private Set<String> getSelectableVerbs(Sentence sentence) {
        Set<String> selectable= new HashSet<String>();
        for (Phrase p:sentence.getPhrases()) {
            if(p.getGrammarTag().equals("vp")) {
                for(Token t :p.getTokens()) {
                    selectable.add(t.getID());
                }
            }
        }
        return selectable;
    }
}
