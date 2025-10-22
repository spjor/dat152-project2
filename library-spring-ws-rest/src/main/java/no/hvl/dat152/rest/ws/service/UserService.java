/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.UserRepository;

/**
 * @author tdoy
 */
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	
	public List<User> findAllUsers(){
		
		List<User> allUsers = (List<User>) userRepository.findAll();
		
		return allUsers;
	}
	
	public User findUser(Long userid) throws UserNotFoundException {
		
		User user = userRepository.findById(userid)
				.orElseThrow(()-> new UserNotFoundException("User with id: "+userid+" not found"));
		
		return user;
	}
	
	
	// TODO public User saveUser(User user)
	public User saveUser(User user) {

		return userRepository.save(user);
	}
	
	// TODO public void deleteUser(Long id) throws UserNotFoundException 
	public void deleteUser(Long userid) throws UserNotFoundException {
		User user = findUser(userid);

		userRepository.delete(user);
	}

	// TODO public User updateUser(User user, Long id)
	public User updateUser(User user, long id) throws UserNotFoundException {
		User existingUser = findUser(id);
		existingUser.setFirstname(user.getFirstname());
		existingUser.setLastname(user.getLastname());
		existingUser.setOrders(user.getOrders());

		return userRepository.save(existingUser);
	}
	
	// TODO public Set<Order> getUserOrders(Long userid) 
	public Set<Order> getUserOrders(Long userid) throws UserNotFoundException {
		return findUser(userid).getOrders();
	}

	// TODO public Order getUserOrder(Long userid, Long oid)
	public Order getUserOrder(Long userid, Long oid) throws UserNotFoundException, OrderNotFoundException {
		User user = findUser(userid);
		Set<Order> orders = user.getOrders();
		return orders.stream()
				.filter(o -> o.getId().equals(oid))
				.findFirst()
				.orElseThrow(()->new OrderNotFoundException("Order with id: "+oid+" not found"));
	}
	
	// TODO public void deleteOrderForUser(Long userid, Long oid)
	public void deleteOrderForUser(Long userid, Long oid) throws UserNotFoundException, OrderNotFoundException {
		User user = findUser(userid);
		Set<Order> orders = user.getOrders();
		Iterator<Order> iterator = orders.iterator();
		boolean found = false;
		while (iterator.hasNext()) {
			Order order = iterator.next();
			if (order.getId().equals(oid)) {
				iterator.remove();
				found = true;
				break;
			}
		}
		if (!found) {
			throw new OrderNotFoundException("Order with id: "+oid+" not found");
		}
		userRepository.save(user);
	}

	// TODO public User createOrdersForUser(Long userid, Order order)
	public User createOrdersForUser(Long userid, Order order) throws UserNotFoundException {
		User user = findUser(userid);
		user.addOrder(order);
		return userRepository.save(user);
	}
}
