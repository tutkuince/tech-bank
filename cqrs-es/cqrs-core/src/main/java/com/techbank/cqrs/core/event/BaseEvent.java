package com.techbank.cqrs.core.event;

import com.techbank.cqrs.core.message.Message;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseEvent extends Message {
    private int version;
}
