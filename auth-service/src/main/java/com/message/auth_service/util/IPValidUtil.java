package com.message.auth_service.util;

import java.util.regex.Pattern;

public interface IPValidUtil {

    String email_regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,10}$";
    String username_regex = "[A-Za-z0-9_]{3,10}";
    String password_regex = "(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=!_])(?=\\S+$).{6,15}";

    public static boolean validateEmail(String email) {
        return Pattern.matches(email_regex, email);
    }
    public static boolean validateUserName(String username) {
        return Pattern.matches(username_regex, username);
    }
    public static boolean validatePassword(String password) {
        return Pattern.matches(password_regex, password);
    }
}
