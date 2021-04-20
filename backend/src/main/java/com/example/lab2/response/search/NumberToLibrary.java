package com.example.lab2.response.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NumberToLibrary {

    private String libraryName;
    long number;


    public void numberIncrement() {
        number += 1;
    }
}
