/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin.windows;

import com.example.vaadin.BushBank;
import com.example.vaadin.corpusManager.NxtCorpusManager;
import com.example.vaadin.user.UserManager;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author Mato
 */
public class LoginWindow extends Window {
    
    private Button btnLogin = new Button("Login");
    private TextField login = new TextField ( "Prihlasovacie meno");
    private TextField password = new TextField ( "Heslo");
    UserManager manager;
    
    public LoginWindow(final UserManager manager,final BushBank app)
    {
        super("Prihlásenie");
        setName ( "login" );
        initUI();
        center();
        btnLogin.addListener ( new Button.ClickListener()
        {
            public void buttonClick ( Button.ClickEvent event )
            {
                if(manager.checkLogin((String)login.getValue(),(String)password.getValue())) {
                    app.loginChecked();
                    clearForms();
                } else {
                    showNotification("Zlé meno alebo heslo.");
                    clearForms();
                }
                
            }
        });
    }
    


    private void initUI ()
    {
        password.setSecret ( true );
        VerticalLayout vl = new VerticalLayout();
        setSizeFull();
        addComponent(vl);
        vl.setSizeFull();
        vl.setStyleName("popupMiddle");
        Label label = new Label ("Pred použitím aplikácie sa prihlás");
        label.setWidth(null);
        vl.addComponent ( label );
        vl.addComponent ( login );
        vl.addComponent ( password );
        vl.addComponent ( btnLogin );

    }
    
    private void clearForms() {
        login.setValue("");
        password.setValue("");
    }
}
