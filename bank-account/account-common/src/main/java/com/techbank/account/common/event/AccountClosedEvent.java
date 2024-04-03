package com.techbank.account.common.event;

import com.techbank.cqrs.core.event.BaseEvent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class AccountClosedEvent extends BaseEvent {

}
