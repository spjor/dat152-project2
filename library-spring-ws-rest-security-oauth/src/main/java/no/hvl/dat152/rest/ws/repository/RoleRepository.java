/**
 * 
 */
package no.hvl.dat152.rest.ws.repository;

import no.hvl.dat152.rest.ws.model.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * 
 */
public interface RoleRepository extends CrudRepository<Role, Integer> {
	
	Role findByName(String name);
	
}
