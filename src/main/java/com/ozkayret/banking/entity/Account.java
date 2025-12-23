package com.ozkayret.banking.entity;


import com.ozkayret.banking.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends BaseEntity {
    private String number; // A unique account number.
    private String name; // A unique account name.
    private BigDecimal balance; // The current balance of the account.

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "from")
    private List<Transaction> sentTransactions;

    @OneToMany(mappedBy = "to")
    private List<Transaction> receivedTransactions;
}
