package com.techbank.account.cmd.infrastructure;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.account.cmd.domain.EventStoreRepository;
import com.techbank.cqrs.core.event.BaseEvent;
import com.techbank.cqrs.core.event.EventModel;
import com.techbank.cqrs.core.exception.AggregateNotFoundException;
import com.techbank.cqrs.core.exception.ConcurrencyException;
import com.techbank.cqrs.core.infrastructure.EventStore;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountEventStore implements EventStore {

    private EventStoreRepository eventStoreRepository;

    public AccountEventStore(EventStoreRepository eventStoreRepository) {
        this.eventStoreRepository = eventStoreRepository;
    }

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        List<EventModel> eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if (expectedVersion != -1 && eventStream.get(eventStream.size() - 1).getVersion() != expectedVersion) {
            throw new ConcurrencyException();
        }
        int version = expectedVersion;
        for (BaseEvent event :events) {
            version++;
            event.setVersion(version);
            EventModel eventModel = EventModel.builder()
                    .timeStamp(LocalDateTime.now())
                    .aggregateIdentifier(aggregateId)
                    .aggregateType(AccountAggregate.class.getTypeName())
                    .version(version)
                    .eventType(event.getClass().getTypeName())
                    .eventData(event)
                    .build();

            EventModel persistentEvent = eventStoreRepository.save(eventModel);
            if (persistentEvent != null) {
                // TODO: produce event to Kafka
            }
        }
    }

    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        List<EventModel> eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if (eventStream == null || eventStream.isEmpty()) {
            throw new AggregateNotFoundException("Incorrect account ID provided!");
        }
        return eventStream.stream().map(EventModel::getEventData).collect(Collectors.toList());
    }
}
