package com.example.LibManager.controller;

import com.example.LibManager.models.*;
import com.example.LibManager.repositories.*;
import com.example.LibManager.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;

@Controller
@RequestMapping(path = "books")
@RequiredArgsConstructor
public class BookController {

    private final PlCompanyRepository plCompanyRepository;

    private final AuthorRepository authorRepository;

    private final CategoryRepository categoryRepository;

    private final BookRepository bookRepository;

    private final Borrow_BookRepository bBRepository;

    private final StorageService service;


    @GetMapping("/detailBook/{bookID}")
    public String getDetailBook(ModelMap modelMap, @PathVariable String bookID) {
        Book book = bookRepository.findById(bookID).get();
        modelMap.addAttribute("author", authorRepository.findById(book.getAuthorID()).get());
        modelMap.addAttribute("plc", plCompanyRepository.findById(book.getPlCompanyID()).get());
        modelMap.addAttribute("categories", categoryRepository.findAll());
        modelMap.addAttribute("book", book);
        modelMap.addAttribute("bookDTO", new BookDTO());
        return "detailBook";
    }

    @GetMapping("/manageBook")
    public String manageBook(ModelMap modelMap) {

        Iterable<Borrow_Book> bbs = bBRepository.findAll();
        modelMap.addAttribute("sum", Borrow_BookController.sum);
        modelMap.addAttribute("books", bookRepository.findAll());
        modelMap.addAttribute("bbs", bbs);
        modelMap.addAttribute("bookDTO", new BookDTO());
        return "manageBook";
    }

    @PostMapping("/deleteBook/{bookID}")
    public String deleteBook(@PathVariable String bookID, ModelMap modelMap) {
        try{
            Iterable<Borrow_Book> bbs = bBRepository.findAll();
            for (Borrow_Book bb: bbs) {
                if(bb.getBorrowBookKey().getBookID().equalsIgnoreCase(bookID)) {
                    bBRepository.delete(bb);
                }
            }
            bookRepository.deleteById(bookID);
            //Iterable<Borrow_Book> bbs = bBRepository.findAll();
            modelMap.addAttribute("sum", Borrow_BookController.sum);
            modelMap.addAttribute("books", bookRepository.findAll());
            modelMap.addAttribute("bbs", bBRepository.findAll());
            modelMap.addAttribute("bookDTO", new BookDTO());
            return "manageBook";
        }catch (Exception ex) {
            modelMap.addAttribute("error", ex.toString());
            return "manageBook";
        }
    }

    @GetMapping("/insertBook")
    public String insertBook(ModelMap modelMap) {
        modelMap.addAttribute("book", new Book());
        modelMap.addAttribute("categories", categoryRepository.findAll());
        modelMap.addAttribute("bookDTO", new BookDTO());
        return "insertBook";
    }

    public String getAuthorIDByName(String name) {
        String Id = "";
        Iterable<Author> authors = authorRepository.findAll();
        for (Author author:authors) {
            if(author.getAuthorName().equalsIgnoreCase(name)) {
                Id += author.getAuthorID();
            }
        }
        return Id;
    }

    public String getPlCompanyIDByName(String name) {
        String Id = "";
        Iterable<PlCompany> plCompanies = plCompanyRepository.findAll();
        for (PlCompany plCompany:plCompanies) {
            if(plCompany.getPlCompanyName().equalsIgnoreCase(name)) {
                Id += plCompany.getPlCompanyID();
            }
        }
        return Id;
    }

    public String getAuthorNameByID(String id) {
        String name = "";
        Iterable<Author> authors = authorRepository.findAll();
        for (Author author:authors) {
            if(author.getAuthorID().equalsIgnoreCase(id)) {
                name += author.getAuthorName();
            }
        }
        return name;
    }

    public String getPlCompanyNameByID(String id) {
        String name = "";
        Iterable<PlCompany> plCompanies = plCompanyRepository.findAll();
        for (PlCompany plCompany : plCompanies) {
            if(plCompany.getPlCompanyID().equalsIgnoreCase(id)) {
                name += plCompany.getPlCompanyName();
            }
        }
        return name;
    }

