package com.tradin.module.users.account.domain.repository;

import com.tradin.module.users.account.domain.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountQueryRepository {

    Optional<Account> findByIdAndUserId(Long id, Long userId);
}
