package com.tejiao.service.impl;

import com.base.vo.PageList;
import com.tejiao.dao.AttachmentDao;
import com.tejiao.model.Attachment;
import com.tejiao.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dzf on 2016/2/24.
 */
@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentDao attachmentDao;

    @Override
    public void save(Attachment attachment) {
        attachmentDao.save(attachment);
    }

    @Override
    public PageList getWebPageList(Integer currentPage, Integer pageSize, Integer siteId, Integer declareId, String keyword) {
        return attachmentDao.getWebPageList(currentPage, pageSize, siteId, declareId, keyword);
    }

    @Override
    public PageList getPageList(Integer currentPage, Integer pageSize, Integer siteId, Integer schoolId, String keyword) {
        return attachmentDao.getPageList(currentPage, pageSize, siteId, schoolId, keyword);
    }

    @Override
    public int delete(Integer siteId, Integer id) {
        return attachmentDao.delete(siteId, id);
    }
}
