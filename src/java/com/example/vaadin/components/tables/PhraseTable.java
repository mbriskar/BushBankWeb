/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.components.tables;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
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
import java.util.Set;
import org.bushbank.bushbank.core.Phrase;

/**
 * Responsible for: 1.Title above the table 
 *                  2.Table 
 *                  3.Buttons under the table
 *
 * @author Mato
 */
public class PhraseTable extends VerticalLayout {

    List<Phrase> phrases;
    Table table = new Table();
    final PopupView removeSemanticPopup;
    final PopupView addSemanticPopup;
    private static String cssTableTitle = "tableTitle";

    /*
     * Create title,table,buttons. 
     */
    public PhraseTable(List<Phrase> phrases) {
        this.phrases = phrases;
        setSpacing(true);
        final Label value = new Label("Frázy");
        addComponent(value);
        setComponentAlignment(value, Alignment.TOP_CENTER);
        value.setStyleName(cssTableTitle);
        
        //init table
        addComponent(table);
        table.setSelectable(true);
        table.setStyleName("iso3166");
        table.setWidth(Sizeable.SIZE_UNDEFINED, 0);
        table.setHeight("170px");
        table.setImmediate(true); // react at once when something is selected
        table.addContainerProperty("Poradie", Integer.class, null);
        table.addContainerProperty("ID", String.class, null);
        table.addContainerProperty("Slová", String.class, null);
        table.addContainerProperty("Tag", String.class, null);
        table.addContainerProperty("Sémantika", String.class, null);
        table.addContainerProperty("Status", String.class, null);
        table.addListener(new Table.ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {

                ((DeleteSemanticPopUp) removeSemanticPopup.getContent()).rowChanged();

            }
        });
        setComponentAlignment(table, Alignment.MIDDLE_CENTER);

        //init all the buttons
        HorizontalLayout buttons = new HorizontalLayout();
        addComponent(buttons);
        buttons.setSizeFull();
        //1. Button for adding semantic attribute
        Button addSemantic = new Button("přidej sémantický atribút");
        buttons.addComponent(addSemantic);
        buttons.setComponentAlignment(addSemantic, Alignment.MIDDLE_LEFT);
        addSemanticPopup = new PopupView(new PhraseTable.AddSemanticPopUp());
        addSemanticPopup.setHideOnMouseOut(false);
        addComponent(addSemanticPopup);
        addSemantic.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (table.getValue() == null) {
                    getWindow().showNotification("Nie je označený riadok v tabuľke");
                } else {
                    addSemanticPopup.setPopupVisible(true);
                }

            }
        });

        //2. Button for deleting semantic attribute
        Button removeSemantic = new Button("odstraň sémantický atribút");
        buttons.addComponent(removeSemantic);
        buttons.setComponentAlignment(removeSemantic, Alignment.MIDDLE_RIGHT);
        removeSemanticPopup = new PopupView(new PhraseTable.DeleteSemanticPopUp());
        removeSemanticPopup.setHideOnMouseOut(false);
        addComponent(removeSemanticPopup);
        removeSemantic.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (table.getValue() == null) {
                    getWindow().showNotification("Nie je označený riadok v tabuľke");
                } else {
                    removeSemanticPopup.setPopupVisible(true);
                }
            }
        });
        fillTable();

    }

    /*
     * called when phrases are changed. Table is filled again.
     */
    public void setPhrases(List<Phrase> phrases) {
        this.phrases = phrases;
        fillTable();
    }

    /*
     * Fill the table. Function is called after every change.
     */
    private void fillTable() {
        table.removeAllItems();
        int i = 1;
        for (Phrase p : phrases) {
            table.addItem(new Object[]{
                        i, p.getID(), p.toString(), p.getGrammarTag(), p.getSemantic(), p.getValidityStatus()}, new Integer(i));
            i++;

        }
        table.setRowHeaderMode(Table.ROW_HEADER_MODE_ICON_ONLY);
    }

    /*
     * Returns the already selected phrase in table.
     */
    private Phrase getSelectedPhrase() {
        Object rowSelected = table.getValue();
        Property containerProperty = table.getContainerProperty(rowSelected, "ID");
        String selectedPhraseID;
        if (containerProperty != null) {
            selectedPhraseID = (String) containerProperty.getValue();
        } else {
            return null;
        }
        Phrase selectedPhrase = null;
        for (Phrase p : phrases) {
            if (p.getID().equals(selectedPhraseID)) {
                selectedPhrase = p;
            }
        }

        return selectedPhrase;
    }

    /*
     * Popup content of adding semantic attribute.
     */
    private class AddSemanticPopUp implements Content {

        private VerticalLayout root = new VerticalLayout();

        public AddSemanticPopUp() {
            root.setSizeUndefined();
            root.setSpacing(true);
            root.setMargin(true);
            final TextField semanticAttributeField = new TextField("Semantický atribút:");
            semanticAttributeField.setImmediate(true);
            root.addComponent(semanticAttributeField);
            Button savePhrase = new Button("Pridaj");
            root.addComponent(savePhrase);
            root.setComponentAlignment(savePhrase, Alignment.MIDDLE_CENTER);
            savePhrase.addListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {

                    if (semanticAttributeField.getValue() == null) {
                        getWindow().showNotification("Nie je vyplnený atribút");
                    } else {
                        Phrase selectedPhrase = getSelectedPhrase();
                        selectedPhrase.addSemantic((String) semanticAttributeField.getValue());
                        //semanticAttributeField.setValue(null);
                        addSemanticPopup.setPopupVisible(false);
                        fillTable();
                    }
                }
            });
        }

        @Override
        public String getMinimizedValueAsHTML() {
            return "";
        }

        public Component getPopupComponent() {
            return root;
        }
    }

    /*
     * Popup content of deleting semantic attribute.
     */
    private class DeleteSemanticPopUp implements Content {

        private VerticalLayout root = new VerticalLayout();
        private Phrase selectedPhrase;
        private ComboBox attributesCombo = new ComboBox("Vyber atribút");

        public DeleteSemanticPopUp() {
            root.setSizeUndefined();
            root.setSpacing(true);
            root.setMargin(true);
            root.addComponent(attributesCombo);
            Button deleteAttribute = new Button("Odstráň");
            root.addComponent(deleteAttribute);
            root.setComponentAlignment(deleteAttribute, Alignment.MIDDLE_CENTER);
            deleteAttribute.addListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {

                    if (attributesCombo.getValue() == null) {
                        getWindow().showNotification("Nie je vyplnený atribút");
                    } else {

                        selectedPhrase.removeSemantic((String) attributesCombo.getValue());
                        removeSemanticPopup.setPopupVisible(false);
                        fillTable();
                    }
                }
            });
        }

        private void fillSemanticAttributes() {
            attributesCombo.removeAllItems();
            selectedPhrase = getSelectedPhrase();
            if (selectedPhrase != null) {
                for (String attribute : selectedPhrase.getSemantic()) {
                    attributesCombo.addItem(attribute);
                }
            }
        }

        public void rowChanged() {
            fillSemanticAttributes();
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
