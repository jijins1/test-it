package com.example.testit.controller;

import com.example.testit.adapter.member.CurrentMemberService;
import com.example.testit.model.Book;
import com.example.testit.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final CurrentMemberService currentMemberService;

    public BookController(BookService bookService, CurrentMemberService currentMemberService) {
        this.bookService = bookService;
        this.currentMemberService = currentMemberService;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/member/{memberId}")
    public List<Book> getBooksByMember(@PathVariable Long memberId) {
        return bookService.findByMemberId(memberId);
    }

    @PostMapping
    public Book createBook(@Valid @RequestBody BookRequest request) {
        return bookService.createBook(request.title, request.title);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        book.setId(id);
        try {
            return ResponseEntity.ok(bookService.updateBook(book));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/borrow")
    public ResponseEntity<Book> borrowBook(@PathVariable Long id) {
        Long memberId = currentMemberService.getCurrentMemberId()
                .orElseThrow(() -> new IllegalStateException("Member not authenticated"));
        try {
            return ResponseEntity.ok(bookService.returnBook(id, memberId));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<Book> returnBook(@PathVariable Long id) {
        Long memberId = currentMemberService.getCurrentMemberId()
                .orElseThrow(() -> new IllegalStateException("Member not authenticated"));
        try {
            return ResponseEntity.ok(bookService.returnBook(id, memberId));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    public static class BookRequest {
        @NotBlank
        public String title;
        @NotBlank
        public String author;
    }
}
