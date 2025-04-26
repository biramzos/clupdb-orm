package kz.ramiyel.clupdb.model;

import java.util.List;

public class TableModel {
    private String name;
    private String comment;
    private String schema;
    private List<ColumnModel> columns;
    private List<IndexModel> indexes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public List<ColumnModel> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnModel> columns) {
        this.columns = columns;
    }

    public List<IndexModel> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<IndexModel> indexes) {
        this.indexes = indexes;
    }
}
