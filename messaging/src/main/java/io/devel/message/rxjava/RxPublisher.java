package io.devel.message.rxjava;

import io.devel.message.Message;
import io.devel.message.MessagePublisher;
import io.devel.message.MessageSubscriber;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class RxPublisher implements MessagePublisher {
    private PublishSubject<Message> subject = PublishSubject.create();

    @Override
    public void publish(final Message message) {
        subject.onNext(message);
    }

    @Override
    public void acceptSubscriber(final MessageSubscriber subscriber) {
        subject.subscribe(subscriber::onMessage);
    }
}
