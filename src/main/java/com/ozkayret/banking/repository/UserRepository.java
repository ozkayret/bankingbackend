package com.ozkayret.banking.repository;

import com.ozkayret.banking.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Boolean existsByUsername(@NotNull(message = "username is required") @NotBlank(message = "username is required") @NotEmpty(message = "username is required") String username);

    Boolean existsByEmail(@NotNull(message = "email is required") @NotBlank(message = "email is required") @NotEmpty(message = "email is required") String email);

    Optional<User> findByUsername(String username);
}
