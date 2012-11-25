/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.components.popups;

import com.example.vaadin.components.CorpusDataComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView.Content;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import org.bushbank.bushbank.core.Token;

/**
 *
 * @author Mato
 */
public class MissingTokenPopUp implements Content {
    
    private final DataComponentPopupManager manager;
    private VerticalLayout root = new VerticalLayout();

    public MissingTokenPopUp(final DataComponentPopupManager manager, final Token verbToken) {
        this.manager=manager;
        root.setSizeUndefined();
        root.setWidth("200px");
        root.setSpacing(true);
        root.setMargin(true);
        
        confirmMissingTokenCreation(verbToken);
        /*
        Label questionLabel = new Label("Skutočne chceš vytvoriť nový missing token pre sloveso \""  + verbToken.getWordForm()+ "\"?");
        root.addComponent(questionLabel);


        VerticalLayout token = new VerticalLayout();
        root.addComponent(token);
        Label titleLabel = new Label("Token :");
        token.addComponent(titleLabel);

        final TextField tokenWordFormField = new TextField("Slovo :");
        token.addComponent(tokenWordFormField);



        Button createButton = new Button("Ulož");
        createButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                data.approvedMissingToken((String)tokenWordFormField.getValue(),verbToken);
                
            }
        });
        HorizontalLayout horizontalButtonPart = new HorizontalLayout();
        root.addComponent(horizontalButtonPart);
        horizontalButtonPart.addComponent(createButton);
        horizontalButtonPart.setComponentAlignment(createButton, Alignment.MIDDLE_LEFT);
        Button cancelButton = new Button("Ne");
        cancelButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
               root.setVisible(false);
               data.notapprovedMissingToken();
            }
        });
        horizontalButtonPart.addComponent(cancelButton);
        horizontalButtonPart.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);*/
    }

    @Override
    public String getMinimizedValueAsHTML() {
        return "";
    }

    @Override
    public Component getPopupComponent() {
        return root;
    }

    private void confirmMissingTokenCreation(final Token verbToken) {
         Label questionLabel = new Label("Skutočne chceš vytvoriť nový missing token pre sloveso \""  + verbToken.getWordForm()+ "\"?");
         root.addComponent(questionLabel);
         
         
        Button yesButton = new Button("Ano");
        yesButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                missingTokenConfirmed(verbToken);
                
            }
        });
        HorizontalLayout horizontalButtonPart = new HorizontalLayout();
        root.addComponent(horizontalButtonPart);
        horizontalButtonPart.addComponent(yesButton);
        horizontalButtonPart.setComponentAlignment(yesButton, Alignment.MIDDLE_LEFT);
        Button cancelButton = new Button("Ne");
        cancelButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
               root.setVisible(false);
               manager.notapprovedMissingToken();
            }
        });
        horizontalButtonPart.addComponent(cancelButton);
        horizontalButtonPart.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
    }
    
    private void missingTokenConfirmed(Token verbToken) { 
        //verbToken is the clicked verb
        manager.approvedMissingToken(verbToken);
        
    }
    
}
