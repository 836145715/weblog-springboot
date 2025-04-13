package com.zmx.weblog.admin.service;

import com.zmx.weblog.admin.model.vo.tag.AddBatchTagReqVO;
import com.zmx.weblog.admin.model.vo.tag.AddTagReqVO;
import com.zmx.weblog.admin.model.vo.tag.DeleteTagReqVO;
import com.zmx.weblog.admin.model.vo.tag.FindTagPageListReqVO;
import com.zmx.weblog.admin.model.vo.tag.SearchTagReqVO;
import com.zmx.weblog.common.utils.PageResponse;
import com.zmx.weblog.common.utils.Response;

public interface AdminTagService {
    Response addTag(AddTagReqVO addTagReqVO);

    Response deleteTag(DeleteTagReqVO deleteTagReqVO);

    PageResponse findTagPageList(FindTagPageListReqVO findTagPageListReqVO);

    Response searchTag(SearchTagReqVO searchTagReqVO);

    Response selectList();

    Response addBatch(AddBatchTagReqVO addBatchTagReqVO);
}
