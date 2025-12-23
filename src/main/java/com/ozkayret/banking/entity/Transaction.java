package com.ozkayret.banking.entity;


import com.ozkayret.banking.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Account from;// The account ID from which money is being transferred.
    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private Account to; // The account ID to which money is being transferred.
    private BigDecimal amount;// The amount of money being transferred.
    private LocalDateTime transactionDate;// The date and time when the transaction was initiated.
    @Enumerated(EnumType.ORDINAL)
    private Status status;// The status of the transaction (e.g., "SUCCESS", "FAILED").
}
