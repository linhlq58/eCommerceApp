package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void add_to_cart() {
        when(userRepository.findByUsername("test")).thenReturn(getTestUser());
        when(itemRepository.findById(10L)).thenReturn(Optional.ofNullable(getTestItem()));

        ResponseEntity<Cart> addToCartResponse = cartController.addTocart(getTestRequest());
        assertNotNull(addToCartResponse);
        assertEquals(200, addToCartResponse.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart() {
        when(userRepository.findByUsername("test")).thenReturn(getTestUser());
        when(itemRepository.findById(2L)).thenReturn(Optional.ofNullable(getTestExistedItem()));

        ResponseEntity<Cart> removeFromCartResponse = cartController.removeFromcart(getAnotherTestRequest());
        assertNotNull(removeFromCartResponse);
        assertEquals(200, removeFromCartResponse.getStatusCodeValue());
    }

    public ModifyCartRequest getTestRequest() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test");
        request.setItemId(10L);
        request.setQuantity(1);
        return request;
    }

    public ModifyCartRequest getAnotherTestRequest() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test");
        request.setItemId(2L);
        request.setQuantity(1);
        return request;
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

    public Item getTestItem() {
        Item item = new Item();
        item.setId(10L);
        item.setName("Plane");
        item.setPrice(BigDecimal.valueOf(17.88));

        return item;
    }

    public Item getTestExistedItem() {
        Item item = new Item();
        item.setId(2L);
        item.setName("Motorbike");
        item.setPrice(BigDecimal.valueOf(2.87));

        return item;
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
}
