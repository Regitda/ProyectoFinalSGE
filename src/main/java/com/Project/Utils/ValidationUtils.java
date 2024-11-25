package com.Project.Utils;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final String NAME_PATTERN = "^[A-Za-zÀ-ÿ\\s'-]{1,100}$";
    private static final String PHONE_PATTERN = "^\\+?[0-9\\s-]{7,15}$";
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String URL_PATTERN = "(https:\\/\\/www\\.|http:\\/\\/www\\.|https:\\/\\/|http:\\/\\/)?[a-zA-Z0-9]{2,}(\\.[a-zA-Z0-9]{2,}){1,2}";

    // Works with https://www.bing.com Im an not regex expert.

    private static final Pattern NAME_REGEX = Pattern.compile(NAME_PATTERN);
    private static final Pattern PHONE_REGEX = Pattern.compile(PHONE_PATTERN);
    private static final Pattern EMAIL_REGEX = Pattern.compile(EMAIL_PATTERN);
    private static final Pattern URL_REGEX = Pattern.compile(URL_PATTERN);

    public static boolean isValidName(String name) {
        if (name == null) return false;
        return NAME_REGEX.matcher(name.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        return PHONE_REGEX.matcher(phone.trim()).matches();
    }

    public static boolean isValidUrl(String url) {
        if (url == null) return false;
        return URL_REGEX.matcher(url.trim()).matches();
    }

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return EMAIL_REGEX.matcher(email.trim()).matches();
    }
}