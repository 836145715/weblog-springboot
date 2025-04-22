package com.zmx.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmx.weblog.common.domain.dos.ArticleCategoryRelDO;
import com.zmx.weblog.common.domain.dos.ArticleDO;
import com.zmx.weblog.common.domain.dos.ArticleTagRelDO;
import com.zmx.weblog.common.domain.dos.CategoryDO;
import com.zmx.weblog.common.domain.dos.TagDO;
import com.zmx.weblog.common.domain.mapper.*;
import com.zmx.weblog.common.utils.PageResponse;
import com.zmx.weblog.common.utils.Response;
import com.zmx.weblog.web.convert.ArticleConvert;
import com.zmx.weblog.web.model.vo.article.FindIndexArticlePageListRspVO;
import com.zmx.weblog.web.model.vo.category.FindCategoryListRspVO;
import com.zmx.weblog.web.model.vo.category.FindIndexArticlePageListReqVO;
import com.zmx.weblog.web.model.vo.tag.FindTagListRspVO;
import com.zmx.weblog.web.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleCategoryRelMapper articleCategoryRelMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagRelMapper articleTagRelMapper;

    @Override
    public Response findArticlePageList(FindIndexArticlePageListReqVO findIndexArticlePageListReqVO) {
        long current = findIndexArticlePageListReqVO.getCurrent();
        long size = findIndexArticlePageListReqVO.getSize();
        // 分页查询文章主体记录
        Page<ArticleDO> articlePage = articleMapper.selectPageList(null,null,null,current,size);
        // 返回得分页数据
        List<ArticleDO> articleDOList = articlePage.getRecords();

        List<FindIndexArticlePageListRspVO> vos = null;
        if (CollectionUtils.isNotEmpty(articleDOList)) {

            // 文章DO转VO
            vos = articleDOList.stream().map(ArticleConvert.INSTANCE::convertDO2VO)
                    .collect(Collectors.toList());

            // 拿到所有得文章ID集合
            List<Long> articleIds = articleDOList.stream().map(ArticleDO::getId).collect(Collectors.toList());

            // 设置文章所属分类
            // 查询所有分类
            List<CategoryDO> categoryDOs = categoryMapper.selectList();

            // 转为MAP 方便后续根据分类ID 批量查询所有关联记录
            Map<Long, String> categoryIdNameMap = categoryDOs.stream()
                    .collect(Collectors.toMap(CategoryDO::getId, CategoryDO::getName));

            // 根据文章ID 批量查询所有关联记录
            List<ArticleCategoryRelDO> articleCategoryRelDOS = articleCategoryRelMapper.selectByArticleIds(articleIds);

            vos.forEach(vo -> {
                Long currArticleId = vo.getId();
                // 过滤当前文章数据
                Optional<ArticleCategoryRelDO> optional = articleCategoryRelDOS.stream()
                        .filter(rel -> Objects.equals(rel.getArticleId(), currArticleId)).findAny();

                // 如果找到了
                if (optional.isPresent()) {
                    ArticleCategoryRelDO articleCategoryRelDO = optional.get();
                    Long categoryId = articleCategoryRelDO.getCategoryId();
                    String categoryName = categoryIdNameMap.get(categoryId);
                    FindCategoryListRspVO findCategoryListRspVO = FindCategoryListRspVO.builder()
                            .id(categoryId)
                            .name(categoryName)
                            .build();
                    // 设置到当前vo中
                    vo.setCategory(findCategoryListRspVO);
                }
            });

            // 设置文章标签
            //查询所有标签
            List<TagDO> tagDOS = tagMapper.selectList();
            // 转为MAP 方便后续根据分类ID 批量查询所有关联记录
            Map<Long, String> tagIdNameMap = tagDOS.stream().collect(Collectors.toMap(TagDO::getId, TagDO::getName));

            // 过滤出当前文章得标签关联记录
            List<ArticleTagRelDO> articleTagRelDOs = articleTagRelMapper.selectByArticleIds(articleIds);

            vos.forEach(vo -> {
                Long currArticleId = vo.getId();
                // 过滤当前文章关联得标签记录
                List<ArticleTagRelDO> articleTagRelDOList = articleTagRelDOs.stream()
                        .filter(rel -> Objects.equals(rel.getArticleId(), currArticleId))
                        .collect(Collectors.toList());

                List<FindTagListRspVO> findTagListRspVOs = new ArrayList<>();

                // 将关联记录 DO 转 VO, 并设置对应的标签名称
                articleTagRelDOList.forEach(articleTagRelDO -> {
                    Long tagId = articleTagRelDO.getTagId();
                    String tagName = tagIdNameMap.get(tagId);
                    FindTagListRspVO findTagListRspVO = FindTagListRspVO.builder()
                            .id(tagId)
                            .name(tagName)
                            .build();
                    findTagListRspVOs.add(findTagListRspVO);
                });

                vo.setTags(findTagListRspVOs);

            });
        }

        return PageResponse.success(articlePage,vos);
    }
}
