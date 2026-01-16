package com.ozkayret.banking.repository;

import com.ozkayret.banking.entity.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByNumber(@NotNull(message = "number is required") @NotBlank(message = "number is required") @NotEmpty(message = "number is required") String number);

    List<Account> findByName(String name);

    Page<Account> findByName(String name, Pageable pageable);
}
