package com.example.application.views.voyage;

import com.example.application.models.Bus;
import com.example.application.models.SystemUser;
import com.example.application.models.Terminal;
import com.example.application.models.Voyage;
import com.example.application.services.BusService;
import com.example.application.services.TerminalService;
import com.example.application.services.VoyageService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.*;

@PageTitle("Sefer İşlemleri")
@Route(value = "voyage", layout = MainLayout.class)
public class VoyageView extends VerticalLayout {
    private final VoyageService voyageService;
    private final TerminalService terminalService;
    private final BusService busService;
    Grid<Voyage> voyageGrid=new Grid<>(Voyage.class);
    TextField txtFilter = new TextField();
    Binder<Voyage>voyageBinder = new Binder<>();
    Dialog dialogVoyage = new Dialog();
    ComboBox<Terminal> terminalBox = new ComboBox<>();
    ComboBox<Terminal> terminalBox2 = new ComboBox<>();
    ComboBox<Bus>busComboBox=new ComboBox<>();
    Long itemIdForEdition = 0L;
    Long loggedInSystemUserId;


    public VoyageView(VoyageService voyageService, TerminalService terminalService,BusService busService) {
        this.voyageService = voyageService;
        this.terminalService = terminalService;
        this.busService = busService;

        if (VaadinSession.getCurrent().getSession().getAttribute("LoggedInSystemUserId")==null){
            UI.getCurrent().getPage().setLocation("/login");
        }else {

            System.out.println("Loged in User ID");
            System.out.println(VaadinSession.getCurrent().getSession().getAttribute("LoggedInSystemUserId").toString());
            loggedInSystemUserId=Long.valueOf(VaadinSession.getCurrent().getSession().getAttribute("LoggedInSystemUserId").toString());
        }


        txtFilter.setPlaceholder("Key");
        Button btnFilter = new Button("Search", VaadinIcon.SEARCH.create());
        btnFilter.addClickListener(buttonClickEvent -> {
            refreshVoyage(txtFilter.getValue());
        });

        HorizontalLayout filterGroup = new HorizontalLayout();
        filterGroup.add(txtFilter,btnFilter);

        dialogVoyage.setModal(true);

        TextField txtVoyageKalkisGunu = new TextField("Kalkış Günü","Kalkış Gününü Giriniz");
        TextField txtVoyageKalkisSaati = new TextField("Kalkış Sati","Kalkış Saatini Giriniz");
        terminalBox.setLabel("Kalkış Terminali");
        refreshTerminal1(txtFilter.getValue().toString());
        terminalBox2.setLabel("Varış Terminali");
        refreshTerminal2(txtFilter.getValue().toString());
        TextField txtVoyagePrice = new TextField("Fiyat","Bilet Fiyatını Giriniz");
        busComboBox.setLabel("Otobüs");
        refreshBus2(txtFilter.getValue().toString());



        voyageBinder.bind(txtVoyageKalkisGunu,Voyage::getKalkisGunu,Voyage::setKalkisGunu);
        voyageBinder.bind(txtVoyageKalkisSaati,Voyage::getKalkisSaati,Voyage::setKalkisSaati);
        voyageBinder.bind(txtVoyagePrice,Voyage::getFiyat,Voyage::setFiyat);

        Button btnSave4 = new Button("Kaydet");
        Button btnCancel4 = new Button("İptal");
        btnCancel4.addClickListener(buttonClickEvent -> {
            dialogVoyage.close();
        });
        btnSave4.addClickListener(buttonClickEvent ->{
            Voyage voyage=new Voyage();
            try {
                voyageBinder.writeBean(voyage);
            } catch (ValidationException e) {
                e.printStackTrace();
            }
            voyage.setId(itemIdForEdition);
            SystemUser loggedInSystemUser=new SystemUser();
            loggedInSystemUser.setId(loggedInSystemUserId);
            voyage.setSystemUser(loggedInSystemUser);
            voyage.setKalkisNoktasi(terminalBox.getValue().getName());
            voyage.setVarisNoktasi(terminalBox2.getValue().getName());
            voyage.setOtobus(busComboBox.getValue().getPlaka());
            refreshTerminal1(txtFilter.getValue().toString());
            refreshTerminal2(txtFilter.getValue().toString());
            refreshBus2(txtFilter.getValue().toString());
            voyageService.save(voyage);
            refreshVoyage(txtFilter.getValue().toString());
            dialogVoyage.close();
        } );


        FormLayout formLayout4 = new FormLayout();
        formLayout4.add( terminalBox,terminalBox2,busComboBox,txtVoyageKalkisGunu, txtVoyageKalkisSaati, txtVoyagePrice);
        HorizontalLayout horizontalLayout4=new HorizontalLayout();
        horizontalLayout4.setSpacing(true);
        horizontalLayout4.add(btnCancel4, btnSave4);
        dialogVoyage.add(new H3("Sefer"),formLayout4,horizontalLayout4);

        Button btnVoyage =new Button("Sefer Oluştur", VaadinIcon.INSERT.create());
        btnVoyage.addClickListener(buttonClickEvent -> {
            voyageBinder.readBean(new Voyage());
            dialogVoyage.open();
        });

        Button btnLogOut = new Button("Çıkış Yap");
        btnLogOut.addClickListener(buttonClickEvent -> {
            VaadinSession.getCurrent().getSession().invalidate();
            UI.getCurrent().getPage().setLocation("/login");
        });
        HorizontalLayout voyageLayout2 = new HorizontalLayout();
        voyageLayout2.setSpacing(true);
        voyageLayout2.add(btnLogOut,btnVoyage);

        add(filterGroup, voyageLayout2,voyageGrid);

        refreshVoyage(txtFilter.getValue().toString());

        voyageGrid.removeColumnByKey("id");
        voyageGrid.setSelectionMode(Grid.SelectionMode.NONE);
        voyageGrid.setColumns("otobus","kalkisGunu","kalkisSaati","kalkisNoktasi","varisNoktasi","fiyat");
        voyageGrid.addComponentColumn(itemVoyage -> uptdltVoyageButton(voyageService,itemVoyage
        )).setHeader("Actions");
    }


