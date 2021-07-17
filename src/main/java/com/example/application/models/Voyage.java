package com.example.application.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voyage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String otobus;
    private String kalkisGunu;
    private String kalkisSaati;
    private String kalkisNoktasi;
    private String varisNoktasi;
    private String fiyat;

    @ManyToOne
    private SystemUser systemUser;
}
