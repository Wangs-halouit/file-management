package com.halouit.filemanagement.utils;

import java.util.UUID;

public class UuidUtils {
    public static String getId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
