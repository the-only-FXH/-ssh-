package com.shuangyulin_QQ287307421.domain;

public class LoanInfo {
    /*借阅编号*/
    private int loadId;
    public int getLoadId() {
        return loadId;
    }
    public void setLoadId(int loadId) {
        this.loadId = loadId;
    }

    /*图书对象*/
    private Book book;
    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }

    /*读者对象*/
    private Reader reader;
    public Reader getReader() {
        return reader;
    }
    public void setReader(Reader reader) {
        this.reader = reader;
    }

    /*借阅时间*/
    private String borrowDate;
    public String getBorrowDate() {
        return borrowDate;
    }
    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    /*归还时间*/
    private String returnDate;
    public String getReturnDate() {
        return returnDate;
    }
    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

}