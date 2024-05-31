package com.smart.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDTO {
    private String name;
    private String secondName;
    private String email;
    private String phone;
    private String work;

    @Max(value = 1000, message = "Description should not be more than 1000 characters long")
    private String description;
}
