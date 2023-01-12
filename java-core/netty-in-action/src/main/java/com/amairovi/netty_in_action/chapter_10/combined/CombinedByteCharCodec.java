package com.amairovi.netty_in_action.chapter_10.combined;

import com.amairovi.netty_in_action.Listing;
import io.netty.channel.CombinedChannelDuplexHandler;

@Listing("10.10")
public class CombinedByteCharCodec extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {
    public CombinedByteCharCodec() {
        super(new ByteToCharDecoder(), new CharToByteEncoder());
    }
}
