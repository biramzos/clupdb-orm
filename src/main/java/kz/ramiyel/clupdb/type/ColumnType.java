package kz.ramiyel.clupdb.type;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum ColumnType {
    INTEGER("INT", "integer", "int"),
    VARCHAR("VARCHAR", "character varying", "varchar"),
    VARCHAR_16("VARCHAR(16)", "character varying(16)", "varchar(16)"),
    VARCHAR_32("VARCHAR(32)", "character varying(32)", "varchar(32)"),
    VARCHAR_64("VARCHAR(64)", "character varying(64)", "varchar(64)"),
    VARCHAR_128("VARCHAR(128)", "character varying(128)", "varchar(128)"),
    VARCHAR_256("VARCHAR(256)", "character varying(256)", "varchar(256)"),
    VARCHAR_255("VARCHAR(255)", "character varying(255)", "varchar(255)"),
    VARCHAR_512("VARCHAR(512)", "character varying(512)", "varchar(512)"),
    VARCHAR_1024("VARCHAR(1024)", "character varying(1024)", "varchar(1024)"),
    VARCHAR_2048("VARCHAR(2048)", "character varying(2048)", "varchar(2048)"),
    VARCHAR_4096("VARCHAR(4096)", "character varying(4096)", "varchar(4096)"),
    DOUBLE("DOUBLE", "double precision", "double"),
    DATE("DATE", "date"),
    TIME("TIME", "time"),
    BOOLEAN("BOOLEAN", "boolean"),
    TEXT("TEXT", "text"),
    TIMESTAMP("TIMESTAMP", "timestamp"),
    DATETIME("DATETIME", "timestamp without time zone"),
    CHAR("CHAR", "character"),
    BIGINT("BIGINT", "bigint"),
    FLOAT("FLOAT", "real");

    private final String sqlType;
    private final String[] filters;

    private static final Map<String, ColumnType> typeToEnum =
            Arrays.stream(values())
                    .flatMap(columnType -> Arrays.stream(columnType.filters)
                            .map(filter -> new AbstractMap.SimpleEntry<>(filter.toUpperCase(), columnType)))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    ColumnType(String sqlType, String... filters) {
        this.sqlType = sqlType;
        this.filters = filters;
    }

    public static ColumnType parseFrom(String type) {
        return typeToEnum.getOrDefault(type.toUpperCase(), null);
    }

    public String getSqlType() {
        return sqlType;
    }
}
