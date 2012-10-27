/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.components.tables;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.HashSet;
import java.util.List;
import org.bushbank.bushbank.core.Token;

/**
 * Responsible for: 1.Title above the table 
 *                  2.Table 
 *                  3.Buttons under the table
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

    /*
     * Fill the table. Function is called after every change.
     */
    public void fillTable() {
        table.removeAllItems();
        table.setStyleName("iso3166");
        table.setWidth(Sizeable.SIZE_UNDEFINED, 0);
        table.setHeight("170px");
        table.setImmediate(true);
        
        table.addContainerProperty("Poradie", String.class, null);
        table.addContainerProperty("ID", String.class, null);
        table.addContainerProperty("Slovo", String.class, null);
        table.addContainerProperty("Morfológia", String.class, null);
        int i =1;
        for (Token t : tokens) {
            table.addItem(new Object[]{
                        i,t.getID(), t.getWordForm(), t.getMorphology()}, new Integer(i));
            i++;

        }
        table.setRowHeaderMode(Table.ROW_HEADER_MODE_ICON_ONLY);
    }
}
