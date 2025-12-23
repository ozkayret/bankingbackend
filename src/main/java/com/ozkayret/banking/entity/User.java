package com.ozkayret.banking.entity;

import com.ozkayret.banking.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "username"), @UniqueConstraint(columnNames = "email")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    private String username;
    private String password;
    private String email;
    private String name;
    private String surname;
    @OneToMany(mappedBy = "user")
    private List<Account> accounts;
}
