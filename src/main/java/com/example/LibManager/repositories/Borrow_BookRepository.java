package com.example.LibManager.repositories;

import com.example.LibManager.models.BorrowBookKey;
import com.example.LibManager.models.Borrow_Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Borrow_BookRepository extends JpaRepository<Borrow_Book, String> {
    Optional<Borrow_Book> findByBorrowBookKey(BorrowBookKey borrowBookKey);
}
