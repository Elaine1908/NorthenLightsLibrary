package com.example.lab2.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void tryRegister() {
        ArrayList<String> mockedList = Mockito.mock(ArrayList.class);
        System.out.println(mockedList);
        mockedList.size();
        Mockito.verify(mockedList).size();


    }
}