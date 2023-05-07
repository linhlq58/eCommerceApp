package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void create_user_happy_path() throws Exception {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();

        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void create_user_password_not_length_enough() throws Exception {
        when(bCryptPasswordEncoder.encode("test1")).thenReturn("thisIsHashed");

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("test1");
        r.setConfirmPassword("test1");

        ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void find_user_by_name() throws Exception {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");
        given(userController.findByUserName("test").getBody()).willReturn(getTestUser());

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        ResponseEntity<User> createResponse = userController.createUser(r);

        assertNotNull(createResponse);
        assertEquals(200, createResponse.getStatusCodeValue());

        User u = createResponse.getBody();

        ResponseEntity<User> findByNameResponse = userController.findByUserName("test");

        assertNotNull(findByNameResponse);
        assertEquals(200, findByNameResponse.getStatusCodeValue());

        User u1 = findByNameResponse.getBody();
        assertEquals(u.getUsername(), u1.getUsername());
    }

    @Test
    public void find_user_by_id() throws Exception {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(getTestUser()));

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        ResponseEntity<User> createResponse = userController.createUser(r);

        assertNotNull(createResponse);
        assertEquals(200, createResponse.getStatusCodeValue());

        User u = createResponse.getBody();

        ResponseEntity<User> findByIdResponse = userController.findById(1L);

        assertNotNull(findByIdResponse);
        assertEquals(200, findByIdResponse.getStatusCodeValue());

        User u1 = findByIdResponse.getBody();
        assertEquals(u.getUsername(), u1.getUsername());
    }

    public User getTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("thisIsHashed");

        return user;
    }
}
