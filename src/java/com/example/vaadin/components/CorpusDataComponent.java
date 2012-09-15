/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.components;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.bushbank.bushbank.core.Sentence;
import java.util.HashSet;
import java.util.Set;

import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;

import com.vaadin.event.Action;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.CellStyleGenerator;
import com.vaadin.ui.VerticalLayout;
import org.bushbank.bushbank.core.Token;


/**
 *
 * @author Mato
 */
public class CorpusDataComponent extends VerticalLayout{
    
    Sentence sentence;
    Table table = new Table("ISO-3166 Country Codes and flags");

    HashSet<Object> markedRows = new HashSet<Object>();
    
    
    public CorpusDataComponent(Sentence s) {
        sentence=s;
        draw();
        
    }
     public void setSentence(Sentence s) {
        sentence=s;
        table.removeAllItems();
        draw();
        
    }
    
    public void draw() {
        
        removeAllComponents();
         final Label value = new Label(sentence.toString());
         value.setWidth(Sizeable.SIZE_UNDEFINED, 0);
         addComponent(value);
         setComponentAlignment(value, Alignment.TOP_CENTER);
         drawTable();
         
         
    }
    
   




     public void drawTable() {
        addComponent(table);

        // Label to indicate current selection
        final Label selected = new Label("No selection");
        addComponent(selected);

        // set a style name, so we can style rows and cells
        table.setStyleName("iso3166");

        // size
        table.setWidth("100%");
        table.setHeight("170px");

        // selectable
        //table.setSelectable(true);

        table.setImmediate(true); // react at once when something is selected

        // connect data source
        for(Token t : sentence.getTokens()) {
             table.addItem(t);
        }
 
        // turn on column reordering and collapsing
        table.setColumnReorderingAllowed(true);
        table.setColumnCollapsingAllowed(true);

        // set column headers
        //table.setColumnHeaders(new String[] { "Poradie", "slovo"});





        // Column width
        //table.setColumnExpandRatio(ExampleUtil.iso3166_PROPERTY_NAME, 1);
        //table.setColumnWidth( 70);

        // Collapse one column - the user can make it visible again
        //table.setColumnCollapsed(ExampleUtil.iso3166_PROPERTY_FLAG, true);

        // show row header w/ icon
        table.setRowHeaderMode(Table.ROW_HEADER_MODE_ICON_ONLY);
        //table.setItemIconPropertyId(ExampleUtil.iso3166_PROPERTY_FLAG);

        // Actions (a.k.a context menu)
        

        // style generator


        // listen for valueChange, a.k.a 'select' and update the label
        table.addListener(new Table.ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                // in multiselect mode, a Set of itemIds is returned,
                // in singleselect mode the itemId is returned directly
                Set<?> value = (Set<?>) event.getProperty().getValue();
                if (null == value || value.size() == 0) {
                    selected.setValue("No selection");
                } else {
                    selected.setValue("Selected: " + table.getValue());
                }
            }
        });

    }

    
    
   
    
}
