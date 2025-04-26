package kz.ramiyel.clupdb.type;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum ColumnType {
    INTEGER("INT"),
    VARCHAR("VARCHAR"),
    VARCHAR_16("VARCHAR(16)"),
    VARCHAR_32("VARCHAR(32)"),
    VARCHAR_64("VARCHAR(64)"),
    VARCHAR_128("VARCHAR(128)"),
    VARCHAR_256("VARCHAR(256)"),
    VARCHAR_255("VARCHAR(255)"),
    VARCHAR_512("VARCHAR(512)"),
    VARCHAR_1024("VARCHAR(1024)"),
    VARCHAR_2048("VARCHAR(2048)"),
    VARCHAR_4096("VARCHAR(4096)"),
    DOUBLE("DOUBLE"),
    DATE("DATE"),
    TIME("TIME"),
    BOOLEAN("BOOLEAN"),
    TEXT("TEXT"),
    TIMESTAMP("TIMESTAMP"),
    DATETIME("DATETIME"),
    CHAR("CHAR"),
    BIGINT("BIGINT"),
    FLOAT("FLOAT");

    private final String sqlType;
    private static final Map<String, ColumnType> typeToEnum =
            Arrays.stream(values()).collect(Collectors.toMap(item -> item.sqlType, item -> item));

    ColumnType(String sqlType) {
        this.sqlType = sqlType;
    }

    public static ColumnType parseFrom(String type) {
        return typeToEnum.getOrDefault(type.toUpperCase(), null);
    }

    public String getSqlType() {
        return sqlType;
    }
}
