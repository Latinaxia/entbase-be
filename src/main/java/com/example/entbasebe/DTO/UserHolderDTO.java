package com.example.entbasebe.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserHolderDTO {
    Integer UserId;
    String UserName;
    String Icon;
    String UserEmail;
    String IsAdmin;
}
