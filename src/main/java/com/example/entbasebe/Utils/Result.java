package com.example.entbasebe.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Boolean success;
    private String errorMsg;
    private Object data;
    private Long total;

    public static Result ok(){
        return new Result(true, null, null, null);
    }
    public static Result ok(Object data){
        return new Result(true, null, data, null);
    }
    public static Result ok(List<?> data, Long total){
        return new Result(true, null, data, total);
    }
    public static Result fail(String errorMsg){
        return new Result(false, errorMsg, null, null);
    }

    public static ResponseEntity<byte[]> fail(String status, String message) {
        Map<String, String> json = new HashMap<>();
        json.put("status", status);
        json.put("message", message);
        try {
            byte[] bytes = new ObjectMapper().writeValueAsBytes(json);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(bytes);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
