/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/users")
	public ResponseEntity<Object> getUsers(){
		
		List<User> users = userService.findAllUsers();
		
		if(users.isEmpty())
			
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else
			return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@GetMapping(value = "/users/{id}")
	public ResponseEntity<Object> getUser(@PathVariable Long id) throws UserNotFoundException, OrderNotFoundException{
		
		User user = userService.findUser(id);
		
		return new ResponseEntity<>(user, HttpStatus.OK);	
		
	}

	@PostMapping("/users")
	public ResponseEntity<Object> createUser(@RequestBody User user){

		User nuser = userService.saveUser(user);

		return new ResponseEntity<>(nuser, HttpStatus.CREATED);
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<Object> updateUser(@RequestBody User user, @PathVariable Long id) throws UserNotFoundException {

		User updatedUser = userService.updateUser(user, id);

		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<Object> deleteUser(@PathVariable Long id) throws UserNotFoundException {

		userService.deleteUser(id);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping(value = "/users/{id}/orders")
	public ResponseEntity<Object> getUserOrders(@PathVariable Long id) throws UserNotFoundException, OrderNotFoundException {

		Set<Order> orders = userService.getUserOrders(id);

		if(orders.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else
			return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	@GetMapping(value = "/users/{uid}/orders/{oid}")
	public ResponseEntity<Object> getUserOrder(@PathVariable Long uid, @PathVariable Long oid) throws UserNotFoundException, OrderNotFoundException {

		Order order = userService.getUserOrder(uid, oid);

		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	@DeleteMapping("/users/{uid}/orders/{oid}")
	public ResponseEntity<Object> deleteUserOrder(@PathVariable Long uid, @PathVariable Long oid) throws UserNotFoundException, OrderNotFoundException {

		userService.deleteOrderForUser(uid, oid);

		return new ResponseEntity<>(HttpStatus.OK);
	}
	

	@PostMapping("/users/{uid}/orders")
	public ResponseEntity<Object> createUserOrder(@PathVariable Long uid,  @RequestBody Order order) throws UserNotFoundException, OrderNotFoundException {

		User user = userService.createOrdersForUser(uid, order);

		Set<Order> orders = user.getOrders();

		Link selfLink = linkTo(methodOn(UserController.class).getUserOrders(uid)).withSelfRel();

		for(Order o : orders) {
			Link getLink = linkTo(methodOn(UserController.class).getUserOrder(uid, o.getId())).withRel("get-order-"+o.getId());
			Link updateLink = linkTo(methodOn(OrderController.class).updateOrder(o, o.getId())).withRel("update-order-"+o.getId());
			Link deleteLink = linkTo(methodOn(UserController.class).deleteUserOrder(uid, o.getId())).withRel("delete-order-"+o.getId());
			o.add(getLink);
			o.add(updateLink);
			o.add(deleteLink);
		}

		return new ResponseEntity<>(orders, HttpStatus.CREATED);
	}
	
}
