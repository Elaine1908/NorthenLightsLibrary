package com.example.lab2.request.sensitive;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RemoveFromSensitiveRequest {

    private List<String> removeFromSensitiveList;
}
