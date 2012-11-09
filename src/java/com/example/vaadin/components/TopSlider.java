/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.components;


import com.example.vaadin.BushBank;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Slider;
import com.vaadin.ui.Slider.ValueOutOfBoundsException;
import com.vaadin.ui.VerticalLayout;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("serial")

public class TopSlider extends VerticalLayout {

    final Slider slider;
     final Label value;
    private static String topSentenceNumberCss = "topSentenceNumber";
    
    public TopSlider(int max,final BushBank app) {
        setSpacing(true);
        setWidth("100%");

        value = new Label("1");
        value.setStyleName(topSentenceNumberCss);
        value.setWidth("3em");

        slider = new Slider("Vyber vetu od 1 do " + max);
        slider.setWidth("100%");
        slider.setMin(1);
        slider.setMax(max);
        slider.setImmediate(true);
        slider.addListener(new ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                value.setValue(((Double)event.getProperty().getValue()).intValue());
                app.sentenceChanged(((Double)event.getProperty().getValue()).intValue());
            }
        });

        
        addComponent(slider);
        setExpandRatio(slider, 1);
        addComponent(drawButtons());

    }
    
    private HorizontalLayout drawButtons() {
        HorizontalLayout lay = new HorizontalLayout();
        
        Button prev = new Button("předešlý");
        Button next = new Button("následující");
        
        prev.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    slider.setValue(((Double)slider.getValue()) - 1);
                } catch (ValueOutOfBoundsException ex) {
                    Logger.getLogger(TopSlider.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        next.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    slider.setValue(((Double)slider.getValue()) + 1);
                } catch (ValueOutOfBoundsException ex) {
                    Logger.getLogger(TopSlider.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        lay.setSizeFull();
        lay.addComponent(prev);
        lay.setComponentAlignment(prev, Alignment.TOP_LEFT);
        lay.addComponent(value);
        lay.setComponentAlignment(value, Alignment.BOTTOM_CENTER);
        lay.addComponent(next);
        lay.setComponentAlignment(next, Alignment.TOP_RIGHT);
        
        return lay;
    }

}
