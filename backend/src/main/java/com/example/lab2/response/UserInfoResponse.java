package com.example.lab2.response;

import com.example.lab2.dto.BookDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserInfoResponse extends GeneralResponse {
    private String username;
    private int credit;
    private String role;
    private List<BookDTO> bookList;

    public UserInfoResponse(String username, int credit, String role) {
        this.username = username;
        this.credit = credit;
        this.role = role;
    }
}