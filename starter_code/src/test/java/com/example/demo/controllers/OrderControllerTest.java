package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submit() {
        when(userRepository.findByUsername("test")).thenReturn(getTestUser());

        ResponseEntity<UserOrder> submitResponse = orderController.submit("test");
        assertNotNull(submitResponse);
        assertEquals(200, submitResponse.getStatusCodeValue());
    }

    @Test
    public void get_order_for_user() {
        when(userRepository.findByUsername("test")).thenReturn(getTestUser());
        given(orderController.getOrdersForUser("test").getBody()).willReturn(getListUserOrderTest());

        ResponseEntity<List<UserOrder>> getListOrderForUserResponse = orderController.getOrdersForUser("test");
        assertNotNull(getListOrderForUserResponse);
        assertEquals(200, getListOrderForUserResponse.getStatusCodeValue());
    }

    public User getTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("thisIsHashed");

        Cart cart = new Cart();
        cart.setItems(getListTestItem());
        user.setCart(cart);

        return user;
    }

    public List<Item> getListTestItem() {
        List<Item> listItem = new ArrayList<>();

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Car");
        item1.setPrice(BigDecimal.valueOf(3.56));

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Motorbike");
        item2.setPrice(BigDecimal.valueOf(2.87));

        Item item3 = new Item();
        item3.setId(3L);
        item3.setName("Bicycle");
        item3.setPrice(BigDecimal.valueOf(1.32));

        listItem.add(item1);
        listItem.add(item2);
        listItem.add(item3);

        return listItem;
    }

    public List<UserOrder> getListUserOrderTest() {
        List<UserOrder> listUserOrder = new ArrayList<>();

        UserOrder order1 = new UserOrder();
        order1.setUser(getTestUser());
        order1.setTotal(BigDecimal.valueOf(12.93));

        UserOrder order2 = new UserOrder();
        order2.setUser(getTestUser());
        order2.setTotal(BigDecimal.valueOf(15.76));

        return listUserOrder;
    }
}
