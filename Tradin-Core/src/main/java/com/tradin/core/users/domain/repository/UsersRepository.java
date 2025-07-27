package com.tradin.core.users.domain.repository;

import com.tradin.core.users.domain.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>, UsersQueryRepository {
    Optional<Users> findByEmail(String email);

    Optional<Users> findBySub(String sub);
}
