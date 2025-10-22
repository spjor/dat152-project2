package no.hvl.dat152.rest.ws.jpa.test;

import no.hvl.dat152.rest.ws.main.LibraryApplication;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = LibraryApplication.class)
class BookRepositoryTest {

	@Autowired
	private BookRepository bookRepo;

	@Test
	final void testFindAllAndSort() {
		
		Iterable<Book> books = bookRepo.findAll(Sort.by("title"));
		System.out.println(books);

        assertEquals("ghijk1234", books.iterator().next().getIsbn());
	}
	
	@Test
	final void testFindAllAndPage() {
		
		Pageable paging = PageRequest.ofSize(2);
		Page<Book> books = bookRepo.findAll(paging);
		System.out.println(books);

        assertEquals(2, books.getNumberOfElements());
	}

	@Test
	final void testFindByTitleLike() {
		
		List<Book> books = bookRepo.findByTitleContaining("Software");
		System.out.println(books);

        assertEquals("abcde1234", books.get(0).getIsbn());
	}
	
	@Test
	final void testFindByIsbn() {
		
		Optional<Book> book = bookRepo.findByIsbn("ghijk1234");
		System.out.println(book.get());

        assertEquals("ghijk1234", book.get().getIsbn());
	}
	
	@Test
	final void testFindBookByIsbn() {
		
		Book book = bookRepo.findBookByISBN("ghijk1234");
		System.out.println(book);

        assertEquals("ghijk1234", book.getIsbn());
	}

}
