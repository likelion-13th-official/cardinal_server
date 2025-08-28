package com.likelionsg13th.cardinal.common.exception;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;

public class LocationNotProvidedForFoodTruck extends BusinessException {
    public LocationNotProvidedForFoodTruck(ErrorCode errorCode) {
        super(errorCode);
    }
}
