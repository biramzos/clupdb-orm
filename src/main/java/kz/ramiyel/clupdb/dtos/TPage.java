package kz.ramiyel.clupdb.dtos;

import java.util.List;

public class TPage<T> {
    private List<T> items;
    private long total = 0;

    public TPage() {}

    public TPage(List<T> items, long total) {
        this.items = items;
        this.total = total;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
