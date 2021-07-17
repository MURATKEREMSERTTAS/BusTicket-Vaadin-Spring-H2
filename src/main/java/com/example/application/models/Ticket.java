package com.example.application.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ad;
    private String soyad;
    private String email;
    private String cikisNoktasi;
    private String VarisNoktasi;
    private String otobus;
    private String tarih;
    private String fiyat;

    @ManyToOne
    private SystemUser systemUser;
}
