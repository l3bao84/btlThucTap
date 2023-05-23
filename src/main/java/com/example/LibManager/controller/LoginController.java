package com.example.LibManager.controller;

import com.example.LibManager.repositories.BookRepository;
import com.example.LibManager.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final CategoryRepository categoryRepository;

    private final BookRepository bookRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
