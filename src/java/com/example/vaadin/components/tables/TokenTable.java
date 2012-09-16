/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.components.tables;

import com.vaadin.data.Property;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bushbank.bushbank.core.Token;

/**
 *
 * @author Mato
 */
public final class TokenTable extends VerticalLayout {

    Table table = new Table();
    List<Token> tokens;
    HashSet<Object> markedRows = new HashSet<Object>();

    public TokenTable(List<Token> tokens) {
        this.tokens = tokens;
        final Label value = new Label("Tokeny");
        value.setStyleName("tableTitle");
        
        setSpacing(true);

        addComponent(value);
        setComponentAlignment(value, Alignment.TOP_CENTER);
        addComponent(table);
        setComponentAlignment(table, Alignment.TOP_CENTER);
       
        fillTable();
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
        fillTable();
    }

    public void fillTable() {

        table.removeAllItems();

        

        // set a style name, so we can style rows and cells
        table.setStyleName("iso3166");

        // size
        table.setWidth(Sizeable.SIZE_UNDEFINED, 0);
        table.setHeight("170px");

        // selectable
        //table.setSelectable(true);

        table.setImmediate(true); // react at once when something is selected

        table.addContainerProperty("Poradie", String.class, null);
        table.addContainerProperty("Slovo", String.class, null);
        table.addContainerProperty("Morfológia", String.class, null);
        // connect data source
        int i =1;
        for (Token t : tokens) {
            table.addItem(new Object[]{
                        i, t.getWordForm(), t.getMorphology()}, new Integer(i));
            i++;

        }

        table.setRowHeaderMode(Table.ROW_HEADER_MODE_ICON_ONLY);
        


    }
}
