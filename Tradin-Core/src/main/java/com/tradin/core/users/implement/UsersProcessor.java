package com.tradin.core.users.implement;


import com.tradin.core.users.domain.UserSocialType;
import com.tradin.core.users.domain.Users;
import com.tradin.core.users.domain.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsersProcessor {

    private final UsersRepository usersRepository;

    public Users createUser(String name, String sub, String email, String socialId, UserSocialType socialType) {
        Users user = Users.of(
            name,
            sub,
            email,
            socialId,
            socialType
        );
        return usersRepository.save(user);
    }

}
