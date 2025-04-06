package com.tradin.module.account.domain.repository;

import com.tradin.module.account.domain.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUserId(Long userId);

    Optional<Account> findByIdAndUserId(Long id, Long userId);
}
