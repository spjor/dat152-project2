/**
 * 
 */
package no.hvl.dat152.rest.ws.repository;


import no.hvl.dat152.rest.ws.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


/**
 * 
 */
public interface UserRepository extends CrudRepository<User, Long> {
	
	Optional<User> findByEmail(String email);
	
	Boolean existsByEmail(String email);

}
