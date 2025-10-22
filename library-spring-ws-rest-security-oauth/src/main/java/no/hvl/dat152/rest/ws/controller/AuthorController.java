/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 
 */
@RestController
@RequestMapping("/elibrary/api/v1")
@PreAuthorize("hasAuthority('ADMIN')")
public class AuthorController {

	@Autowired
    private AuthorService authorService;
    
    @GetMapping("/authors")
    public ResponseEntity<Object> getAllBooks(){

        List<Author> authors = authorService.findAll();

        if(authors.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @GetMapping("/authors/{id}")
    public ResponseEntity<Object> getAuthor(@PathVariable int id) throws AuthorNotFoundException {

        Author author = authorService.findById(id);

        return new ResponseEntity<>(author, HttpStatus.OK);

    }

    @GetMapping("/authors/{id}/books")
    public ResponseEntity<Set<Book>> getAllBooksByAuthorId(@PathVariable int id) {

        try {
            Set<Book> books = authorService.findBooksByAuthorId(id);
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (AuthorNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/authors")
    public ResponseEntity<Author> createAuthor(@RequestBody Author author){

        Author nauthor = authorService.saveAuthor(author);

        return new ResponseEntity<>(nauthor, HttpStatus.CREATED);
    }

    @PutMapping("/authors/{id}")
    public ResponseEntity<Author> updateAuthor(@RequestBody Author author, @PathVariable int id) throws AuthorNotFoundException {

        Author updatedAuthor = authorService.updateAuthor(author, id);

        return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
    }


}
