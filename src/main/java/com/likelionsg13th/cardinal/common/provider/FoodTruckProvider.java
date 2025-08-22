package com.likelionsg13th.cardinal.common.provider;

public class FoodTruckProvider implements CategoryProvider{
    @Override
    public boolean hasCategory(String category) {
        return false;
    }

    @Override
    public Object getMapMarkersByCategory() {
        return null;
    }
}
