package com.example.lab2.entity;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long libraryID;

    @Column(unique = true)
    private String libraryName;

    public Library(String libraryName) {
        this.libraryName = libraryName;
    }
}
