package com.example.application.views.firma;

import com.example.application.models.*;
import com.example.application.services.*;
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
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.example.application.views.MainLayout;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Firma İşlemleri")
@Route(value = "firma", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class FirmaView extends VerticalLayout {

    private final BusService busService;
    private final TerminalService terminalService;
    private final VoyageService voyageService;
    private final SystemUserService systemUserService;

    Grid<Bus> busGrid=new Grid<>(Bus.class);
    TextField txtFilter = new TextField();
    Dialog dialogBus=new Dialog();
    Dialog dialogTerminal=new Dialog();
    Dialog dialogVoyage=new Dialog();
    Binder<Bus> busBinder=new Binder<>();
    Binder<Terminal>terminalBinder =new Binder<>();
    Binder<Voyage>voyageBinder =new Binder<>();
    ComboBox<Terminal> terminalBox = new ComboBox<>();
    ComboBox<Terminal> terminalBox2 = new ComboBox<>();
    ComboBox<Bus> busComboBox = new ComboBox<>();
    Long itemIdForEdition=0L;
    Long loggedInSystemUserId;


    public FirmaView(BusService busService, TerminalService terminalService,
                     VoyageService voyageService, SystemUserService systemUserService){
        this.busService = busService;
        this.terminalService=terminalService;
        this.voyageService=voyageService;
        this.systemUserService=systemUserService;

        if (VaadinSession.getCurrent().getSession().getAttribute("LoggedInSystemUserId")==null){
            UI.getCurrent().getPage().setLocation("/login");
        }else {

            System.out.println("Loged in User ID");
            System.out.println(VaadinSession.getCurrent().getSession().getAttribute("LoggedInSystemUserId").toString());
            loggedInSystemUserId=Long.valueOf(VaadinSession.getCurrent().getSession().getAttribute("LoggedInSystemUserId").toString());
        }


        txtFilter.setPlaceholder("Key");
        Button btnFilter = new Button("Ara", VaadinIcon.SEARCH.create());
        btnFilter.addClickListener(buttonClickEvent -> {
            refreshBus(txtFilter.getValue());
        });

        HorizontalLayout filterGroup = new HorizontalLayout();
        filterGroup.add(txtFilter,btnFilter);



        dialogBus.setModal(true);

        TextField txtBusPlate =new TextField("Plaka","Plaka Numarasını Giriniz");
        TextField txtBusCapacity =new TextField("Kapasite","Otobüsün Kapasitesini Giriniz");
        TextField txtBusType =new TextField("Marka","Otobüsün Markasını Giriniz");
        TextField txtModel =new TextField("Model","Marka Modelini Giriniz");


        busBinder.bind(txtBusPlate,Bus::getPlaka,Bus::setPlaka);
        busBinder.bind(txtBusCapacity,Bus::getKapasite,Bus::setKapasite);
        busBinder.bind(txtBusType,Bus::getMarka,Bus::setMarka);
        busBinder.bind(txtModel,Bus::getModel,Bus::setModel);


        Button btnSave2 =new Button("Kaydet");
        Button btnCancel2 =new Button("İptal");

        btnSave2.addClickListener(buttonClickEvent ->{
            Bus bus = new Bus();
            try {
                busBinder.writeBean(bus);
            } catch (ValidationException c) {
                c.printStackTrace();
            }
            bus.setId(itemIdForEdition);
            SystemUser loggedInSystemUser=new SystemUser();
            loggedInSystemUser.setId(loggedInSystemUserId);

            bus.setSystemUser(loggedInSystemUser);

            busService.save(bus);
            refreshBus(txtFilter.getValue().toString());
            refreshBus2(txtFilter.getValue().toString());
            dialogBus.close();
        } );
        btnCancel2.addClickListener(buttonClickEvent -> {
            dialogBus.close();
        });

        FormLayout formLayout2 = new FormLayout();
        formLayout2.add(txtBusPlate,txtBusCapacity,txtBusType,txtModel);
        HorizontalLayout horizontalLayout2=new HorizontalLayout();
        horizontalLayout2.setSpacing(true);
        horizontalLayout2.add(btnCancel2,btnSave2);
        dialogBus.add(new H3("Otobüs"),formLayout2,horizontalLayout2);

        Button btnBus =new Button("Otobüs Ekle", VaadinIcon.INSERT.create());
        btnBus.addClickListener(buttonClickEvent ->{
            itemIdForEdition=0L;
            busBinder.readBean(new Bus());
            dialogBus.open();
        });


        dialogTerminal.setModal(true);

        TextField txtTerminalName = new TextField("Terminal Adı","Terminal Adını Giriniz");
        TextField txtTerminalEnlem = new TextField("Terminal Enlem","Terminal'in Enlem Koordinatını Giriniz");
        TextField txtTerminalBoylam = new TextField("Terminal Boylam","Terminal'in Boylam Koordinatını Giriniz");


        terminalBinder.bind(txtTerminalName,Terminal::getName,Terminal::setName);
        terminalBinder.bind(txtTerminalEnlem,Terminal::getEnlem,Terminal::setEnlem);
        terminalBinder.bind(txtTerminalBoylam,Terminal::getBoylam,Terminal::setBoylam);

        Button btnSaveT =new Button("Kaydet");
        Button btnCancelT =new Button("İptal");

        btnSaveT.addClickListener(buttonClickEvent ->{
            Terminal terminal = new Terminal();
            try {
                terminalBinder.writeBean(terminal);
            } catch (ValidationException d) {
                d.printStackTrace();
            }

            terminalService.save(terminal);
            dialogTerminal.close();
        } );

        btnCancelT.addClickListener(buttonClickEvent -> {
            dialogTerminal.close();
        });

        FormLayout formLayout3 = new FormLayout();
        formLayout3.add(txtTerminalName, txtTerminalEnlem, txtTerminalBoylam);
        HorizontalLayout terminalLayout = new HorizontalLayout();
        terminalLayout.setSpacing(true);
        terminalLayout.add(btnCancelT, btnSaveT);
        dialogTerminal.add(new H3("Yeni Terminal"),formLayout3,terminalLayout);

        Button btnTerminal =new Button("Terminal Ekle", VaadinIcon.INSERT.create());
        btnTerminal.addClickListener(buttonClickEvent -> {
            terminalBinder.readBean(new Terminal());
            dialogTerminal.open();

        });

        dialogVoyage.setModal(true);

        TextField txtVoyageKalkisGunu = new TextField("Kalkış Günü","Kalkış Gününü Giriniz");
        TextField txtVoyageKalkisSaati = new TextField("Kalkış Sati","Kalkış Saatini Giriniz");
        terminalBox.setLabel("Kalkış Terminali");
        refreshTerminal1(txtFilter.getValue().toString());
        terminalBox2.setLabel("Varış Terminali");
        refreshTerminal2(txtFilter.getValue().toString());
        TextField txtVoyagePrice = new TextField("Ücret","Bilet Fiyatını Giriniz");
        busComboBox.setLabel("Otobüs");
        refreshBus2(txtFilter.getValue().toString());





        voyageBinder.bind(txtVoyageKalkisGunu,Voyage::getKalkisGunu,Voyage::setKalkisGunu);
        voyageBinder.bind(txtVoyageKalkisSaati,Voyage::getKalkisSaati,Voyage::setKalkisSaati);
        voyageBinder.bind(txtVoyagePrice,Voyage::getFiyat,Voyage::setFiyat);

        Button btnSave4 = new Button("Kaydet");
        Button btnCancel4 = new Button("İptal");

        btnSave4.addClickListener(buttonClickEvent ->{
            Voyage voyage=new Voyage();
            try {
                voyageBinder.writeBean(voyage);
            } catch (ValidationException f) {
                f.printStackTrace();
            }
            voyage.setId(itemIdForEdition);
            SystemUser loggedInSystemUser=new SystemUser();
            loggedInSystemUser.setId(loggedInSystemUserId);
            voyage.setSystemUser(loggedInSystemUser);
            voyage.setKalkisNoktasi(terminalBox.getValue().getName());
            voyage.setVarisNoktasi(terminalBox2.getValue().getName());
            voyage.setOtobus(busComboBox.getValue().getPlaka());

            voyageService.save(voyage);
            refreshBus2(txtFilter.getValue().toString());
            dialogVoyage.close();
        } );
        btnCancel4.addClickListener(buttonClickEvent -> {
            dialogVoyage.close();
        });

        FormLayout formLayout4 = new FormLayout();
        formLayout4.add( terminalBox,terminalBox2,busComboBox,txtVoyageKalkisGunu, txtVoyageKalkisSaati, txtVoyagePrice);
        HorizontalLayout horizontalLayout4=new HorizontalLayout();
        horizontalLayout4.setSpacing(true);
        horizontalLayout4.add(btnCancel4, btnSave4);
        dialogVoyage.add(new H3("Yeni Sefer"),formLayout4,horizontalLayout4);

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



        HorizontalLayout btnGroup = new HorizontalLayout();
        btnGroup.add(btnBus,btnTerminal, btnVoyage, btnLogOut);

        add(btnGroup,filterGroup,busGrid);

        refreshBus(txtFilter.getValue().toString());


        busGrid.removeColumnByKey("id");
        busGrid.setSelectionMode(Grid.SelectionMode.NONE);
        busGrid.setColumns("marka","plaka","model","kapasite");
        busGrid.addComponentColumn(itemBus -> uptdltBusButton(busGrid,itemBus
        )).setHeader("Actions");
    }


    private void refreshBus(String filter){
        List<Bus> busList = new ArrayList<>();
        busList.addAll(busService.getList(filter,loggedInSystemUserId));
        busGrid.setItems(busList);
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

    private void onDelete(ConfirmDialog.ConfirmEvent confirmEvent) {

    }

    private HorizontalLayout uptdltBusButton(Grid<Bus> busGrid, Bus itemBus) {
        @SuppressWarnings("unchecked")
        Button btnDeleteBus = new Button("Sil");
        btnDeleteBus.addClickListener(buttonClickEvent -> {
            //Notification.show("Delete item clicked on :" + item.getName());

            ConfirmDialog dialogbus2 = new ConfirmDialog("Silmeyi Onayla",
                    "Kaydı silmek istediğinize emin misiniz?", "Sil", confirmEvent -> {
                busService.delete(itemBus);
                refreshBus(txtFilter.getValue().toString());
            },
                    "İptal", cancelEvent -> {

            });
            dialogbus2.setConfirmButtonTheme("error primary");

            dialogbus2.open();
        });

        Button btnUpdateBus = new Button("Güncelle");
        btnUpdateBus.addClickListener(buttonClickEvent -> {
            itemIdForEdition = itemBus.getId();
            busBinder.readBean(itemBus);
            dialogBus.open();
        });


        HorizontalLayout btnBookingGroup = new HorizontalLayout();
        btnBookingGroup.add(btnUpdateBus, btnDeleteBus);

        return btnBookingGroup;


    }
}
