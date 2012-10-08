/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private static String sentenceTokenCss = "sentenceToken";
    private static String selectedPronounCss = "selectedPronoun";
    CorpusDataComponent data;
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

    public SentencesWriter(List<Sentence> beforeSentences, Sentence thisSentence, Sentence afterSentence,CorpusDataComponent data) {
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
                HorizontalLayout sentenceLayout = new HorizontalLayout();
                sentenceLayout.addListener(new LayoutEvents.LayoutClickListener() {
                @Override
                public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                    if (event.getChildComponent() instanceof Label) {
                        pronounsTargetTokenSelected(event);
                    }
                }
            });
                beforeSentenceVerLayout.addComponent(sentenceLayout);
                sentenceLayout.setWidth(Sizeable.SIZE_UNDEFINED, 0);
                Label beforeSentence = new Label(counter + ". predošlá veta:   ");
                beforeSentence.setStyleName(beforeSentenceTitleCss);
                sentenceLayout.addComponent(beforeSentence);
                for (Token t : s.getNotMissingTokens()) {
                    Label sentenceToken = new Label(t.getWordForm());
                    sentenceToken.setData(t);
                    sentenceToken.setSizeUndefined();
                    sentenceLayout.addComponent(sentenceToken);
                    sentenceLayout.setComponentAlignment(sentenceToken, Alignment.BOTTOM_CENTER);
                    sentenceToken.setStyleName(sentenceTokenCss);

                }
                counter++;
            }
        }

        //selected sentence label
        HorizontalLayout thisSentenceLayout = new HorizontalLayout();
        thisSentenceLayout.addListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (event.getChildComponent() instanceof Label) {
                    Label label = (Label) event.getChildComponent();
                    if (pronouns.contains((String) label.getValue())) { //if it is a pronoun
                        if ((selectedPronoun != null) && (((String) label.getValue()).equals(selectedPronoun.getValue())) ) {
                            pronounsCanceled(label);
                        } else {
                            pronounsClicked(event);
                        }
                    } else {
                        pronounsTargetTokenSelected(event);
                    }
                }
            }
        });

        thisSentenceLayout.setStyleName(bottomPaddingCss);
        Label beforeSentence = new Label("Vybratá veta:   ");
        thisSentenceLayout.addComponent(beforeSentence);
        thisSentenceLayout.setComponentAlignment(beforeSentence, Alignment.BOTTOM_CENTER);
        for (Token t : sentence.getNotMissingTokens()) {
            Label sentenceToken = new Label(t.getWordForm());
            sentenceToken.setData(t);
            sentenceToken.setSizeUndefined();
            thisSentenceLayout.addComponent(sentenceToken);
            thisSentenceLayout.setComponentAlignment(sentenceToken, Alignment.BOTTOM_CENTER);
            if (pronouns.contains(t.getWordForm())) {
                sentenceToken.setStyleName(pronounCss);
            } else {
                sentenceToken.setStyleName(sentenceTokenCss);
            }

        }
        beforeSentence.setStyleName(beforeSentenceTitleCss);
        thisSentenceLayout.setWidth(Sizeable.SIZE_UNDEFINED, 0);
        addComponent(thisSentenceLayout);

        //after sentence label
        if (afterSentence != null) {
            HorizontalLayout afterSentenceLayout = new HorizontalLayout();
            afterSentenceLayout.setStyleName(bottomPaddingCss);
            addComponent(afterSentenceLayout);
            Label beforeAfterSentence = new Label("Nasledujúca veta:   ");
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
                afterSentenceLayout.setComponentAlignment(sentenceToken, Alignment.BOTTOM_CENTER);
                sentenceToken.setStyleName(sentenceTokenCss);
            }

        }
    }

    private void pronounsClicked(LayoutEvents.LayoutClickEvent event) {
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

    private void pronounsCanceled(Label label) {
        getWindow().showNotification("Zámeno odznačené.");
        selectedPronoun = null;
        label.setStyleName(pronounCss);
    }
}
