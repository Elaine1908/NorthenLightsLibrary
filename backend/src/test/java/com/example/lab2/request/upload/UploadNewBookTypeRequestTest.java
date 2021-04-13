package com.example.lab2.request.upload;

import org.junit.jupiter.api.Test;

import javax.swing.text.DateFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UploadNewBookTypeRequestTest {

    @Test
    void getIsbn() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd ");
        String s = "2011-07-09 ";
        Date date = formatter.parse(s);
        System.out.println(" = " +date);


    }
}