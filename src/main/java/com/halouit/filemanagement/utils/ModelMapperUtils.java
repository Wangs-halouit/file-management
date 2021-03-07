package com.halouit.filemanagement.utils;

import org.modelmapper.ModelMapper;

public class ModelMapperUtils {
    private static final ModelMapper mapper = new ModelMapper();
    public static <D,T> T mapTo(D origin,Class<T> destination){
        return mapper.map(origin,destination);
    }
}
