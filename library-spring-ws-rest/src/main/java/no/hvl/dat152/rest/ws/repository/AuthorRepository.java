/**
 * 
 */
package no.hvl.dat152.rest.ws.repository;


import no.hvl.dat152.rest.ws.model.Author;
import org.springframework.data.repository.CrudRepository;

/**
 * @author tdoy
 */
public interface AuthorRepository extends CrudRepository<Author, Integer> {
	

}
