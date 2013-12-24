package com.noobsqn.util;

/**
 * Created by Molinski on 23/12/13.
 */
public enum JOType {
    JSONNULL (0),
    JSONPRIMITIVE (1),
    JSONOBJECT (2),
    JSONARRAY (3),
    JSONELEMENT (4);

    private final int typeId;

    private JOType(int typeId) {
        this.typeId = typeId;
    }

}
