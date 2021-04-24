package com.example.lab2.request.reserve;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Valid
public class ReserveRequest {

    @NotNull(message = "图书编号不能为空!")
    private String uniqueBookMark;
}
