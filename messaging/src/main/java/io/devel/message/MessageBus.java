package io.devel.message;

import com.google.common.collect.ImmutableMap;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MessageBus {
    private final Map<String, MessagePublisher> channels;
    private final MessagePublisherFactory factory;
    private final boolean createIfMissing;

    public MessageBus(final MessagePublisherFactory factory, final Set<String> channelsNames) {
        final ImmutableMap.Builder<String, MessagePublisher> channelsMapBuilder = ImmutableMap.builder();;

        if (channelsNames != null) {
            channelsNames
                    .forEach(channel -> channelsMapBuilder.put(channel, factory.create()));

            this.channels = channelsNames.stream()
                    .collect(Collectors.toMap(Function.identity(), ignored -> factory.create()));

           this.createIfMissing = false;
           this.factory = null; // won't be needed
        } else {
            this.channels = new HashMap<>();
            this.createIfMissing = true;
            this.factory = factory;
        }
    }

    public void publish(final String channel, final Message message) {
        final MessagePublisher publisher = Optional.ofNullable(getPublisher(channel))
                .orElseThrow(() -> new IllegalArgumentException("Cannot publish to non-existing channel " + channel));

        publisher.publish(message);
    }

    public void subscribe(final String channel, final MessageSubscriber subscriber) {
        final MessagePublisher publisher = getNullable(channel);

        if (publisher == null) {
            throw new IllegalArgumentException("Cannot subscribe to non-existing channel " + channel);
        }

        publisher.acceptSubscriber(subscriber);
    }

    private MessagePublisher getPublisher(final String channel) {
        return createIfMissing ? getOrCreateIfMissing(channel) : getNullable(channel);
    }

    private MessagePublisher getNullable(final String channel) {
        return channels.get(channel);
    }

    private MessagePublisher getOrCreateIfMissing(final String channel) {
        final MessagePublisher publisher = channels.get(channel);

        if (publisher == null) {
            final MessagePublisher createdPublisher = factory.create();

            channels.put(channel, factory.create());

            return createdPublisher;
        } else {
            return publisher;
        }
    }
}
