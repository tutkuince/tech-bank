package com.techbank.account.common.event;

import com.techbank.account.common.dto.AccountType;
import com.techbank.cqrs.core.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountOpenedEvent extends BaseEvent {
    private String accountHolder;
    private AccountType accountType;
    private LocalDateTime createdDate;
    private double openingBalance;
}
