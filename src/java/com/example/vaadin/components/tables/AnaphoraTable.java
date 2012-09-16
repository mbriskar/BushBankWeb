/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.components.tables;

import com.example.vaadin.components.TopSlider;
import com.example.vaadin.corpusManager.NxtCorpusManager;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Slider;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bushbank.bushbank.core.Anaphora;
import org.bushbank.bushbank.core.Phrase;
import org.bushbank.bushbank.core.Sentence;
import org.bushbank.bushbank.core.Token;

/**
 *
 * @author Mato
 */
public class AnaphoraTable extends VerticalLayout {

    List<Anaphora> anaphoras;
    Table table = new Table();
    final PopupView popup;

    public AnaphoraTable(List<Anaphora> anaphoras,List<Sentence> threeLastSentences) {
        this.anaphoras = anaphoras;
        setSpacing(true);
        final Label value = new Label("Anafory");
        addComponent(value);
        value.setStyleName("tableTitle");

        setComponentAlignment(value, Alignment.TOP_CENTER);
        addComponent(table);
        setComponentAlignment(table, Alignment.MIDDLE_CENTER);
        Button addAnaphora = new Button("přidej Anaforu");
        addComponent(addAnaphora);
        fillTable();
        
        popup = new PopupView(new PopupTextField(threeLastSentences));

        popup.setHideOnMouseOut(false);
        addComponent(popup);
        addAnaphora.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

                popup.setPopupVisible(true);

            }
        });

        
    }

    public void sentenceChanged(List<Anaphora> anaphoras,List<Sentence> threeLastSentences) {
        this.anaphoras = anaphoras;
        fillTable();
        popup.setContent(new PopupTextField(threeLastSentences));
        
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
        table.addContainerProperty("Token", String.class, null);
        table.addContainerProperty("Fráza", String.class, null);


        // connect data source
        int i = 1;
        for (Anaphora a : anaphoras) {
            table.addItem(new Object[]{
                        i, a.getToken().getWordForm(), a.getPhrase().toString()}, new Integer(i));
            i++;

        }

        table.setRowHeaderMode(Table.ROW_HEADER_MODE_ICON_ONLY);
        
        


    }

    public class PopupTextField implements PopupView.Content {

        private VerticalLayout root = new VerticalLayout();

        public PopupTextField(List<Sentence> threeLastSentences) {
            root.setSizeUndefined();
            root.setSpacing(true);
            root.setMargin(true);

            ComboBox tokensCombo = new ComboBox("Vyber token");
            for(Sentence s : threeLastSentences) {
                for (Token t : s.getTokens()) {
                    //TODO: iba zámená
                    tokensCombo.addItem(t.getWordForm());
                }
            }
            
            ComboBox phrasesCombo = new ComboBox("Vyber frázu");
            Sentence thisSentence = threeLastSentences.get(threeLastSentences.size() - 1);
            
                for (Phrase p : thisSentence.getPhrases()) {
                    //TODO: iba zámená
                    phrasesCombo.addItem(p.toString());
                }
            

            HorizontalLayout horizontalTokenPart = new HorizontalLayout();
            TextField editor = new TextField("Zadaj chýbajúci token:");
            Label label = new Label("alebo");
            horizontalTokenPart.addComponent(tokensCombo);
            horizontalTokenPart.addComponent(label);
            horizontalTokenPart.setSpacing(true);
            horizontalTokenPart.setComponentAlignment(label, Alignment.BOTTOM_CENTER);
            horizontalTokenPart.addComponent(editor);
            root.addComponent(horizontalTokenPart);

            root.addComponent(phrasesCombo);
/*
            root.addComponent(new Label(
                    "The changes made to any components inside the popup are "
                    + "reflected automatically when the popup is closed, but you "
                    + "might want to provide explicit action buttons for the "
                    + "user, like \"Save\" or \"Close\"."));

            root.addComponent(tf);
*/
        }
        
        public void valueChange(ValueChangeEvent event) {
        getWindow().showNotification("Selected city: " + event.getProperty());

         }


        public String getMinimizedValueAsHTML() {
            return "";
        }

        public Component getPopupComponent() {
            return root;
        }
    };
}