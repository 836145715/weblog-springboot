package com.zmx.weblog.admin.service.impl;

import com.zmx.weblog.admin.model.vo.article.PublishArticleReqVO;
import com.zmx.weblog.admin.service.ArticleService;
import com.zmx.weblog.common.domain.dos.*;
import com.zmx.weblog.common.domain.mapper.*;
import com.zmx.weblog.common.enums.ResponseCodeEnum;
import com.zmx.weblog.common.exception.BizException;
import com.zmx.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

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
        List<String> requestedTagNames = reqVO.getTags();
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
                        .map(tagId -> ArticleTagRelDO.builder().articleId(article.getId()).tagId(tagId).build())
                        .collect(Collectors.toList());
                articleTagRelMapper.insertBatch(relsToInsert); // 需要实现 insertBatch
            }
        }

        return Response.success(article.getId());
    }

}