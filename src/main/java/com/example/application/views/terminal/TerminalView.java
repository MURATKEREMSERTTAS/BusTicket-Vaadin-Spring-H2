package com.example.application.views.terminal;

import com.example.application.models.SystemUser;
import com.example.application.models.Terminal;
import com.example.application.services.TerminalService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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

import java.util.ArrayList;
import java.util.List;

@PageTitle("Terminal İşlemleri")
@Route(value = "terminal", layout = MainLayout.class)
public class TerminalView extends VerticalLayout {
    private final TerminalService terminalService;
    TextField txtFilter = new TextField();
    Grid<Terminal>terminalGrid=new Grid<>(Terminal.class);
    Dialog dialogTerminal =new Dialog();
    Binder<Terminal>terminalBinder=new Binder<>();
    Long loggedInSystemUserId;
    Long itemIdForEdition=0L;


    public TerminalView(TerminalService terminalService) {
        this.terminalService = terminalService;

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
            refreshTerminal(txtFilter.getValue());
        });

        HorizontalLayout filterGroup = new HorizontalLayout();
        filterGroup.add(txtFilter,btnFilter);


        dialogTerminal.setModal(true);

        TextField txtTerminalName = new TextField("Terminal Adı","Terminal Adını Giriniz");
        TextField txtTerminalEnlem = new TextField("Terminal Enlem","Terminal'in Enlem Koordinatını Giriniz");
        TextField txtTerminalBoylam = new TextField("Terminal Boylam","Terminal'in Boylam Koordinatını Giriniz");


        terminalBinder.bind(txtTerminalName,Terminal::getName,Terminal::setName);
        terminalBinder.bind(txtTerminalEnlem,Terminal::getEnlem,Terminal::setEnlem);
        terminalBinder.bind(txtTerminalBoylam,Terminal::getBoylam,Terminal::setBoylam);

        Button btnSaveT =new Button("Save");
        Button btnCancelT =new Button("Cancel");

        btnSaveT.addClickListener(buttonClickEvent ->{
            Terminal terminal = new Terminal();
            try {
                terminalBinder.writeBean(terminal);
            } catch (ValidationException d) {
                d.printStackTrace();
            }
            terminal.setId(itemIdForEdition);
            SystemUser loggedInSystemUser=new SystemUser();
            loggedInSystemUser.setId(loggedInSystemUserId);
            terminal.setSystemUser(loggedInSystemUser);

            terminalService.save(terminal);
            refreshTerminal(txtFilter.getValue().toString());
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
        dialogTerminal.add(new H3("Terminal"),formLayout3,terminalLayout);

        Button btnTerminal =new Button("Terminal Oluştur", VaadinIcon.INSERT.create());
        btnTerminal.addClickListener(buttonClickEvent -> {
            terminalBinder.readBean(new Terminal());
            dialogTerminal.open();

        });

        Button btnLogOut = new Button("Çıkış Yap");
        btnLogOut.addClickListener(buttonClickEvent -> {
            VaadinSession.getCurrent().getSession().invalidate();
            UI.getCurrent().getPage().setLocation("/login");
        });
        HorizontalLayout terminalLayout2 = new HorizontalLayout();
        terminalLayout2.setSpacing(true);
        terminalLayout2.add(btnLogOut,btnTerminal);


        add(filterGroup,terminalLayout2,terminalGrid);

        refreshTerminal(txtFilter.getValue().toString());


        terminalGrid.removeColumnByKey("id");
        terminalGrid.setSelectionMode(Grid.SelectionMode.NONE);
        terminalGrid.setColumns("name","enlem","boylam");
        terminalGrid.addComponentColumn(itemTerminal -> uptdltTerminalButton(terminalGrid,itemTerminal
        )).setHeader("Actions");

    }


    private void refreshTerminal(String filter) {
        List<Terminal> terminals = new ArrayList<>();
        terminals.addAll(terminalService.getList(filter,loggedInSystemUserId));
        terminalGrid.setItems(terminals);
    }

    private HorizontalLayout uptdltTerminalButton(Grid<Terminal> terminalGrid, Terminal itemTerminal) {
        @SuppressWarnings("unchecked")
        Button btnDeleteTerminal = new Button("Sil");
        btnDeleteTerminal.addClickListener(buttonClickEvent -> {
            //Notification.show("Delete item clicked on :" + item.getName());

            ConfirmDialog dialogVoyage = new ConfirmDialog("Silmeyi Onayla",
                    "Kaydı silmek isteğinize emin misiniz?", "Sil", confirmEvent -> {
                terminalService.delete(itemTerminal);
                refreshTerminal(txtFilter.getValue().toString());
            },
                    "İptal", cancelEvent -> {

            });
            dialogVoyage.setConfirmButtonTheme("error primary");

            dialogVoyage.open();
        });

        Button btnUpdateTerminal = new Button("Güncelle");
        btnUpdateTerminal.addClickListener(buttonClickEvent -> {
            itemIdForEdition = itemTerminal.getId();
            terminalBinder.readBean(itemTerminal);
            dialogTerminal.open();
        });


        HorizontalLayout btnActionGroup = new HorizontalLayout();
        btnActionGroup.setSpacing(true);
        btnActionGroup.add(btnUpdateTerminal, btnDeleteTerminal);

        return btnActionGroup;
    }

}
