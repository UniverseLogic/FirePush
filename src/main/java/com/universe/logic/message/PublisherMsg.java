package com.universe.logic.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by frank_zhao on 2017/2/26.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublisherMsg implements Serializable {

    private String message;
}
