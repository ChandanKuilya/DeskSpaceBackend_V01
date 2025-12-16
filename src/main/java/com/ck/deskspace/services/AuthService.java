package com.ck.deskspace.services;

import com.ck.deskspace.dto.AuthRequestDTO;
import com.ck.deskspace.dto.AuthResponseDTO;
import com.ck.deskspace.models.User;
import com.ck.deskspace.repository.IUserRepository;
import com.ck.deskspace.services.utilityServices.jwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final jwtService jwtService;

    public AuthService(IUserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                       jwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponseDTO register(AuthRequestDTO request) {
        // 1. Check if user exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // 2. Create User Entity & HASH the password
        User user = new User();
        user.setFirstName(request.getLastName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // <--- Crucial Step
        user.setRole(request.getRole());

        // 3. Save
        userRepository.save(user);

        // 4. Return dummy token (we will implement real JWT generation in Part 2)
        return new AuthResponseDTO("User registered successfully. Login to get token.");
    }

    // Adding the Login Method
    public AuthResponseDTO login(AuthRequestDTO request) {
        // 1. This triggers the AuthenticationManager -> Calls CustomUserDetailsService -> Checks Password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. If step 1 didn't throw an exception, the user is valid. Generate Token.
        String token = jwtService.generateToken(request.getEmail());

        return new AuthResponseDTO(token);
    }
}
