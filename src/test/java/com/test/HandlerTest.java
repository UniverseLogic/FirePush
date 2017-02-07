package com.test;

import com.universe.net.codec.UDPDecoder;
import com.universe.net.codec.UDPEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import junit.framework.TestCase;
import org.junit.Assert;

import java.io.UnsupportedEncodingException;

/**
 * Created by zg on 2017/2/7.
 */
public class HandlerTest extends TestCase {

    public void testDecode() throws UnsupportedEncodingException {
        String str = "hello,world";
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(str.length());
        buf.writeBytes(str.getBytes("UTF-8"));
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new UDPDecoder());
        Assert.assertTrue(embeddedChannel.writeInbound(buf));
        Assert.assertTrue(embeddedChannel.finish());
    }

    public void testEnCode() throws UnsupportedEncodingException {
        String str = "hello,world";
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(str.length());
        buf.writeBytes(str.getBytes("UTF-8"));

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new UDPEncoder());
        Assert.assertTrue(embeddedChannel.writeOutbound(buf));
        Assert.assertTrue(embeddedChannel.finish());
    }


}
