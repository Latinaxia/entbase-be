package com.example.entbasebe.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UsersListDTO {
        Integer UserId;
        String UserName;
        String Icon;
        String UserEmail;
        String IsAdmin;
}
