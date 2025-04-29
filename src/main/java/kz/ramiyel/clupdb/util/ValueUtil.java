package kz.ramiyel.clupdb.util;

import kz.ramiyel.clupdb.model.QuerySelection;

public class ValueUtil {

    public static String formatValue(Object value) {
        if (value instanceof String) {
            return "'" + value + "'";
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
