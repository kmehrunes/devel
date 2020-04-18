package io.devel.message.rxjava;

import io.devel.message.MessagePublisher;
import io.devel.message.MessagePublisherFactory;

public class RxPublisherFactory implements MessagePublisherFactory {
    @Override
    public MessagePublisher create() {
        return new RxPublisher();
    }
}
