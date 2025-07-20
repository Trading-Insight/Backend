package com.tradin.core.users.implement;

import static com.tradin.core.common.exception.ExceptionType.NOT_FOUND_USER_EXCEPTION;

import com.tradin.core.common.exception.TradinException;

import com.tradin.core.users.domain.Users;
import com.tradin.core.users.domain.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsersReader implements UserDetailsService {

    private final UsersRepository usersRepository;

    public Users findByEmail(String email) {
        return usersRepository.findByEmail(email)
            .orElseThrow(() -> new TradinException(NOT_FOUND_USER_EXCEPTION));
    }

    public boolean isUserExist(String email) {
        return usersRepository.findByEmail(email).isPresent();
    }

    public Users findBySub(String sub) {
        return usersRepository.findBySub(sub)
            .orElseThrow(() -> new TradinException(NOT_FOUND_USER_EXCEPTION));
    }

    public Users findById(Long id) {
        return usersRepository.findById(id)
            .orElseThrow(() -> new TradinException(NOT_FOUND_USER_EXCEPTION));
    }

    @Override
    public Users loadUserByUsername(String userId) {
        return findById(Long.valueOf(userId));
    }
}
