package com.example.application.views.rapor;

import com.example.application.models.Ticket;
import com.example.application.services.TicketService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Rapor İşlemleri")
@Route(value = "rapor",layout = MainLayout.class)
public class RaporView extends VerticalLayout {
    private final TicketService ticketService;
    Grid<Ticket>ticketGrid =new Grid<>(Ticket.class);
    Long loggedInSystemUserId;
    TextField txtFilter = new TextField();

    public RaporView(TicketService ticketService) {
        this.ticketService = ticketService;

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

        Button btnLogOut = new Button("Çıkış Yap");
        btnLogOut.addClickListener(buttonClickEvent -> {
            VaadinSession.getCurrent().getSession().invalidate();
            UI.getCurrent().getPage().setLocation("/login");
        });



        add(filterGroup,btnLogOut,ticketGrid);

        refreshTerminal(txtFilter.getValue().toString());


        ticketGrid.removeColumnByKey("id");
        ticketGrid.setSelectionMode(Grid.SelectionMode.NONE);
        ticketGrid.setColumns("ad","soyad","email","cikisNoktasi","varisNoktasi","otobus","tarih","fiyat");

    }

    private void refreshTerminal(String filter) {
        List<Ticket> tickets = new ArrayList<>();
        tickets.addAll(ticketService.getList(filter,loggedInSystemUserId));
        ticketGrid.setItems(tickets);
    }
}
