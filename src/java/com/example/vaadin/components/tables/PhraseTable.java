/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.components.tables;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import org.bushbank.bushbank.core.Phrase;
import org.bushbank.bushbank.core.Token;

/**
 *
 * @author Mato
 */
public class PhraseTable extends VerticalLayout {

    List<Phrase> phrases;
    Table table = new Table();
    
    public PhraseTable(List<Phrase> phrases) {
        this.phrases=phrases;
        setSpacing(true);
        final Label value = new Label("Frázy");
        addComponent(value);
        setComponentAlignment(value, Alignment.TOP_CENTER);
        addComponent(table);
        setComponentAlignment(table, Alignment.MIDDLE_CENTER);
        fillTable();
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
        table.setWidth("30%");
        table.setHeight("170px");

        // selectable
        //table.setSelectable(true);

        table.setImmediate(true); // react at once when something is selected

        table.addContainerProperty("Poradie", Integer.class, null);
        table.addContainerProperty("Slová", String.class, null);
        table.addContainerProperty("Tag", String.class, null);
        table.addContainerProperty("Sémantika", String.class, null);
        table.addContainerProperty("Status", String.class, null);

        // connect data source
        int i =1;
        for (Phrase p : phrases) {
            table.addItem(new Object[]{
                        i, p.getTokens().toString(), p.getGrammarTag(), p.getSemantic(), p.getValidityStatus()}, new Integer(i));
            i++;

        }

        table.setRowHeaderMode(Table.ROW_HEADER_MODE_ICON_ONLY);
        
        
    }
    
}
