package com.longshihan.collect.apm.io.core;

import com.tencent.matrix.iocanary.core.IOIssue;

import java.util.List;

public interface OnJniIssuePublishListener {
    void onIssuePublish(List<IOIssue> issues);
}
