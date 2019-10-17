package com.example.codechallenge.utils;

public class Validations {
    //Validation class can validate string and object. These methods helps every where:)
    public static boolean isStringNull(String str){
        return str == null;
    }

    public static boolean isStringEmpty(String str){
        return str.isEmpty();
    }

    public static boolean isStringNotEmptyAndNull(String str){
        if(!isStringNull(str))
            return !isStringEmpty(str);
        return false;
    }

    public static boolean isObjectNotNull(Object obj) {
        return (obj != null);
    }

    public static boolean isObjectNotEmpty(Object obj) {
        return (!obj.equals(""));
    }

    public static boolean isObjectNotEmptyAndNull(Object obj) {
        return ( obj != null &&!obj.equals(""));
    }
}
