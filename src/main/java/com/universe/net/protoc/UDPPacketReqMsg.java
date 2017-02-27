package com.universe.net.protoc;

import com.google.common.base.MoreObjects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * Created by zg on 2017/2/7.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UDPPacketReqMsg implements Serializable {
    private String msg;
    private InetSocketAddress inetSocketAddress;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("msg", msg)
                .add("inetSocketAddress", inetSocketAddress)
                .toString();
    }
}
