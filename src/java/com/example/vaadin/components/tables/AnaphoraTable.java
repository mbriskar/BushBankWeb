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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import org.bushbank.bushbank.core.Anaphora;
import org.bushbank.bushbank.core.Phrase;
import org.bushbank.bushbank.core.Sentence;

/**
 * Responsible for: 1.Title above the table 2.Table 3.Buttons under the table
 *
 * @author Mato
 */
public class AnaphoraTable extends VerticalLayout {

    List<Anaphora> anaphoras;
    Table table = new Table();


    
    private static String tableTitleCss ="tableTitle";

    public AnaphoraTable( Sentence thisSentence, final NxtCorpusManager corpus) {

        this.anaphoras = thisSentence.getAnaphoras();
        setSpacing(true);
        final Label value = new Label("Anafory");
        addComponent(value);
        value.setStyleName(tableTitleCss);
        setComponentAlignment(value, Alignment.TOP_CENTER);

        table.setWidth(Sizeable.SIZE_UNDEFINED, 0);
        table.setHeight("170px");
        table.setImmediate(true); // react at once when something is selected
        table.addContainerProperty("Poradie", Integer.class, null);
        table.addContainerProperty("ID", String.class, null);
        table.addContainerProperty("Pointer", String.class, null);
        table.addContainerProperty("Target", String.class, null);
        table.setSelectable(true);
        addComponent(table);
        setComponentAlignment(table, Alignment.MIDDLE_CENTER);
        fillTable();

        //buttons
        HorizontalLayout buttons = new HorizontalLayout();
        addComponent(buttons);   

        Button deleteAnaphora = new Button("odeber Anaforu");
        buttons.addComponent(deleteAnaphora);
        buttons.setComponentAlignment(deleteAnaphora, Alignment.MIDDLE_RIGHT);


        deleteAnaphora.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

                Anaphora selectedAnaphora = getSelectedAnaphora();
                if (selectedAnaphora == null) {
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
        Anaphora selectedAnaphora = null;
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

    /*
     * Called from application when sentence is changed. The table has to be filled
     * again.
     */
    public void sentenceChanged(Sentence thisSentence) {
        this.anaphoras = thisSentence.getAnaphoras();
        fillTable();
        //popup.setContent(new AnaphoraSavePopUp(threeLastSentences));
    }

    /*
     * Fill the table. Function is called after every change.
     */
    private void fillTable() {
        table.removeAllItems();

        // connect data source
        int i = 1;
        for (Anaphora a : anaphoras) {
            table.addItem(new Object[]{
                        i, a.getId(), a.getPointer().getWordForm(), a.getTarget().toString()}, new Integer(i));
            i++;

        }
        table.setRowHeaderMode(Table.ROW_HEADER_MODE_ICON_ONLY);
    }

   
}