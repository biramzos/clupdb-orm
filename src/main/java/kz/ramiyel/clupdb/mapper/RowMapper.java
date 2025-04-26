package kz.ramiyel.clupdb.mapper;

import kz.ramiyel.clupdb.util.StringUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class RowMapper<T> {
    private final Class<T> clazz;

    public RowMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T mapRow(ResultSet rs) {
        if (Objects.isNull(clazz)) {
            return null;
        }
        try {
            T instance = clazz.newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                try {
                    if (isAliasExist(rs, field.getName())) {
                        Object value = rs.getObject(field.getName(), field.getType());
                        if (Objects.nonNull(value)) {
                            getSetter(clazz, field).invoke(instance, value);
                        }
                    }
                } catch (Exception ignored) {}
            }
            return instance;
        } catch (Exception ignored) {
            return null;
        }
    }

    private static boolean isAliasExist(ResultSet rs, String alias) throws SQLException {
        return Objects.nonNull(rs.getObject(alias));
    }

    private static <T> Method getSetter(Class<T> clazz, Field field) throws NoSuchMethodException {
        return clazz.getMethod("set" + StringUtil.capitalize(field.getName()), field.getType());
    }

    private static <T> Method getGetter(Class<T> clazz, Field field) throws NoSuchMethodException {
        return clazz.getMethod(field.getType() == boolean.class ? "is" : "get" + StringUtil.capitalize(field.getName()), field.getType());
    }
}
