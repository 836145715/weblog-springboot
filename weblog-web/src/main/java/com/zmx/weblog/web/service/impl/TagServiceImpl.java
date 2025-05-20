package com.zmx.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmx.weblog.common.domain.dos.ArticleDO;
import com.zmx.weblog.common.domain.dos.ArticleTagRelDO;
import com.zmx.weblog.common.domain.dos.TagDO;
import com.zmx.weblog.common.domain.mapper.ArticleMapper;
import com.zmx.weblog.common.domain.mapper.ArticleTagRelMapper;
import com.zmx.weblog.common.domain.mapper.TagMapper;
import com.zmx.weblog.common.enums.ResponseCodeEnum;
import com.zmx.weblog.common.exception.BizException;
import com.zmx.weblog.common.utils.PageResponse;
import com.zmx.weblog.common.utils.Response;
import com.zmx.weblog.web.convert.ArticleConvert;
import com.zmx.weblog.web.model.vo.tag.FindTagArticlePageListReqVO;
import com.zmx.weblog.web.model.vo.tag.FindTagArticlePageListRspVO;
import com.zmx.weblog.web.model.vo.tag.FindTagListRspVO;
import com.zmx.weblog.web.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagRelMapper articleTagRelMapper;
    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 获取标签列表
     *
     * @return
     */
    @Override
    public Response findTagList() {
        // 查询所有标签
        List<TagDO> tagDOS = tagMapper.selectList(Wrappers.emptyWrapper());

        // DO 转 VO
        List<FindTagListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(tagDOS)) {
            vos = tagDOS.stream()
                    .map(tagDO -> FindTagListRspVO.builder()
                            .id(tagDO.getId())
                            .name(tagDO.getName())
                            .build())
                    .collect(Collectors.toList());
        }

        return Response.success(vos);
    }

    @Override
    public Response findTagPageList(FindTagArticlePageListReqVO findTagArticlePageListReqVO) {
        Long current = findTagArticlePageListReqVO.getCurrent();
        Long size = findTagArticlePageListReqVO.getSize();
        // 标签 ID
        Long tagId = findTagArticlePageListReqVO.getId();

        //判断标签是否存在
        TagDO tagDO = tagMapper.selectById(tagId);
        if(Objects.isNull(tagDO)){
            log.warn("==> 标签不存在，标签ID：{}",tagId);
            throw new BizException(ResponseCodeEnum.TAG_NOT_FOUND);
        }

        // 查询标签下 关联的 文章ID
        List<ArticleTagRelDO> articleTagRelDOS = articleTagRelMapper.selectByTagId(tagId);

        //如果标签下没有任何文章
        if (CollectionUtils.isEmpty(articleTagRelDOS)){
            return Response.success();
        }

        //提取文章ID
        List<Long> articleIds = articleTagRelDOS.stream()
               .map(ArticleTagRelDO::getArticleId)
               .collect(Collectors.toList());

        // 根据文章ID 分页查询文章
        Page<ArticleDO> articleDOPage = articleMapper.selectPageListByArticleIds(current, size, articleIds);
        List<ArticleDO> articleDOS  = articleDOPage.getRecords();

        //DO转VO
        List<FindTagArticlePageListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(articleDOS)) {
            vos = articleDOS.stream()
                    .map(ArticleConvert.INSTANCE::convertDO2TagArticleVO)
                    .collect(Collectors.toList());
        }

        return PageResponse.success(articleDOPage,vos);
    }
}
