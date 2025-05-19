package com.zmx.weblog.admin.service.impl;

import com.zmx.weblog.admin.model.vo.article.PublishArticleReqVO;
import com.zmx.weblog.admin.model.vo.article.UpdateArticleReqVO;
import com.zmx.weblog.admin.model.vo.article.FindArticlePageListReqVO;
import com.zmx.weblog.admin.model.vo.article.FindArticlePageListRspVO;
import com.zmx.weblog.admin.convert.ArticleDetailConvert;
import com.zmx.weblog.admin.model.vo.article.ArticleDetailRspVO;
import com.zmx.weblog.admin.service.AdminArticleService;
import com.zmx.weblog.common.domain.dos.*;
import com.zmx.weblog.common.domain.mapper.*;
import com.zmx.weblog.common.enums.ResponseCodeEnum;
import com.zmx.weblog.common.exception.BizException;
import com.zmx.weblog.common.utils.Response;
import com.zmx.weblog.common.utils.PageResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminArticleServiceImpl implements AdminArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleContentMapper articleContentMapper;
    @Autowired
    private ArticleCategoryRelMapper articleCategoryRelMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleTagRelMapper articleTagRelMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response publishArticle(PublishArticleReqVO reqVO) {

        // 检查文章分类是否存在
        CategoryDO categoryDO = categoryMapper.selectById(reqVO.getCategoryId());
        if (categoryDO == null) {
            log.error("文章分类不存在，categoryId: {}", reqVO.getCategoryId());
            throw new BizException(ResponseCodeEnum.CATEGORY_NOT_FOUND);
        }

        // 1. 保存文章主表
        ArticleDO article = ArticleDO.builder()
                .title(reqVO.getTitle())
                .cover(reqVO.getCover())
                .summary(reqVO.getSummary())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .isDeleted(0)
                .readNum(1)
                .build();
        articleMapper.insert(article);

        // 2. 保存文章内容
        ArticleContentDO content = ArticleContentDO.builder()
                .articleId(article.getId())
                .content(reqVO.getContent())
                .build();
        articleContentMapper.insert(content);

        // 3. 保存文章分类关联
        ArticleCategoryRelDO categoryRel = ArticleCategoryRelDO.builder()
                .articleId(article.getId())
                .categoryId(reqVO.getCategoryId())
                .build();
        articleCategoryRelMapper.insert(categoryRel);

        // 4. 保存文章关联的标签集合
        insertTags(article.getId(), reqVO.getTags());
        return Response.success(article.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response deleteArticle(Long articleId) {
        // 1. 删除主表
        ArticleDO article = articleMapper.selectById(articleId);
        if (article == null) {
            return Response.fail(ResponseCodeEnum.ARTICLE_NOT_FOUND);
        }
        articleMapper.deleteById(articleId);

        // 2. 删除内容表
        articleContentMapper.deleteByArticleId(articleId);

        // 3. 删除分类关联
        articleCategoryRelMapper.deleteByArticleId(articleId);

        // 4. 删除标签关联
        articleTagRelMapper.deleteByArticleId(articleId);

        return Response.success();
    }

    @Override
    public PageResponse findArticlePageList(FindArticlePageListReqVO reqVO) {
        String title = reqVO.getTitle();
        LocalDate startDate = reqVO.getStartDate();
        LocalDate endDate = reqVO.getEndDate();
        Page<ArticleDO> pageResult = articleMapper.selectPageList(title, startDate, endDate, reqVO.getCurrent(),
                reqVO.getSize());
        List<FindArticlePageListRspVO> vos = null;
        if (CollectionUtils.isNotEmpty(pageResult.getRecords())) {
            vos = pageResult.getRecords().stream()
                    .map(article -> FindArticlePageListRspVO.builder()
                            .id(article.getId())
                            .title(article.getTitle())
                            .cover(article.getCover())
                            .summary(article.getSummary())
                            .createTime(article.getCreateTime())
                            .build())
                    .collect(Collectors.toList());
        }
        return PageResponse.success(pageResult, vos);
    }

    @Override
    public Response getArticleDetail(Long articleId) {
        // 查询主表
        ArticleDO article = articleMapper.selectById(articleId);
        if (article == null) {
            log.error("文章不存在，articleId: {}", articleId);
            throw new BizException(ResponseCodeEnum.ARTICLE_NOT_FOUND);
        }

        // 查询内容
        ArticleContentDO content = articleContentMapper.selectByArticleId(articleId);

        // 查询分类
        ArticleCategoryRelDO categoryRel = articleCategoryRelMapper.selectByArticleId(articleId);
        Long categoryId = categoryRel != null ? categoryRel.getCategoryId() : null;

        // 对应标签
        List<ArticleTagRelDO> articleTagRelDOS = articleTagRelMapper.selectByArticleId(articleId);

        // 查询标签
        List<Long> tagIds = articleTagRelDOS.stream().map(ArticleTagRelDO::getTagId).collect(Collectors.toList());

        // do转vo
        ArticleDetailRspVO rspVO = ArticleDetailConvert.INSTANCE.convertDO2VO(article);
        rspVO.setContent(content.getContent());
        rspVO.setCategoryId(categoryId);
        rspVO.setTagIds(tagIds);

        return Response.success(rspVO);
    }

    /**
     * 插入标签
     * 
     * @param articleId 文章ID
     * @param tags      标签列表
     */
    private void insertTags(Long articleId, List<String> tags) {
        // 4. 保存文章关联的标签集合
        List<String> requestedTagNames = tags;
        // 用于存储最终关联的所有 tagId
        List<Long> finalTagIds = new ArrayList<>();

        if (requestedTagNames != null && !requestedTagNames.isEmpty()) {
            // 1. 批量查询已存在的标签 如果没有那么插入
            List<TagDO> existingTags = tagMapper.selectByNames(requestedTagNames);

            Map<String, Long> existingTagMap = existingTags.stream()
                    .collect(Collectors.toMap(TagDO::getName, TagDO::getId));
            finalTagIds.addAll(existingTagMap.values());

            // 2. 找出新标签并批量插入
            List<String> newTagsToInsert = new ArrayList<>();
            for (String tagName : requestedTagNames) {
                if (!existingTagMap.containsKey(tagName)) {
                    newTagsToInsert.add(tagName);
                }
            }

            if (!newTagsToInsert.isEmpty()) {
                tagMapper.insertBatch(newTagsToInsert);
                // 批量插入后，需要获取新标签的 ID
                List<TagDO> newlyInsertedTags = tagMapper.selectByNames(newTagsToInsert);
                finalTagIds.addAll(newlyInsertedTags.stream().map(TagDO::getId).collect(Collectors.toList()));
            }

            // 3. 批量插入文章与标签关联记录
            if (!finalTagIds.isEmpty()) {
                List<ArticleTagRelDO> relsToInsert = finalTagIds.stream()
                        .map(tagId -> ArticleTagRelDO.builder().articleId(articleId).tagId(tagId).build())
                        .collect(Collectors.toList());
                articleTagRelMapper.insertBatch(relsToInsert);
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateArticle(UpdateArticleReqVO reqVO) {
        Long articleId = reqVO.getId();

        // 1. VO 转 ArticleDO, 并更新
        ArticleDO articleDO = ArticleDO.builder()
                .id(articleId)
                .title(reqVO.getTitle())
                .cover(reqVO.getCover())
                .summary(reqVO.getSummary())
                .updateTime(LocalDateTime.now())
                .build();
        int count = articleMapper.updateById(articleDO);

        // 根据更新是否成功，来判断该文章是否存在
        if (count == 0) {
            log.warn("==> 该文章不存在, articleId: {}", articleId);
            throw new BizException(ResponseCodeEnum.ARTICLE_NOT_FOUND);
        }

        // 2. VO 转 ArticleContentDO，并更新
        ArticleContentDO articleContentDO = ArticleContentDO.builder()
                .articleId(articleId)
                .content(reqVO.getContent())
                .build();
        articleContentMapper.updateByArticleId(articleContentDO);

        // 3. 更新文章分类
        Long categoryId = reqVO.getCategoryId();

        // 3.1 校验提交的分类是否真实存在
        CategoryDO categoryDO = categoryMapper.selectById(categoryId);
        if (Objects.isNull(categoryDO)) {
            log.warn("==> 分类不存在, categoryId: {}", categoryId);
            throw new BizException(ResponseCodeEnum.CATEGORY_NOT_FOUND);
        }

        // 先删除该文章关联的分类记录，再插入新的关联关系
        articleCategoryRelMapper.deleteByArticleId(articleId);
        ArticleCategoryRelDO articleCategoryRelDO = ArticleCategoryRelDO.builder()
                .articleId(articleId)
                .categoryId(categoryId)
                .build();
        articleCategoryRelMapper.insert(articleCategoryRelDO);

        // 4. 保存文章关联的标签集合
        // 先删除该文章对应的标签
        articleTagRelMapper.deleteByArticleId(articleId);
        insertTags(articleId, reqVO.getTags());

        return Response.success();

    }

}