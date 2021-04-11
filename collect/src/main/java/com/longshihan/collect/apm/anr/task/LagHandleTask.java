package com.longshihan.collect.apm.anr.task;

import android.os.Looper;
import android.os.Process;

import com.longshihan.collect.apm.fps.AppMethodBeat;
import com.longshihan.collect.init.Utils;
import com.longshihan.collect.utils.DeviceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static com.longshihan.collect.utils.DeviceUtil.dumpMemory;

/**
 * 严格模式的
 *
 */
public class LagHandleTask implements Runnable{
    @Override
    public void run() {
//        String scene = AppMethodBeat.getVisibleScene();
//        boolean isForeground = isForeground();
//        try {
//            TracePlugin plugin = Matrix.with().getPluginByClass(TracePlugin.class);
//            if (null == plugin) {
//                return;
//            }
//
//            StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
//
//            JSONObject jsonObject = new JSONObject();
//            jsonObject = DeviceUtil.getDeviceInfo(jsonObject, Matrix.with().getApplication());
//            jsonObject.put(SharePluginInfo.ISSUE_STACK_TYPE, Constants.Type.LAG);
//            jsonObject.put(SharePluginInfo.ISSUE_SCENE, scene);
//            jsonObject.put(SharePluginInfo.ISSUE_THREAD_STACK, Utils.getStack(stackTrace));
//            jsonObject.put(SharePluginInfo.ISSUE_PROCESS_FOREGROUND, isForeground);
//
//            Issue issue = new Issue();
//            issue.setTag(SharePluginInfo.TAG_PLUGIN_EVIL_METHOD);
//            issue.setContent(jsonObject);
//            plugin.onDetectIssue(issue);
//            MatrixLog.e(TAG, "happens lag : %s ", jsonObject.toString());
//
//        } catch (JSONException e) {
//            MatrixLog.e(TAG, "[JSONException error: %s", e);
//        }

    }

    private String printInputExpired(long inputCost) {
        StringBuilder print = new StringBuilder();
        String scene = AppMethodBeat.getVisibleScene();
        boolean isForeground = true;
        // memory
        long[] memoryInfo = dumpMemory();
        // process
        int[] processStat = Utils.getProcessPriority(Process.myPid());
        print.append(String.format("-\n>>>>>>>>>>>>>>>>>>>>>>> maybe happens Input ANR(%s ms)! <<<<<<<<<<<<<<<<<<<<<<<\n", inputCost));
        print.append("|* [Status]").append("\n");
        print.append("|*\t\tScene: ").append(scene).append("\n");
        print.append("|*\t\tForeground: ").append(isForeground).append("\n");
        print.append("|*\t\tPriority: ").append(processStat[0]).append("\tNice: ").append(processStat[1]).append("\n");
        print.append("|*\t\tis64BitRuntime: ").append(DeviceUtil.is64BitRuntime()).append("\n");
        print.append("|* [Memory]").append("\n");
        print.append("|*\t\tDalvikHeap: ").append(memoryInfo[0]).append("kb\n");
        print.append("|*\t\tNativeHeap: ").append(memoryInfo[1]).append("kb\n");
        print.append("|*\t\tVmSize: ").append(memoryInfo[2]).append("kb\n");
        print.append("=========================================================================");
        return print.toString();
    }

}
