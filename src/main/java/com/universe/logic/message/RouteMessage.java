package com.universe.logic.message;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by frank_zhao on 2017/2/26.
 */
@Data
@AllArgsConstructor
public abstract class RouteMessage {

    private Long userId;
}
