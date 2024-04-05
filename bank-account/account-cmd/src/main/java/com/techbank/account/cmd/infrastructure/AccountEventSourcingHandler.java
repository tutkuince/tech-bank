package com.techbank.account.cmd.infrastructure;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.cqrs.core.domain.AggregateRoot;
import com.techbank.cqrs.core.event.BaseEvent;
import com.techbank.cqrs.core.handler.EventSourcingHandler;
import com.techbank.cqrs.core.infrastructure.EventStore;
import com.techbank.cqrs.core.producer.EventProducer;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {
    private final EventStore eventStore;
    private final EventProducer eventProducer;

    public AccountEventSourcingHandler(EventStore eventStore, EventProducer eventProducer) {
        this.eventStore = eventStore;
        this.eventProducer = eventProducer;
    }

    @Override
    public void save(AggregateRoot aggregateRoot) {
        eventStore.saveEvents(aggregateRoot.getId(), aggregateRoot.getUncommittedChanges(), aggregateRoot.getVersion());
        aggregateRoot.markChangesAsCommitted();
    }

    @Override
    public AccountAggregate getById(String id) {
        AccountAggregate accountAggregate = new AccountAggregate();
        List<BaseEvent> events = eventStore.getEvents(id);
        if (events != null && !events.isEmpty()) {
            accountAggregate.replayEvents(events);
            Optional<Integer> latestVersion = events.stream().map(BaseEvent::getVersion).max(Comparator.naturalOrder());
            latestVersion.ifPresent(accountAggregate::setVersion);
        }
        return accountAggregate;
    }

    @Override
    public void republishEvents() {
        List<String> aggregateIds = eventStore.getAggregateIds();
        for (String aggregateId : aggregateIds) {
            AccountAggregate aggregate = getById(aggregateId);
            if (aggregate == null ||!aggregate.getActive()) continue;
            List<BaseEvent> events = eventStore.getEvents(aggregateId);
            for (BaseEvent event: events) {
                eventProducer.produce(event.getClass().getSimpleName(), event);
            }
        }
    }
}
