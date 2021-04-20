package com.example.lab2.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
