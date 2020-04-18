package io.devel.message;

import java.time.Instant;

public class Message<T> {
    private final Instant timeStamp;
    private final String messageType;
    private final Class<T> bodyType;
    private final T body;

    public Message(final Instant timeStamp, final String messageType, final Class<T> bodyType, final T body) {
        this.timeStamp = timeStamp;
        this.messageType = messageType;
        this.bodyType = bodyType;
        this.body = body;
    }

    public Instant timeStamp() {
        return timeStamp;
    }

    public String type() {
        return messageType;
    }

    public Class<T> bodyType() {
        return bodyType;
    }

    public T body() {
        return body;
    }

    public static class Builder<T> {
        private Instant timeStamp;
        private String messageType;
        private Class<T> bodyType;
        private T body;

        public Builder timeStamp(final Instant timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public Builder messageType(final String messageType) {
            this.messageType = messageType;
            return this;
        }

        public Builder<T> bodyType(final Class<T> bodyType) {
            this.bodyType = bodyType;
            return this;
        }

        public Builder<T> body(final T body) {
            this.body = body;
            return this;
        }

        public Message<T> build() {
            return new Message<>(timeStamp, messageType, bodyType, body);
        }
    }
}
