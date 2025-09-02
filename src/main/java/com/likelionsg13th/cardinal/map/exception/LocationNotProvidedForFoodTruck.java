package com.likelionsg13th.cardinal.map.exception;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.BusinessException;

public class LocationNotProvidedForFoodTruck extends BusinessException {
    public LocationNotProvidedForFoodTruck(ErrorCode errorCode) {
        super(errorCode);
    }
}
