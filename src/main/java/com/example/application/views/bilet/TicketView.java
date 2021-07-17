package com.example.application.views.bilet;


import com.example.application.models.*;
import com.example.application.services.TicketService;
import com.example.application.services.VoyageService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.example.application.views.MainLayout;
import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Bilet Satış")
@Route(value = "bilet", layout = MainLayout.class)
public class TicketView extends VerticalLayout {

    private final VoyageService voyageService;
    private final TicketService ticketService;
    Grid<Voyage> voyageGrid = new Grid<>(Voyage.class);
    Long loggedInSystemUserId;
    Binder<Voyage> voyageBinder=new Binder<>();
    Binder<Ticket> ticketBinder = new Binder<>();
    Dialog dialogVoyage = new Dialog();
    Long itemIdForEdition=0L;
    Long itemIdForEdition2=0L;

    public TicketView(VoyageService voyageService,TicketService ticketService){
        this.voyageService = voyageService;
        this.ticketService = ticketService;


        if (VaadinSession.getCurrent().getSession().getAttribute("LoggedInSystemUserId")==null){
            UI.getCurrent().getPage().setLocation("/login");
        }else {

            System.out.println("Loged in User ID");
            System.out.println(VaadinSession.getCurrent().getSession().getAttribute("LoggedInSystemUserId").toString());
            loggedInSystemUserId=Long.valueOf(VaadinSession.getCurrent().getSession().getAttribute("LoggedInSystemUserId").toString());
        }

        dialogVoyage.setModal(true);

        TextField txtCustomerName= new TextField("Ad","İlk Adınızı Giriniz");
        TextField txtCustomerSurname= new TextField("Soyad","Soyadınızı Giriniz");
        TextField txtCustomerEmail= new TextField("E-Mail","E-mailinizi Giriniz");
        TextField txtCustomerSeferC= new TextField();
        TextField txtCustomerSeferV= new TextField();
        TextField txtCustomerOtobus= new TextField();
        TextField txtCustomerFiyat= new TextField();
        TextField txtCustomerGun= new TextField();

        ticketBinder.bind(txtCustomerName,Ticket::getAd,Ticket::setAd);
        ticketBinder.bind(txtCustomerSurname,Ticket::getSoyad,Ticket::setSoyad);
        ticketBinder.bind(txtCustomerEmail,Ticket::getEmail,Ticket::setEmail);
        voyageBinder.bind(txtCustomerSeferC,Voyage::getKalkisNoktasi,Voyage::setKalkisNoktasi);
        voyageBinder.bind(txtCustomerSeferV,Voyage::getVarisNoktasi,Voyage::setVarisNoktasi);
        voyageBinder.bind(txtCustomerOtobus,Voyage::getOtobus,Voyage::setOtobus);
        voyageBinder.bind(txtCustomerFiyat,Voyage::getFiyat,Voyage::setFiyat);
        voyageBinder.bind(txtCustomerGun,Voyage::getKalkisGunu,Voyage::setKalkisGunu);


        Button btnBookingSave = new Button("Satış");
        Button btnBookingCancel = new Button("İptal");

        btnBookingSave.addClickListener(buttonClickEvent -> {
            Ticket ticket = new Ticket();
            try {
                ticketBinder.writeBean(ticket);
            } catch (ValidationException l) {
                l.printStackTrace();
            }
            ticket.setId(itemIdForEdition2);
            SystemUser loggedInSystemUser=new SystemUser();
            loggedInSystemUser.setId(loggedInSystemUserId);
            ticket.setSystemUser(loggedInSystemUser);
            ticket.setCikisNoktasi(txtCustomerSeferC.getValue());
            ticket.setVarisNoktasi(txtCustomerSeferV.getValue());
            ticket.setOtobus(txtCustomerOtobus.getValue());
            ticket.setFiyat(txtCustomerFiyat.getValue());
            ticket.setTarih(txtCustomerGun.getValue());

            ticketService.save(ticket);
            dialogVoyage.close();


        });
        btnBookingCancel.addClickListener(buttonClickEvent -> {
            dialogVoyage.close();
        });

        FormLayout formLayout7 = new FormLayout();
        formLayout7.add(txtCustomerName,txtCustomerSurname,txtCustomerEmail);
        HorizontalLayout horizontalLayout7=new HorizontalLayout();
        horizontalLayout7.setSpacing(true);
        horizontalLayout7.add(btnBookingCancel, btnBookingSave);
        dialogVoyage.add(new H3("Bilet"),formLayout7,horizontalLayout7);




        add(voyageGrid);

        refreshVoyage();


        voyageGrid.removeColumnByKey("id");
        voyageGrid.setSelectionMode(Grid.SelectionMode.NONE);
        voyageGrid.setColumns("otobus","kalkisGunu","kalkisSaati","kalkisNoktasi","varisNoktasi","fiyat");
        voyageGrid.addComponentColumn(itemVoyage -> uptdltVoyageButton(voyageGrid,itemVoyage
        )).setHeader("Actions");
    }



    private void refreshVoyage(){
        List<Voyage> voyages = new ArrayList<>(voyageService.getList());
        voyageGrid.setItems(voyages);
    }
    private Component uptdltVoyageButton(Grid<Voyage> voyageGrid, Voyage itemVoyage) {

        Button btnBooking = new Button("Bilet Al");
        btnBooking.addClickListener(buttonClickEvent -> {
            itemIdForEdition=itemVoyage.getId();
            ticketBinder.readBean(new Ticket());
            voyageBinder.readBean(itemVoyage);
            dialogVoyage.open();

        });

        return btnBooking;
    }
}
