package com.zestindiait.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zestindiait.dto.LoginRequest;
import com.zestindiait.entity.User;
import com.zestindiait.repository.UserRepository;
import com.zestindiait.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("john_doe", "password123", "John", "Doe", "john.doe@example.com", "USER");
    }

    @Test
    void testRegisterUser_Success() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        User registeredUser = userService.registerUser(user);
        assertNotNull(registeredUser);
        assertEquals("john_doe", registeredUser.getUsername());
    }

    @Test
    void testRegisterUser_AlreadyExists() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        User registeredUser = userService.registerUser(user);
        assertNull(registeredUser);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<User> users = userService.getAllUsers();
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
    }

    @Test
    void testLoginUser_Success() {
        LoginRequest loginRequest = new LoginRequest("john_doe", "password123");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);

        User loggedInUser = userService.loginUser(loginRequest);
        assertNotNull(loggedInUser);
        assertEquals("john_doe", loggedInUser.getUsername());
    }

    @Test
    void testLoginUser_Failure() {
        LoginRequest loginRequest = new LoginRequest("john_doe", "wrongPassword");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        User loggedInUser = userService.loginUser(loginRequest);
        assertNull(loggedInUser);
    }

    @Test
    void testLoadUserByUsername_Success() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("john_doe");
        assertNotNull(userDetails);
        assertEquals("john_doe", userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        when(userRepository.findByUsername("unknown_user")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("unknown_user"));
    }
}