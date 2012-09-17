/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.components.tables;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.PopupView.Content;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import org.bushbank.bushbank.core.Phrase;
import org.bushbank.bushbank.core.Sentence;
import org.bushbank.bushbank.core.Token;

/**
 *
 * @author Mato
 */
public class PhraseTable extends VerticalLayout {

    List<Phrase> phrases;
    Table table = new Table();
    PopupView popup;
    
    public PhraseTable(List<Phrase> phrases ) {
        this.phrases=phrases;
        setSpacing(true);
        final Label value = new Label("Frázy");
        addComponent(value);
        value.setStyleName("tableTitle");
 
        setComponentAlignment(value, Alignment.TOP_CENTER);
        addComponent(table);
        table.setSelectable(true);
        setComponentAlignment(table, Alignment.MIDDLE_CENTER);

        
        Button addSemantic = new Button("přidej sémantický atribút");
        addComponent(addSemantic);
        fillTable();
        
        popup = new PopupView(new PhraseTable.AddSemanticPopUp());

        popup.setHideOnMouseOut(false);
        addComponent(popup);
        addSemantic.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(table.getValue() == null) {
                    getWindow().showNotification("Nie je označený riadok v tabuľke");
                } else {
                popup.setPopupVisible(true);
                }

            }
        });
    }

    public void setPhrases(List<Phrase> phrases) {
        this.phrases=phrases;
        fillTable();
    }

    private void fillTable() {
        

        table.removeAllItems();
        
        
        
        

        // set a style name, so we can style rows and cells
        table.setStyleName("iso3166");

        // size
        table.setWidth(Sizeable.SIZE_UNDEFINED, 0);
        table.setHeight("170px");

        // selectable
        //table.setSelectable(true);

        table.setImmediate(true); // react at once when something is selected

        table.addContainerProperty("Poradie", Integer.class, null);
        table.addContainerProperty("ID", String.class, null);
        table.addContainerProperty("Slová", String.class, null);
        table.addContainerProperty("Tag", String.class, null);
        table.addContainerProperty("Sémantika", String.class, null);
        table.addContainerProperty("Status", String.class, null);

        // connect data source
        int i =1;
        for (Phrase p : phrases) {
            table.addItem(new Object[]{
                        i,  p.getID(),p.toString(),p.getGrammarTag(), p.getSemantic(), p.getValidityStatus()}, new Integer(i));
            i++;

        }

        table.setRowHeaderMode(Table.ROW_HEADER_MODE_ICON_ONLY);
        
        
    }

    private  class AddSemanticPopUp implements Content {
        
    private VerticalLayout root = new VerticalLayout();

        public AddSemanticPopUp() {
            root.setSizeUndefined();
            root.setSpacing(true);
            root.setMargin(true);


            final TextField semanticAttributeField = new TextField("Semantický atribút:");
            semanticAttributeField.setImmediate(true);
            

            root.addComponent(semanticAttributeField);

            
            Button savePhrase = new Button("Pridaj");
            
            savePhrase.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

               if(semanticAttributeField.getValue() == null) {
                  getWindow().showNotification("Nie je vyplnený atribút");
               } else {
                   
                   
                   Object rowSelected =table.getValue();
                   String selectedPhraseID = (String)table.getContainerProperty(rowSelected,"ID").getValue();
                   Phrase selectedPhrase = null;
                   for (Phrase p : phrases) {
                       if (p.getID().equals(selectedPhraseID)) {
                           selectedPhrase = p;
                       }
                   } 
                   selectedPhrase.addSemantic((String)semanticAttributeField.getValue());
                   //corpus.trySaveAnaphora((String)tokensCombo.getValue(),(String)phrasesCombo.getValue(), threeLastSentences);
                   popup.setPopupVisible(false);
                   fillTable();
                   
               }
               
               
            }

              
        });
            
            root.addComponent(savePhrase);
            root.setComponentAlignment(savePhrase, Alignment.MIDDLE_CENTER);
/*
            root.addComponent(new Label(
                    "The changes made to any components inside the popup are "
                    + "reflected automatically when the popup is closed, but you "
                    + "might want to provide explicit action buttons for the "
                    + "user, like \"Save\" or \"Close\"."));

            root.addComponent(tf);
*/

       
        }
         @Override
         public String getMinimizedValueAsHTML() {
            return "";
        }

        public Component getPopupComponent() {
            return root;
        }



    }
    };
   
    

