package com.tradin.core.users.service;

import static com.tradin.core.common.exception.ExceptionType.NOT_FOUND_USER_EXCEPTION;

import com.tradin.core.common.exception.TradinException;
import com.tradin.core.users.domain.UserSocialType;
import com.tradin.core.users.domain.Users;
import com.tradin.core.users.domain.repository.UsersRepository;
import com.tradin.core.users.service.dto.FindUserInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService implements UserDetailsService {

    private final UsersRepository usersRepository;

    public Users saveOrGetUser(String name, String sub, String email, String socialId, UserSocialType socialType) {
        if (isUserExist(email)) {
            return findByEmail(email);
        }
        return createUser(name, sub, email, socialId, socialType);
    }

    public FindUserInfoResponseDto findUserInfo(Long userId) {
        Users user = findById(userId);
        return new FindUserInfoResponseDto(user.getName(), user.getEmail());
    }

    private Users createUser(String name, String sub, String email, String socialId, UserSocialType socialType) {
        Users user = Users.of(name, sub, email, socialId, socialType);
        return usersRepository.save(user);
    }

    private Users findByEmail(String email) {
        return usersRepository.findByEmail(email)
            .orElseThrow(() -> new TradinException(NOT_FOUND_USER_EXCEPTION));
    }

    public boolean isUserExist(String email) {
        return usersRepository.findByEmail(email).isPresent();
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
