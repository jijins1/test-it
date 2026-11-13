package com.example.testit;

import com.example.testit.adapter.mail.MailService;
import com.example.testit.model.Book;
import com.example.testit.model.Member;
import com.example.testit.model.Status;
import com.example.testit.repository.BookRepository;
import com.example.testit.repository.MemberRepository;
import com.example.testit.service.BookService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

class BookServiceTest {

    BookService bookService;
    BookRepository bookRepository;
    MemberRepository memberRepository;
    MailService mailService;

    @BeforeEach
    void setUp() {
        bookRepository = Mockito.mock(BookRepository.class);
        memberRepository = Mockito.mock(MemberRepository.class);
        mailService = Mockito.mock(MailService.class);
        bookService = new BookService(bookRepository, memberRepository, mailService);
    }

    @Test
    public void emprunteUnLivreDisponibleEtPrevientMembre() {

        var book = new Book("F1", "Charles Leclerc Le Goat");
        var alice = new Member("alice");

        //GIven
        book.setId(1L);
        book.setStatus(Status.DISPONIBLE);
        alice.setId(1L);

        //When
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(memberRepository.findById(1L)).thenReturn(Optional.of(alice));
        Mockito.when(bookRepository.findByMemberAndStatus(1L, Status.EMPRUNTE)).thenReturn(List.of());
        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

        //Then
        var result = bookService.borrowBook(1L, 1L);
        Assertions.assertThat(result.getStatus()).isEqualTo(Status.EMPRUNTE);
        Assertions.assertThat(result.getBorrower()).isSameAs(alice);
        Mockito.verify(bookRepository).findByMemberAndStatus(1L, Status.EMPRUNTE);
        Mockito.verify(bookRepository).save(book);
        Mockito.verify(mailService).sendMail(Mockito.eq(alice), Mockito.anyString(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(mailService);
    }

}
