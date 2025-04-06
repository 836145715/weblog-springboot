package com.zmx.weblog.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class PageResponse<T> extends Response<List<T>> {

    /**
     * 总记录数
     */
    private long total;

    /**
     * 每页显示的记录数，默认每页显示10条
     */
    private long size = 10;

    /**
     * 当前页码，默认第1页
     */
    private long current = 1;

    /**
     * 总页数
     */
    private long pages;


    public static <T> PageResponse<T> success(IPage page,List<T> data){
        PageResponse<T> response = new PageResponse<>();
        response.setSuccess(true);
        response.setSize(Objects.isNull(page.getSize()) ? 10 : page.getSize());
        response.setCurrent(Objects.isNull(page.getCurrent()) ? 1 : page.getCurrent());
        response.setPages(Objects.isNull(page.getPages()) ? 0 : page.getPages());
        response.setTotal(Objects.isNull(page.getTotal()) ? 0 : page.getTotal());
        response.setData(data);
        return response;
    }


}
