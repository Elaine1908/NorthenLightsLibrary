package com.example.lab2.request.sensitive;

import com.example.lab2.entity.Library;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddToSensitiveRequest {
    private List<String> addToSensitiveList;
}
