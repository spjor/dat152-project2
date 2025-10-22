/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * @author tdoy
 */
@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	public Order saveOrder(Order order) {
		
		order = orderRepository.save(order);
		
		return order;
	}
	
	public Order findOrder(Long id) throws OrderNotFoundException {
		
		Order order = orderRepository.findById(id)
				.orElseThrow(()-> new OrderNotFoundException("Order with id: "+id+" not found in the order list!"));
		
		return order;
	}
	
	// TODO public void deleteOrder(Long id)
	public void deleteOrder(Long id) {
		orderRepository.deleteById(id);
	}
	
	// TODO public List<Order> findAllOrders()
	public List<Order> findAllOrders() {
		return orderRepository.findAll();
	}
	
	// TODO public List<Order> findByExpiryDate(LocalDate expiry, Pageable page)
	public List<Order> findByExpiryDate(LocalDate expiry, Pageable page) {
		Page<Order> ordersPage = orderRepository.findByExpiryBefore(expiry, page);
		return ordersPage.getContent();
	}
	
	// TODO public Order updateOrder(Order order, Long id)
	public Order updateOrder(Order order, Long id) throws OrderNotFoundException {
		Order existingOrder = findOrder(id);
		existingOrder.setExpiry(order.getExpiry());
		existingOrder.setIsbn(order.getIsbn());

		return orderRepository.save(existingOrder);
	}

}
