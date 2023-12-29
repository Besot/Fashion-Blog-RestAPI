package com.alutastitches.serviceImpli;
import com.alutastitches.dto.UserDto;
import com.alutastitches.enums.Role;
import com.alutastitches.model.Users;
import com.alutastitches.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ObjectInputFilter;


@Service
public class UserServiceImpl implements UserDetailsService {


    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

@Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Username Not Found"));

    }

    public Users saveUser(UserDto userDto) {
    Users user = new  ObjectMapper().convertValue(userDto, Users.class);
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    user.setUserRole(Role.ROLE_USER);
    return userRepository.save(user);
    }
}
