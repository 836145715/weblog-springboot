package com.zmx.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmx.weblog.common.domain.dos.ArticleCategoryRelDO;
import com.zmx.weblog.common.domain.dos.ArticleDO;
import com.zmx.weblog.common.domain.dos.CategoryDO;
import com.zmx.weblog.common.domain.mapper.ArticleCategoryRelMapper;
import com.zmx.weblog.common.domain.mapper.ArticleMapper;
import com.zmx.weblog.common.domain.mapper.CategoryMapper;
import com.zmx.weblog.common.enums.ResponseCodeEnum;
import com.zmx.weblog.common.exception.BizException;
import com.zmx.weblog.common.utils.PageResponse;
import com.zmx.weblog.common.utils.Response;
import com.zmx.weblog.web.convert.ArticleConvert;
import com.zmx.weblog.web.model.vo.category.FindCategoryArticlePageListReqVO;
import com.zmx.weblog.web.model.vo.category.FindCategoryArticlePageListRspVO;
import com.zmx.weblog.web.model.vo.category.FindCategoryListRspVO;
import com.zmx.weblog.web.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleCategoryRelMapper articleCategoryRelMapper;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public Response findCategoryList() {
        // 查询所有分类
        List<CategoryDO> categoryDOS = categoryMapper.selectList(Wrappers.emptyWrapper());

        // DO 转 VO
        List<FindCategoryListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(categoryDOS)) {
            vos = categoryDOS.stream()
                    .map(categoryDO -> FindCategoryListRspVO.builder()
                            .id(categoryDO.getId())
                            .name(categoryDO.getName())
                            .build())
                    .collect(Collectors.toList());
        }

        return Response.success(vos);
    }

    @Override
    public Response findCategoryArticlePageList(FindCategoryArticlePageListReqVO findCategoryArticlePageListReqVO) {
        Long current = findCategoryArticlePageListReqVO.getCurrent();
        Long size = findCategoryArticlePageListReqVO.getSize();
        Long categoryId = findCategoryArticlePageListReqVO.getId();

        CategoryDO categoryDO = categoryMapper.selectById(categoryId);
        if (Objects.isNull(categoryDO)) {
            log.warn("==> 该分类不存在, categoryId: {}", categoryId);
            throw new BizException(ResponseCodeEnum.CATEGORY_NOT_FOUND);
        }

        // 先查询分类下的文章ID
        List<ArticleCategoryRelDO> articleCategoryRelDOS = articleCategoryRelMapper
                .selectListByCategoryId(categoryId);

        // 如果该分类没有任何文章
        if (CollectionUtils.isEmpty(articleCategoryRelDOS)) {
            log.warn("==> 该分类没有任何文章, categoryId: {}", categoryId);
            return PageResponse.success(null);
        }

        List<Long> articleIds = articleCategoryRelDOS.stream()
                .map(ArticleCategoryRelDO::getArticleId)
                .collect(Collectors.toList());

        // 根据分类id集合查询文章分页数据
        Page<ArticleDO> articlePage = articleMapper.selectPageListByArticleIds(current, size, articleIds);
        List<ArticleDO> articleDOS = articlePage.getRecords();

        // 将文章DO转VO
        List<FindCategoryArticlePageListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(articleDOS)) {
            vos = articleDOS.stream()
                    .map(ArticleConvert.INSTANCE::convertDO2CategoryArticleVO)
                    .collect(Collectors.toList());
        }

        return PageResponse.success(articlePage,vos);
    }

}
