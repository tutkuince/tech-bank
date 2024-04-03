package com.techbank.cqrs.core.message;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Message {
    private String id;
}
