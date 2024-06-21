package com.example.entbasebe.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class UserDTO {
    String UserName;
    String Icon;
    String UserEmail;
    String IsAdmin;
}
