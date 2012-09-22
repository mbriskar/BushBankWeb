/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.components.tables;

import com.example.vaadin.corpusManager.NxtCorpusManager;
import com.vaadin.data.Property;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import org.bushbank.bushbank.core.Anaphora;
import org.bushbank.bushbank.core.Phrase;
import org.bushbank.bushbank.core.Sentence;
import org.bushbank.bushbank.core.Token;

/**
 * Responsible for: 1.Title above the table 2.Table 3.Buttons under the table
 *
 * @author Mato
 */
public class AnaphoraTable extends VerticalLayout {

    List<Anaphora> anaphoras;
    Table table = new Table();
    final PopupView popup;
    NxtCorpusManager corpus;

    public AnaphoraTable(List<Sentence> threeLastSentences,final NxtCorpusManager corpus) {
        this.corpus=corpus;
        Sentence thisSentence = threeLastSentences.get(threeLastSentences.size() -1);
        this.anaphoras = thisSentence.getAnaphoras();
        setSpacing(true);
        final Label value = new Label("Anafory");
        addComponent(value);
        value.setStyleName("tableTitle");
        
        table.setWidth(Sizeable.SIZE_UNDEFINED, 0);
        table.setHeight("170px");
        table.setImmediate(true); // react at once when something is selected
        table.addContainerProperty("Poradie", Integer.class, null);
        table.addContainerProperty("ID", String.class, null);
        table.addContainerProperty("Token", String.class, null);
        table.addContainerProperty("Fráza", String.class, null);
        table.setSelectable(true);

        setComponentAlignment(value, Alignment.TOP_CENTER);
        addComponent(table);
        setComponentAlignment(table, Alignment.MIDDLE_CENTER);
        fillTable();
        //buttons
        HorizontalLayout buttons = new HorizontalLayout();
        addComponent(buttons);

        Button addAnaphora = new Button("přidej Anaforu");
        buttons.addComponent(addAnaphora);
        buttons.setComponentAlignment(addAnaphora, Alignment.MIDDLE_LEFT);
        Button deleteAnaphora = new Button("odeber Anaforu");
        buttons.addComponent(deleteAnaphora);
        buttons.setComponentAlignment(deleteAnaphora, Alignment.MIDDLE_RIGHT);
        
        
        popup = new PopupView(new AnaphoraSavePopUp(threeLastSentences));
        popup.setHideOnMouseOut(false);
        addComponent(popup);
        addAnaphora.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                popup.setPopupVisible(true);
            }
        });    
        
        deleteAnaphora.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                
                Anaphora selectedAnaphora = getSelectedAnaphora();
                if(selectedAnaphora == null) {
                    getWindow().showNotification("Nie je označený riadok v tabuľke");
                } else {
                  corpus.deleteAnaphora(selectedAnaphora);
                anaphoras.remove(selectedAnaphora);
                fillTable();  
                }
                
            }
        });  
    }
    
    public Anaphora getSelectedAnaphora() {
        Object rowSelected = table.getValue();
        Property containerProperty = table.getContainerProperty(rowSelected, "ID");
        String selectedAnaphoraID;
        Anaphora selectedAnaphora =null;
        if (containerProperty != null) {
            selectedAnaphoraID = (String) containerProperty.getValue();
        } else {
            return null;
        }
        Phrase selectedPhrase = null;
        for (Anaphora a : anaphoras) {
            if (a.getId().equals(selectedAnaphoraID)) {
                selectedAnaphora = a;
            }
        }

        return selectedAnaphora;
        
        
    }

    public void sentenceChanged(List<Sentence> threeLastSentences) {
        Sentence thisSentence = threeLastSentences.get(threeLastSentences.size() -1);
        this.anaphoras = thisSentence.getAnaphoras();   
        fillTable();
        popup.setContent(new AnaphoraSavePopUp(threeLastSentences));
    }
    
  
    private void fillTable() {
        table.removeAllItems();

        // connect data source
        int i = 1;
        for (Anaphora a : anaphoras) {
            table.addItem(new Object[]{
                        i, a.getId(),a.getToken().getWordForm(), a.getPhrase().toString()}, new Integer(i));
            i++;

        }
        table.setRowHeaderMode(Table.ROW_HEADER_MODE_ICON_ONLY);
    }

    public class AnaphoraSavePopUp implements PopupView.Content{

        private VerticalLayout root = new VerticalLayout();

        public AnaphoraSavePopUp(final List<Sentence> threeLastSentences) {
            root.setSizeUndefined();
            root.setSpacing(true);
            root.setMargin(true);

            final ComboBox tokensCombo = new ComboBox("Vyber token");
            for(Sentence s : threeLastSentences) {
                for (Token t : s.getTokens()) {
                    //TODO: iba zámená
                    
                    tokensCombo.addItem(t.getWordForm());
                }
            }
            
            final ComboBox phrasesCombo = new ComboBox("Vyber frázu");
            final Sentence thisSentence = threeLastSentences.get(threeLastSentences.size() - 1);
            
                for (Phrase p : thisSentence.getPhrases()) {
                    //TODO: iba zámená
                    phrasesCombo.addItem(p.toString());
                }
            
            final Label selectToken = new Label("Token");
            selectToken.setStyleName("tableTitle");
            root.addComponent(selectToken);
            HorizontalLayout horizontalTokenPart = new HorizontalLayout();            
            Label label = new Label("alebo chýbajúci token");
            horizontalTokenPart.addComponent(tokensCombo);
            horizontalTokenPart.addComponent(label);
            horizontalTokenPart.setSpacing(true);
            horizontalTokenPart.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
            final TextField wordForm = new TextField("Slovo:");
            wordForm.setImmediate(true);           
            VerticalLayout missingTokenLayout = new VerticalLayout();
            missingTokenLayout.addComponent(wordForm);           
            horizontalTokenPart.addComponent(missingTokenLayout);           
            root.addComponent(horizontalTokenPart);           
            final Label selectPhrase = new Label("Fráza");
            selectPhrase.setStyleName("tableTitle");
            root.addComponent(selectPhrase);
            root.addComponent(phrasesCombo);            
            Button savePhrase = new Button("Vytvor");
            
            savePhrase.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
               if((tokensCombo.getValue() != null) && (!wordForm.getValue().equals("")) ) {
                  getWindow().showNotification("Je vybraný token a zároveň vyplnený aj chýbajúci token");
               } else if (tokensCombo.getValue() != null){
                   corpus.trySaveAnaphora((String)tokensCombo.getValue(),(String)phrasesCombo.getValue(), threeLastSentences);
                   popup.setPopupVisible(false);                   
               } else {                   
                   corpus.trySaveAnaphora((String)wordForm.getValue(),(String)phrasesCombo.getValue(),thisSentence);
                   popup.setPopupVisible(false);
               }   
            } 
        });
            
            root.addComponent(savePhrase);
            root.setComponentAlignment(savePhrase, Alignment.MIDDLE_CENTER);
        }

        public String getMinimizedValueAsHTML() {
            return "";
        }
        
        public Component getPopupComponent() {
            return root;
        }
    };
}