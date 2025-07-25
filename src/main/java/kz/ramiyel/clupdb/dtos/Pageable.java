package kz.ramiyel.clupdb.dtos;

public class Pageable {
    private int countInPage;
    // Page number starts from 1
    private int pageNumber;

    public Pageable(int countInPage, int pageNumber) {
        this.countInPage = countInPage;
        this.pageNumber = pageNumber;
    }

    public int getCountInPage() {
        return countInPage;
    }

    public void setCountInPage(int countInPage) {
        this.countInPage = countInPage;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public static Pageable of(int countInPage, int pageNumber) {
        return new Pageable(countInPage, pageNumber);
    }

    public static Pageable of(int countInPage) {
        return new Pageable(countInPage, 1);
    }

    @Override
    public String toString() {
        return " LIMIT " + ((pageNumber - 1) * countInPage) + ", " + countInPage;
    }
}
