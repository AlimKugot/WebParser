package com.alim.admin.security.auth;

import com.alim.admin.model.UserEntity;
import com.alim.admin.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findFirstByName(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(String.format("Username %s cannot find", username));
        }
        return new UserDetailsImpl(userEntity);
    }
}
