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
public class Campus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long campusID;

    @Column(unique = true)
    private String campusName;

    public Campus(String campusName) {
        this.campusName = campusName;
    }
}
