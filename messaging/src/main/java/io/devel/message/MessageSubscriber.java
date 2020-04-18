package io.devel.message;

import com.authguard.emb.model.Message;

@FunctionalInterface
public interface MessageSubscriber {
    void onMessage(Message message);
}
