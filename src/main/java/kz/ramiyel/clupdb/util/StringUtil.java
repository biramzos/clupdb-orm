package kz.ramiyel.clupdb.util;

public class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static boolean equalsIgnoreCase(String a, String b) {
        return (a == null && b == null) || (a != null && a.equalsIgnoreCase(b));
    }

    public static boolean equalsNullableString(String a, String b) {
        return (a == null ? "" : a).trim().equalsIgnoreCase((b == null ? "" : b).trim());
    }

}
