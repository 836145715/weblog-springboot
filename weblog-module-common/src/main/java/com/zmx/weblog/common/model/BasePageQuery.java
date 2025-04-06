package com.zmx.weblog.common.model;
import lombok.Data;

@Data
public class BasePageQuery {
    /**
     * 当前页码
     */
    private long current = 1;

    /**
     * 每页显示的记录数
     */
    private long size = 10;


}
