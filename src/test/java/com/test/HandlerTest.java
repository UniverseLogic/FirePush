package com.test;

import com.universe.net.codec.UDPDecoder;
import com.universe.net.codec.UDPEncoder;
import com.universe.net.message.UDPPacketReqMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import junit.framework.TestCase;
import org.junit.Assert;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

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


    public void testUDPClient() throws UnsupportedEncodingException {
        String str = "hello,world";
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(str.length());
        buf.writeBytes(str.getBytes("UTF-8"));

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel channel) throws Exception {
                            ChannelPipeline channelPipeline = channel.pipeline();
                            channelPipeline.addLast("decoder", new UDPDecoder());
                            channelPipeline.addLast("encoder", new UDPEncoder());
                            channelPipeline.addLast("handler", new SimpleChannelInboundHandler<UDPPacketReqMsg>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, UDPPacketReqMsg msg) throws Exception {
                                    System.out.println("i`m client");
                                    System.out.println(msg);
                                }
                            });
                        }
                    });

            Channel ch = b.bind(0).sync().channel();
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    ch.closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    group.shutdownGracefully();
                }
            });


            ch.writeAndFlush(new DatagramPacket(
                    buf, new InetSocketAddress(InetAddress.getLocalHost(), 46000))).sync();


            if (!ch.closeFuture().await(51000)) {
                System.err.println("request timed out.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
