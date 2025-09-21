package com.likelionsg13th.cardinal.common.dto.resonseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class ApiResponse<T> implements Serializable {
    private boolean success;
    private int code;
    private String message;
    private T data;


    public ApiResponse(boolean success,int code,String message){
        this.message = message;
        this.success = success;
        this.code = code;
    }


}