    @GetMapping("/{bookID}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String bookID) throws IOException {
        byte[] imageData = service.downloadImageFromFileSystem(bookID);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

    @PostMapping("/insertBook")
    public String insertBook(ModelMap modelMap,
                             @Valid @ModelAttribute("book") Book book,
                             BindingResult bindingResult,
                             @RequestParam("file") MultipartFile file) {
        if(bindingResult.hasErrors()) {
            modelMap.addAttribute("categories", categoryRepository.findAll());
            modelMap.addAttribute("bookDTO", new BookDTO());
            return "insertBook";
        }else {
            try {
                if (getAuthorIDByName(book.getAuthorID()) == "") {
                    Author author = new Author();
                    author.setAuthorName(book.getAuthorID());
                    authorRepository.save(author);
                    book.setAuthorID(author.getAuthorID());
                } else {
                    book.setAuthorID(getAuthorIDByName(book.getAuthorID()));
                }
                if (getPlCompanyIDByName(book.getPlCompanyID()) == "") {
                    PlCompany plCompany = new PlCompany();
                    plCompany.setPlCompanyName(book.getPlCompanyID());
                    plCompanyRepository.save(plCompany);
                    book.setPlCompanyID(plCompany.getPlCompanyID());
                    book.setImagePath(service.uploadFileToFileSystem(file));
                    bookRepository.save(book);
                } else {
                    book.setPlCompanyID(getPlCompanyIDByName(book.getPlCompanyID()));
                }
                book.setImagePath(service.uploadFileToFileSystem(file));
                bookRepository.save(book);
                modelMap.addAttribute("sum", Borrow_BookController.sum);
                modelMap.addAttribute("books", bookRepository.findAll());
                modelMap.addAttribute("bbs", bBRepository.findAll());
                modelMap.addAttribute("bookDTO", new BookDTO());
                return "manageBook";
            } catch (Exception ex) {
                modelMap.addAttribute("error", ex.toString());
                return "insertBook";
            }
        }
    }

    @GetMapping("/updateBookForm/{bookID}")
    public String updateBook(ModelMap modelMap, @PathVariable String bookID) {
        modelMap.addAttribute("categories", categoryRepository.findAll());
        Book book = bookRepository.findById(bookID).get();
        book.setAuthorID(getAuthorNameByID(book.getAuthorID()));
        book.setPlCompanyID(getPlCompanyNameByID(book.getPlCompanyID()));
        modelMap.addAttribute("book", book);
        modelMap.addAttribute("bookDTO", new BookDTO());
        return "updateBook";
    }

    @PostMapping("/updateBook/{bookID}")
    public String updateBook(ModelMap modelMap,
                             @PathVariable String bookID,
                             @Valid @ModelAttribute("book") Book book,
                             BindingResult bindingResult,
                             @RequestParam("file") MultipartFile file) {
        if(bindingResult.hasErrors()) {
            modelMap.addAttribute("categories", categoryRepository.findAll());
            Book fBook = bookRepository.findById(bookID).get();
            fBook.setAuthorID(getAuthorNameByID(fBook.getAuthorID()));
            fBook.setPlCompanyID(getPlCompanyNameByID(fBook.getPlCompanyID()));
            modelMap.addAttribute("book", fBook);
            modelMap.addAttribute("bookDTO", new BookDTO());
            return "updateBook";
        }else {
            try {
                if(bookRepository.findById(bookID).isPresent()) {
                    Book foundBook = bookRepository.findById(bookID).get();

                    foundBook.setBookName(book.getBookName());
                    foundBook.setBookPrice(book.getBookPrice());
                    foundBook.setPageNumber(book.getPageNumber());
                    foundBook.setReleasedDay(book.getReleasedDay());
                    book.setImagePath(service.uploadFileToFileSystem(file));
                    foundBook.setImagePath(book.getImagePath());
                    foundBook.setCategoryID(book.getCategoryID());
                    foundBook.setAuthorID(getAuthorIDByName(book.getAuthorID()));
                    foundBook.setPlCompanyID(getPlCompanyIDByName(book.getPlCompanyID()));
                    bookRepository.save(foundBook);
                    modelMap.addAttribute("sum", Borrow_BookController.sum);
                    modelMap.addAttribute("books", bookRepository.findAll());
                    modelMap.addAttribute("bookDTO", new BookDTO());
                    return "manageBook";
                }
            }catch (Exception ex) {
                modelMap.addAttribute("error", ex.toString());
                return "updateBookForm";
            }
        }
        modelMap.addAttribute("sum", Borrow_BookController.sum);
        modelMap.addAttribute("books", bookRepository.findAll());
        modelMap.addAttribute("bbs", bBRepository.findAll());
        modelMap.addAttribute("bookDTO", new BookDTO());
        return "manageBook";
    }
    
    @GetMapping("/getBooksByCategoryID/{categoryID}")
    public String getBooksByCategoryID(ModelMap modelMap, @PathVariable String categoryID) {
        ArrayList<Book> books = (ArrayList<Book>) bookRepository.findByCategoryID(categoryID);
        if(!books.isEmpty()) {
            modelMap.addAttribute("category", categoryRepository.findById(categoryID).get());
            modelMap.addAttribute("books", books);
            modelMap.addAttribute("hasData", true);
            modelMap.addAttribute("bookDTO", new BookDTO());
            return "bookOnCat";
        }
        modelMap.addAttribute("category", categoryRepository.findById(categoryID).get());
        modelMap.addAttribute("books", books);
        modelMap.addAttribute("hasData", false);
        modelMap.addAttribute("bookDTO", new BookDTO());
        return "bookOnCat";
    }

}
