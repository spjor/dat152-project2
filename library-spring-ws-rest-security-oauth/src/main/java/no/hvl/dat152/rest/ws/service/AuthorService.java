/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author tdoy
 */
@Service
public class AuthorService {

	@Autowired
	private AuthorRepository authorRepository;
		
	
	public Author findById(int id) throws AuthorNotFoundException {
		
		Author author = authorRepository.findById(id)
				.orElseThrow(()-> new AuthorNotFoundException("Author with the id: "+id+ "not found!"));
		
		return author;
	}

	public Author saveAuthor(Author author) {
		return authorRepository.save(author);
	}

	public Author updateAuthor(Author author, int id) throws AuthorNotFoundException  {
		Author existingAuthor = findById(id);
		existingAuthor.setFirstname(author.getFirstname());
		existingAuthor.setLastname(author.getLastname());
		existingAuthor.setBooks(author.getBooks());
		return authorRepository.save(existingAuthor);
	}

	public List<Author> findAll() {
		Iterable<Author> authors = authorRepository.findAll();
		List<Author> asList = new ArrayList<Author>();
		authors.forEach(asList::add);
		return asList;
	}

	public void deleteById(int id) throws AuthorNotFoundException {
		Author author = findById(id);
		authorRepository.delete(author);
	}

	public Set<Book> findBooksByAuthorId(int id) throws AuthorNotFoundException {
		Author author = findById(id);
		return author.getBooks();
	}
}
