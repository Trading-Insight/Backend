package com.tradin.module.users.domain.repository;

import com.tradin.module.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>, UsersQueryRepository {
    Optional<Users> findByEmail(String email);

    Optional<Users> findBySub(String sub);
}
