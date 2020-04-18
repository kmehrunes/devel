package io.devel.message.annotations;

public class Channels {
    private Channels() {}

    public Channel channel(final String channelName) {
        return new RequiredChannel(channelName);
    }
}
