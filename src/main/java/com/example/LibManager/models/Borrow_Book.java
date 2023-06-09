package com.example.LibManager.models;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "borrow_book")
@NoArgsConstructor
public class Borrow_Book {

    @EmbeddedId
    private BorrowBookKey borrowBookKey = new BorrowBookKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("borrowID")
    @JoinColumn(name = "borrowID")
    private Borrow borrow;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookID")
    @JoinColumn(name = "bookID")
    private Book book;

    @Column(name = "borrowDay")
    private LocalDate borrowDay;

    @Column(name = "returnDay")
    private LocalDate returnDay;

    @Column(name = "status")
    private String status;

    @Column(name = "borrowFee")
    private double borrowFee;


    public Borrow_Book(Borrow borrow, Book book, LocalDate borrowDay, LocalDate returnDay, double borrowFee, String status) {
        this.borrow = borrow;
        this.book = book;
        this.borrowDay = borrowDay;
        this.returnDay = returnDay;
        this.borrowFee = borrowFee;
        this.status = status;
    }

    public BorrowBookKey getBorrowBookKey() {
        return borrowBookKey;
    }

    public void setBorrowBookKey(BorrowBookKey borrowBookKey) {
        this.borrowBookKey = borrowBookKey;
    }

    public Borrow getBorrow() {
        return borrow;
    }

    public void setBorrow(Borrow borrow) {
        this.borrow = borrow;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getBorrowDay() {
        return borrowDay;
    }

    public void setBorrowDay(LocalDate borrowDay) {
        this.borrowDay = borrowDay;
    }

    public LocalDate getReturnDay() {
        return returnDay;
    }

    public void setReturnDay(LocalDate returnDay) {
        this.returnDay = returnDay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getBorrowFee() {
        return borrowFee;
    }

    public void setBorrowFee(double borrowFee) {
        this.borrowFee = borrowFee;
    }
}
