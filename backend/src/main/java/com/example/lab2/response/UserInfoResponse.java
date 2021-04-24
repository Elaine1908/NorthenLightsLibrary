package com.example.lab2.response;

import com.example.lab2.dto.BorrowedBookCopyDTO;
import com.example.lab2.dto.ReservedBookCopyDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserInfoResponse{
    private String username;
    private int credit;
    private String role;
    private List<BorrowedBookCopyDTO> borrowedBooks;
    private List<ReservedBookCopyDTO> reservedBooks;
    /**
     * 根据用户个人信息创建response
     *
     */
    public UserInfoResponse(String username, int credit, String role) {
        this.username = username;
        this.credit = credit;
        this.role = role;
    }
}