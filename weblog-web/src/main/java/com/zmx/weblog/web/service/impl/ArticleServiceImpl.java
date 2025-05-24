package com.zmx.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmx.weblog.admin.event.ReadArticleEvent;
import com.zmx.weblog.common.domain.dos.*;
import com.zmx.weblog.common.domain.mapper.*;
import com.zmx.weblog.common.enums.ResponseCodeEnum;
import com.zmx.weblog.common.exception.BizException;
import com.zmx.weblog.common.utils.PageResponse;
import com.zmx.weblog.common.utils.Response;
import com.zmx.weblog.web.convert.ArticleConvert;
import com.zmx.weblog.web.markdown.MarkdownHelper;
import com.zmx.weblog.web.model.vo.article.FindArticleDetailReqVO;
import com.zmx.weblog.web.model.vo.article.FindArticleDetailRspVO;
import com.zmx.weblog.web.model.vo.article.FindIndexArticlePageListRspVO;
import com.zmx.weblog.web.model.vo.article.FindPreNextArticleRspVO;
import com.zmx.weblog.web.model.vo.category.FindCategoryListRspVO;
import com.zmx.weblog.web.model.vo.category.FindIndexArticlePageListReqVO;
import com.zmx.weblog.web.model.vo.tag.FindTagListRspVO;
import com.zmx.weblog.web.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 文章服务实现类
 */
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
    @Autowired
    private ArticleContentMapper articleContentMapper;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public Response findArticlePageList(FindIndexArticlePageListReqVO findIndexArticlePageListReqVO) {
        long current = findIndexArticlePageListReqVO.getCurrent();
        long size = findIndexArticlePageListReqVO.getSize();
        // 分页查询文章主体记录
        Page<ArticleDO> articlePage = articleMapper.selectPageList(null, null, null, current, size);
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
            // 查询所有标签
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

        return PageResponse.success(articlePage, vos);
    }

    @Override
    public Response findArticleDetail(FindArticleDetailReqVO reqVO) {
        Long articleId = reqVO.getArticleId();
        ArticleDO articleDO = articleMapper.selectById(articleId);

        // 判断文章是否存在
        if (Objects.isNull(articleDO)) {
            log.warn("====> 该文章不存在，articleId:{}", articleId);
            throw new BizException(ResponseCodeEnum.ARTICLE_NOT_FOUND);
        }

        // 查询正文
        ArticleContentDO articleContentDO = articleContentMapper.selectByArticleId(articleId);

        // DO转VO
        FindArticleDetailRspVO rspVo = FindArticleDetailRspVO.builder()
                .title(articleDO.getTitle())
                .createTime(articleDO.getCreateTime())
                .readNum(articleDO.getReadNum())
                .content(MarkdownHelper.convertMarkdown2Html(articleContentDO.getContent())).build();

        // 查询所属分类
        ArticleCategoryRelDO articleCategoryRelDO = articleCategoryRelMapper.selectByArticleId(articleId);
        CategoryDO categoryDO = categoryMapper.selectById(articleCategoryRelDO.getCategoryId());

        rspVo.setCategoryId(categoryDO.getId());
        rspVo.setCategoryName(categoryDO.getName());

        // 查询标签
        List<ArticleTagRelDO> articleTagRelDOS = articleTagRelMapper.selectByArticleId(articleId);
        List<Long> tagIds = articleTagRelDOS.stream().map(ArticleTagRelDO::getTagId).collect(Collectors.toList());
        List<TagDO> tagDOs = tagMapper.selectByIds(tagIds);

        // 标签DO转VO
        List<FindTagListRspVO> tagVOs = tagDOs.stream()
                .map(tagDO -> FindTagListRspVO.builder().id(tagDO.getId()).name(tagDO.getName()).build())
                .collect(Collectors.toList());

        rspVo.setTags(tagVOs);

        // 查询上一篇文章
        ArticleDO preArticleDO = articleMapper.selectPreArticle(articleId);
        if (Objects.nonNull(preArticleDO)) {
            FindPreNextArticleRspVO preArticleRspVO = FindPreNextArticleRspVO.builder()
                    .articleId(preArticleDO.getId())
                    .articleTitle(preArticleDO.getTitle())
                    .build();
            rspVo.setPreArticle(preArticleRspVO);
        }

        // 查询下一篇文章
        ArticleDO nextArticleDO = articleMapper.selectNextArticle(articleId);
        if (Objects.nonNull(nextArticleDO)) {
            FindPreNextArticleRspVO nextArticleRspVO = FindPreNextArticleRspVO.builder()
                    .articleId(nextArticleDO.getId())
                    .articleTitle(nextArticleDO.getTitle())
                    .build();
            rspVo.setNextArticle(nextArticleRspVO);
        }

        // 文章发布订阅事件
        eventPublisher.publishEvent(new ReadArticleEvent(this, articleId));

        return Response.success(rspVo);

    }

}
