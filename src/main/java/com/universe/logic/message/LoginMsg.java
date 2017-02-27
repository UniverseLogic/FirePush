package com.universe.logic.message;

import lombok.Data;

/**
 * Created by frank_zhao on 2017/2/26.
 */
@Data
public class LoginMsg extends RouteMessage {
    private String userName;

    public LoginMsg(Long userId, String userName) {
        super(userId);
        this.userName = userName;
    }
}
