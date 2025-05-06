package kz.ramiyel.clupdb.util;

import kz.ramiyel.clupdb.model.QuerySelection;

import java.util.List;

public class ValueUtil {

    public static String formatValue(Object value, List<Object> parameters) {
        if (value instanceof String str) {
            parameters.add(str);
            return "?";
        }
        if (value == null) {
            return "NULL";
        }
        if (value instanceof QuerySelection selection) {
            return selection.getExpression();
        }
        return String.valueOf(value);
    }

    public static String formatValue(Object value) {
        if (value instanceof String str) {
            String escaped = str.replace("'", "''");
            return "'" + escaped + "'";
        }
        if (value == null) {
            return "NULL";
        }
        if (value instanceof QuerySelection selection) {
            return selection.getExpression();
        }
        return String.valueOf(value);
    }

}
