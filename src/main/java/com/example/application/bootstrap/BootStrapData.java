package com.example.application.bootstrap;

import com.example.application.models.*;
import com.example.application.services.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootStrapData implements CommandLineRunner {


    private final SystemUserService systemUserService;
    private final BusService busService;
    private final TerminalService terminalService;
    private final VoyageService voyageService;

    public BootStrapData(SystemUserService systemUserService, BusService busService, TerminalService terminalService, VoyageService voyageService) {

        this.systemUserService = systemUserService;
        this.busService = busService;
        this.terminalService = terminalService;
        this.voyageService = voyageService;
    }


    @Override
    public void run(String... args) throws Exception {

        SystemUser systemUser=new SystemUser();
        systemUser.setEmail("Edirne Turizm");
        systemUser.setPassword("1881");
        systemUserService.save(systemUser);

        SystemUser systemUser2=new SystemUser();
        systemUser2.setEmail("Truva Turizm");
        systemUser2.setPassword("1938");
        systemUserService.save(systemUser2);


        Terminal terminal =new Terminal();
        terminal.setName("Çanakkale Otogar");
        terminal.setBoylam("135151351531");
        terminal.setEnlem("1586156186");
        terminal.setSystemUser(systemUser);
        terminalService.save(terminal);

        Terminal terminal2 =new Terminal();
        terminal2.setName("Lapseki Otogar");
        terminal2.setBoylam("135151351531");
        terminal2.setEnlem("1586156186");
        terminal2.setSystemUser(systemUser);
        terminalService.save(terminal2);

        Terminal terminal3 =new Terminal();
        terminal3.setName("İstanbul Otogar");
        terminal3.setBoylam("135151351531");
        terminal3.setEnlem("1586156186");
        terminal3.setSystemUser(systemUser2);
        terminalService.save(terminal3);

        Terminal terminal4 =new Terminal();
        terminal4.setName("Edirne Otogar");
        terminal4.setBoylam("135151351531");
        terminal4.setEnlem("1586156186");
        terminal4.setSystemUser(systemUser2);
        terminalService.save(terminal4);


        Bus bus = new Bus();
        bus.setKapasite("75");
        bus.setMarka("Mercedes");
        bus.setPlaka("17 RL 403");
        bus.setModel("2+1");
        bus.setSystemUser(systemUser);
        busService.save(bus);

        Bus bus2 = new Bus();
        bus2.setKapasite("55");
        bus2.setMarka("Mercedes");
        bus2.setPlaka("17 RL 405");
        bus2.setModel("2+1");
        bus2.setSystemUser(systemUser2);
        busService.save(bus2);


        Voyage voyage = new Voyage();
        voyage.setOtobus(bus.getPlaka());
        voyage.setKalkisNoktasi(terminal.getName());
        voyage.setKalkisGunu("Pazartesi");
        voyage.setFiyat("50");
        voyage.setKalkisSaati("15:30");
        voyage.setVarisNoktasi(terminal2.getName());
        voyage.setSystemUser(systemUser);
        voyageService.save(voyage);

        Voyage voyage2 = new Voyage();
        voyage2.setOtobus(bus2.getPlaka());
        voyage2.setKalkisNoktasi(terminal3.getName());
        voyage2.setKalkisGunu("Salı");
        voyage2.setFiyat("50");
        voyage2.setKalkisSaati("17:30");
        voyage2.setVarisNoktasi(terminal4.getName());
        voyage2.setSystemUser(systemUser2);
        voyageService.save(voyage2);









    }
}
