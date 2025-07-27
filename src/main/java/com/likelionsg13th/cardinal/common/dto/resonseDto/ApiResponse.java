package com.likelionsg13th.cardinal.common.dto.resonseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
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