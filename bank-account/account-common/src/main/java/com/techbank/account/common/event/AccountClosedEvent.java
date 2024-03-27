package com.techbank.account.common.event;

import com.techbank.cqrs.core.event.BaseEvent;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@Builder
public class AccountClosedEvent extends BaseEvent {

}
