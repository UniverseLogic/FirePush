package com.universe.logic.message;

import lombok.Data;

/**
 * Created by frank_zhao on 2017/2/26.
 */
@Data
public class CallbackMsg extends RouteMessage {

    private String message;

    public CallbackMsg(Long userId, String message) {
        super(userId);
        this.message = message;
    }
}
