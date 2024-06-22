package com.kob.backend.context;

public class UserContext {
    private static final ThreadLocal<String> userTypeHolder = new ThreadLocal<>();

    public static void setUserType(String userType) {
        userTypeHolder.set(userType);
    }

    public static String getUserType() {
        return userTypeHolder.get();
    }

    public static void clear() {
        userTypeHolder.remove();
    }
}