    private void refreshVoyage(String filter){
        List<Voyage> voyages = new ArrayList<>(voyageService.getList(filter, loggedInSystemUserId));
        voyageGrid.setItems(voyages);
    }

    private void refreshTerminal1(String filter){
        List<Terminal> terminals = new ArrayList<>(terminalService.getList(filter, loggedInSystemUserId));
        terminalBox.setItemLabelGenerator(Terminal::getName);
        terminalBox.setItems(terminals);
    }

    private void refreshTerminal2(String filter){
        List<Terminal> terminals = new ArrayList<>(terminalService.getList(filter, loggedInSystemUserId));
        terminalBox2.setItemLabelGenerator(Terminal::getName);
        terminalBox2.setItems(terminals);
    }

    private void refreshBus2(String filter){
        List<Bus> buses = new ArrayList<>(busService.getList(filter, loggedInSystemUserId));
        busComboBox.setItemLabelGenerator(Bus::getPlaka);
        busComboBox.setItems(buses);
    }


    private HorizontalLayout uptdltVoyageButton(VoyageService voyageService, Voyage itemVoyage) {
        @SuppressWarnings("unchecked")
        Button btnDeleteVoyage = new Button("Sil");
        btnDeleteVoyage.addClickListener(buttonClickEvent -> {
            //Notification.show("Delete item clicked on :" + item.getName());

            ConfirmDialog dialogVoyage = new ConfirmDialog("Silmeyi Onayla",
                    "Kaydı silmek istediğinize emin misiniz?", "Sil", confirmEvent -> {
                voyageService.delete(itemVoyage);
                refreshVoyage(txtFilter.getValue().toString());
            },
                    "İptal", cancelEvent -> {

            });
            dialogVoyage.setConfirmButtonTheme("error primary");

            dialogVoyage.open();
        });

        Button btnUpdateVoyage = new Button("Güncelle");
        btnUpdateVoyage.addClickListener(buttonClickEvent -> {
            itemIdForEdition = itemVoyage.getId();
            voyageBinder.readBean(itemVoyage);
            dialogVoyage.open();
        });


        HorizontalLayout btnActionGroup = new HorizontalLayout();
        btnActionGroup.setSpacing(true);
        btnActionGroup.add(btnUpdateVoyage, btnDeleteVoyage);

        return btnActionGroup;

    }



}
