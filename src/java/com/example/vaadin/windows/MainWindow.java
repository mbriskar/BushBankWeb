/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.windows;

import com.example.vaadin.BushBank;
import com.example.vaadin.components.CorpusDataComponent;
import com.example.vaadin.components.TopSlider;
import com.example.vaadin.corpusManager.NxtCorpusManager;
import com.example.vaadin.user.UserManager;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.ArrayList;


import java.util.List;
import org.bushbank.bushbank.core.Sentence;

/**
 *
 * @author Mato
 */
public class MainWindow extends Window {

    private CorpusDataComponent data;
    private TopSlider slider;
    NxtCorpusManager corpus;
    UserManager userManager;
    private final BushBank app;

    public MainWindow(BushBank app, int sentenceCount, NxtCorpusManager corpus, UserManager userManager) {
        this.corpus = corpus;
        this.app=app;
        this.userManager = userManager;
        slider = new TopSlider(sentenceCount, this);
        data = new CorpusDataComponent(null, corpus.getSentence(0), corpus.getSentence(1), corpus);
        setName("BushBank");
        initUI();
    }

    private void initUI() {

        VerticalLayout vl = new VerticalLayout();
        vl.setSizeFull();
        HorizontalLayout userManagementLayout = new HorizontalLayout();
        vl.addComponent(userManagementLayout);
        Label welcomeLabel = new Label("Prihlásený ako " + userManager.getUserOnline() + ".");
        welcomeLabel.setWidth(null);
        userManagementLayout.addComponent(welcomeLabel);
        Button logout = new Button("Odhlásiť");
        logout.setStyleName(Button.STYLE_LINK);

        userManagementLayout.addComponent(logout);
        userManagementLayout.setSpacing(true);
        logout.addListener ( new Button.ClickListener()
        {
            public void buttonClick ( Button.ClickEvent event )
            {
                userManager.logout();
            }
        });

        vl.setComponentAlignment(userManagementLayout, Alignment.MIDDLE_RIGHT);
        vl.addComponent(slider);
        vl.addComponent(data);
        addComponent(vl);
        addListener(new Window.CloseListener() {
            //to save corpus when exit was clicked
            private static final long serialVersionUID = 1L;

            @Override
            public void windowClose(CloseEvent e) {
                corpus.saveChanges();
            }
        });
    }

    public void sentenceChanged(int intValue) {
        List<Sentence> beforeSentences = new ArrayList<Sentence>();
        Sentence thisSentence = null;
        Sentence afterSentence = null;
        if ((intValue - 3) >= 0) {
            beforeSentences.add(corpus.getSentence(intValue - 3));
        }
        if ((intValue - 2) >= 0) {
            beforeSentences.add(corpus.getSentence(intValue - 2));
        }
        if ((intValue - 1) >= 0) {
            thisSentence = corpus.getSentence(intValue - 1);
        }

        if (intValue < corpus.getSentenceCount()) {
            afterSentence = corpus.getSentence(intValue);
        }
        data.setSentences(beforeSentences, thisSentence, afterSentence);
    }
}