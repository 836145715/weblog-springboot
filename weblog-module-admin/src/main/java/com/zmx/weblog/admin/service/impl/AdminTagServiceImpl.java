package com.zmx.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmx.weblog.admin.model.vo.tag.*;
import com.zmx.weblog.admin.service.AdminTagService;
import com.zmx.weblog.common.domain.dos.TagDO;
import com.zmx.weblog.common.domain.mapper.TagMapper;
import com.zmx.weblog.common.enums.ResponseCodeEnum;
import com.zmx.weblog.common.exception.BizException;
import com.zmx.weblog.common.model.vo.SelectRspVO;
import com.zmx.weblog.common.utils.PageResponse;
import com.zmx.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminTagServiceImpl implements AdminTagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public Response addTag(AddTagReqVO addTagReqVO) {
        String tagName = addTagReqVO.getName();

        // 先判断该标签是否已经存在
        TagDO tagDO = tagMapper.selectByName(tagName);

        if (Objects.nonNull(tagDO)) {
            log.warn("标签名称： {}, 此标签已存在", tagName);
            throw new BizException(ResponseCodeEnum.TAG_NAME_IS_EXISTED);
        }

        // 构建 DO 类
        TagDO insertTagDO = TagDO.builder()
                .name(addTagReqVO.getName().trim())
                .build();

        // 执行 insert
        tagMapper.insert(insertTagDO);

        return Response.success();
    }

    @Override
    public Response deleteTag(DeleteTagReqVO deleteTagReqVO) {
        tagMapper.deleteById(deleteTagReqVO.getId());
        return Response.success();
    }

    @Override
    public PageResponse findTagPageList(FindTagPageListReqVO findTagPageListReqVO) {
        // 从请求参数中获取查询条件
        String name = findTagPageListReqVO.getName(); // 标签名称
        LocalDate startDate = findTagPageListReqVO.getStartDate(); // 开始时间
        LocalDate endDate = findTagPageListReqVO.getEndDate(); // 结束时间

        Page<TagDO> pageResult = tagMapper.findTagPageList(name, startDate, endDate, findTagPageListReqVO.getCurrent(),
                findTagPageListReqVO.getSize());

        // 将 DO 转换为 VO 对象
        List<FindTagPageListRspVO> vos = null;
        // 如果查询结果不为空,则转换数据
        if (CollectionUtils.isNotEmpty(pageResult.getRecords())) {
            vos = pageResult.getRecords().stream()
                    .map(tag -> FindTagPageListRspVO.builder()
                            .id(tag.getId()) // 设置标签ID
                            .name(tag.getName()) // 设置标签名称
                            .createTime(tag.getCreateTime()) // 设置创建时间
                            .build())
                    .collect(Collectors.toList());
        }

        // 返回分页数据
        return PageResponse.success(pageResult, vos);
    }

    @Override
    public Response searchTag(SearchTagReqVO searchTagReqVO) {
        String keyword = searchTagReqVO.getKeyword();
        List<TagDO> tagDOS = tagMapper.searchByKey(keyword);

        List<SearchTagRspVO> vos = tagDOS.stream()
                .map(tag -> SearchTagRspVO.builder()
                        .name(tag.getName())
                        .createTime(tag.getCreateTime())
                        .build())
                .collect(Collectors.toList());

        return Response.success(vos);
    }

    @Override
    public Response selectList() {
        List<TagDO> tagDOList = tagMapper.selectList(new QueryWrapper<>());
        List<SelectRspVO> vos = null;
        if (CollectionUtils.isNotEmpty(tagDOList)) {
            vos = tagDOList.stream()
                    .map(tag -> SelectRspVO.builder()
                            .label(tag.getName())
                            .value(tag.getId())
                            .build())
                    .collect(Collectors.toList());
        }
        return Response.success(vos);
    }

}
