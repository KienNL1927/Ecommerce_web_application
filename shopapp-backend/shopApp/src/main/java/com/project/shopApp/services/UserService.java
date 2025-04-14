package com.project.shopApp.services;

import com.project.shopApp.components.JwtTokenUtil;
import com.project.shopApp.dtos.UserDTO;
import com.project.shopApp.exceptions.DataNotFoundException;
import com.project.shopApp.exceptions.PermissionDenyException;
import com.project.shopApp.models.Role;
import com.project.shopApp.models.User;
import com.project.shopApp.reposistories.RoleRepository;
import com.project.shopApp.reposistories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Configuration
public class UserService implements IUserService{
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        //register
        String phoneNumber = userDTO.getPhoneNumber();
        //check if the phone number existed
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException(("Role not found")));
        if(role.getName().toUpperCase().equals("ADMIN")){
            throw new PermissionDenyException("You cannot register new account with ADMIN");
        }
        //convert from userDTO to user
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();

        newUser.setRole(role);

        //Check if account ID existed, then do not need password
        if(userDTO.getFacebookAccountId().equals("0") && userDTO.getGoogleAccountId().equals("0")){
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) throws Exception {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if(optionalUser.isEmpty()){
            throw new Exception("Invalid phone number or password");
        }
        User existingUser = optionalUser.get();
        //check password
        if(existingUser.getFacebookAccountId() == "0"
                && existingUser.getGoogleAccountId() == "0"){
            if(!passwordEncoder.matches(password, existingUser.getPassword())){
                throw new BadCredentialsException("Wrong phone number or Password");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        phoneNumber, password,
                existingUser.getAuthorities());
        //Authenticate with Java Spring security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser); //return jwt token
    }
}
