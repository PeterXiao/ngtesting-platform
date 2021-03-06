package com.ngtesting.platform.service.impl;

import com.ngtesting.platform.config.Constant;
import com.ngtesting.platform.dao.IssueAttachmentDao;
import com.ngtesting.platform.dao.IssueDao;
import com.ngtesting.platform.model.IsuAttachment;
import com.ngtesting.platform.model.IsuIssue;
import com.ngtesting.platform.model.IsuAttachment;
import com.ngtesting.platform.model.TstUser;
import com.ngtesting.platform.service.intf.IssueAttachmentService;
import com.ngtesting.platform.service.intf.IssueHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IssueAttachmentServiceImpl extends BaseServiceImpl implements IssueAttachmentService {
    @Autowired
    IssueAttachmentDao issueAttachmentDao;
    @Autowired
    IssueHistoryService issueHistoryService;
    @Autowired
    IssueDao issueDao;

    @Override
    @Transactional
    public Boolean save(Integer issueId, String name, String path, TstUser user) {
        IsuIssue testIssue = issueDao.get(issueId, user.getDefaultPrjId());
        if (testIssue == null) {
            return false;
        }

        IsuAttachment attach = new IsuAttachment(name, path, issueId, user.getId());
        issueAttachmentDao.save(attach);
        issueHistoryService.saveHistory(user, Constant.EntityAct.attachment_upload, testIssue, name);
        return true;
    }

    @Override
    @Transactional
    public Boolean delete(Integer id, TstUser user) {
        IsuAttachment attach = issueAttachmentDao.get(id);
        IsuIssue testIssue = issueDao.get(attach.getIssueId(), user.getDefaultPrjId());
        if (testIssue == null) {
            return false;
        }

        issueAttachmentDao.delete(id);
        issueHistoryService.saveHistory(user, Constant.EntityAct.attachment_delete, testIssue, attach.getName());

        return true;
    }

}

