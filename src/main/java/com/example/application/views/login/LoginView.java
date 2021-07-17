package com.example.application.views.login;

import com.example.application.models.SystemUser;
import com.example.application.services.SystemUserService;
import com.example.application.services.VoyageService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@PageTitle("Giriş Sayfası")
@Route(value = "login", layout = MainLayout.class)
public class LoginView extends VerticalLayout {

    public LoginView(SystemUserService systemUserService) {
        LoginForm loginForm = new LoginForm();
        loginForm.addLoginListener(loginEvent -> {


            SystemUser result = systemUserService.login(loginEvent.getUsername(), loginEvent.getPassword());

            if (result.getId() != null) {
                VaadinSession.getCurrent().getSession().setAttribute("LoggedInSystemUserId", result.getId());
                UI.getCurrent().getPage().setLocation("/bilet");
            } else {
                loginForm.setError(true);
            }
        });

        Dialog dialogSystemUser = new Dialog();
        Binder<SystemUser> systemUserBinder =new Binder<>();

        dialogSystemUser.setModal(true);

        TextField txtUserEmail= new TextField("Kullanıcı Adı","Kullanıcı Adınızı Giriniz");
        TextField txtPassword= new TextField("Parola","Parolanızı Giriniz");

        systemUserBinder.bind(txtUserEmail,SystemUser::getEmail,SystemUser::setEmail);
        systemUserBinder.bind(txtPassword,SystemUser::getPassword,SystemUser::setPassword);

        Button btnSave5 = new Button("Kaydet");
        Button btnCancel5 = new Button("İptal");

        btnSave5.addClickListener(buttonClickEvent ->{
            SystemUser systemUser = new SystemUser();
            try {
                systemUserBinder.writeBean(systemUser);
            } catch (ValidationException k) {
                k.printStackTrace();
            }

            systemUserService.save(systemUser);
            dialogSystemUser.close();
        } );
        btnCancel5.addClickListener(buttonClickEvent -> {
            dialogSystemUser.close();
        });

        FormLayout formLayout5 = new FormLayout();
        formLayout5.add(txtUserEmail,txtPassword);
        HorizontalLayout horizontalLayout5=new HorizontalLayout();
        horizontalLayout5.setSpacing(true);
        horizontalLayout5.add(btnCancel5, btnSave5);
        dialogSystemUser.add(new H3("Yeni Firma"),formLayout5,horizontalLayout5);

        Button btnSystemUser =new Button("Katıl", VaadinIcon.INSERT.create());
        btnSystemUser.addClickListener(buttonClickEvent -> {
            systemUserBinder.readBean(new SystemUser());
            dialogSystemUser.open();
        });

        HorizontalLayout horizontalLayout6=new HorizontalLayout();
        horizontalLayout6.setSpacing(true);
        horizontalLayout6.add(loginForm,btnSystemUser);

        add(horizontalLayout6);
    }
}