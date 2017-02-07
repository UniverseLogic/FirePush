package com.universe.net.protoc;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * Created by zg on 2017/2/7.
 */
public class UDPPacketRspMsg implements Serializable {
    private String msg;
    private InetSocketAddress inetSocketAddress;

    public UDPPacketRspMsg() {
    }

    public UDPPacketRspMsg(String msg, InetSocketAddress inetSocketAddress) {
        this.msg = msg;
        this.inetSocketAddress = inetSocketAddress;
    }

    public String getMsg() {
        return msg;
    }

    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("msg", msg)
                .add("inetSocketAddress", inetSocketAddress)
                .toString();
    }
}
