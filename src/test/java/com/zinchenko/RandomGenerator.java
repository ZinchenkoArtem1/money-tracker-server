package com.zinchenko;

import uk.co.jemos.podam.api.PodamFactoryImpl;

public class RandomGenerator {

    public <T> T random(Class<T> type) {
        return new PodamFactoryImpl().manufacturePojo(type);
    }
}
