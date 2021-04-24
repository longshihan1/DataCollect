/*
 * Tencent is pleased to support the open source community by making wechat-matrix available.
 * Copyright (C) 2018 THL A29 Limited, a Tencent company. All rights reserved.
 * Licensed under the BSD 3-Clause License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.longshihan.collect.apm.io.core;


import com.longshihan.collect.apm.io.IOPlugin;
import com.longshihan.collect.apm.io.detect.CloseGuardHooker;
import com.longshihan.collect.utils.IOCanaryUtil;
import com.longshihan.collect.utils.MatrixLog;
import com.tencent.matrix.iocanary.core.IOCanaryJniBridge;
import com.tencent.matrix.iocanary.core.IOIssue;

import java.util.List;

/**
 * @author liyongjie
 * Created by liyongjie on 2017/6/6.
 */

public class IOCanaryCore implements OnJniIssuePublishListener {
    private static final String TAG = "测试IO1";

    private final IOPlugin mIoCanaryPlugin;

    private boolean mIsStart;
    private CloseGuardHooker mCloseGuardHooker;

    public IOCanaryCore(IOPlugin ioCanaryPlugin) {
        mIoCanaryPlugin = ioCanaryPlugin;
    }

    public void start() {
        initDetectorsAndHookers();
        synchronized (this) {
            mIsStart = true;
        }
    }

    public synchronized boolean isStart() {
        return mIsStart;
    }

    public void stop() {
        synchronized (this) {
            mIsStart = false;
        }

        if (mCloseGuardHooker != null) {
            mCloseGuardHooker.unHook();
        }

        IOCanaryJniBridge.uninstall();
    }

    private void initDetectorsAndHookers() {

        IOCanaryJniBridge.install(this);


        //if only detect io closeable leak use CloseGuardHooker is Better

        mCloseGuardHooker = new CloseGuardHooker();
        mCloseGuardHooker.hook();

    }

    @Override
    public void onIssuePublish(List<IOIssue> issues) {
        if (issues == null) {
            return;
        }

        for (int i = 0; i < issues.size(); i++) {
            MatrixLog.d(TAG, IOCanaryUtil.convertIOIssueToReportIssue(issues.get(i)));
        }
    }
}
