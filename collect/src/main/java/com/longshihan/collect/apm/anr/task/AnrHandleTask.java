package com.longshihan.collect.apm.anr.task;

import android.os.Looper;
import android.os.Process;
import android.os.SystemClock;

import com.longshihan.collect.apm.fps.AppMethodBeat;
import com.longshihan.collect.apm.fps.UIThreadMonitor;
import com.longshihan.collect.init.Utils;
import com.longshihan.collect.model.MethodItem;
import com.longshihan.collect.utils.Constants;
import com.longshihan.collect.utils.DeviceUtil;
import com.longshihan.collect.utils.MatrixLog;
import com.longshihan.collect.utils.TraceDataUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.longshihan.collect.utils.DeviceUtil.dumpMemory;

/**
 * Anr 收集器
 */
public class AnrHandleTask implements Runnable{
   public AppMethodBeat.IndexRecord beginRecord;
  public   long token;
    public AppMethodBeat.IndexRecord getBeginRecord() {
        return beginRecord;
    }

    public AnrHandleTask() {
    }

    AnrHandleTask(AppMethodBeat.IndexRecord record, long token) {
        this.beginRecord = record;
        this.token = token;
    }


    @Override
    public void run() {
        long curTime = SystemClock.uptimeMillis();
        // process
        int[] processStat = Utils.getProcessPriority(Process.myPid());
        long[] data = AppMethodBeat.getInstance().copyData(beginRecord);
        beginRecord.release();
        String scene = AppMethodBeat.getVisibleScene();

        // memory
        long[] memoryInfo = dumpMemory();

        // Thread state
        Thread.State status = Looper.getMainLooper().getThread().getState();
        StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
        String dumpStack = Utils.getStack(stackTrace, "|*\t\t", 12);

        // frame
        UIThreadMonitor monitor = UIThreadMonitor.getMonitor();
        long inputCost = monitor.getQueueCost(UIThreadMonitor.CALLBACK_INPUT, token);
        long animationCost = monitor.getQueueCost(UIThreadMonitor.CALLBACK_ANIMATION, token);
        long traversalCost = monitor.getQueueCost(UIThreadMonitor.CALLBACK_TRAVERSAL, token);

        // trace
        LinkedList<MethodItem> stack = new LinkedList();
        if (data.length > 0) {
            TraceDataUtils.structuredDataToStack(data, stack, true, curTime);
            TraceDataUtils.trimStack(stack, Constants.TARGET_EVIL_METHOD_STACK, new TraceDataUtils.IStructuredDataFilter() {
                @Override
                public boolean isFilter(long during, int filterCount) {
                    return during < filterCount * Constants.TIME_UPDATE_CYCLE_MS;
                }

                @Override
                public int getFilterMaxCount() {
                    return Constants.FILTER_STACK_MAX_COUNT;
                }

                @Override
                public void fallback(List<MethodItem> stack, int size) {
                    MatrixLog.w(TAG, "[fallback] size:%s targetSize:%s stack:%s", size, Constants.TARGET_EVIL_METHOD_STACK, stack);
                    Iterator iterator = stack.listIterator(Math.min(size, Constants.TARGET_EVIL_METHOD_STACK));
                    while (iterator.hasNext()) {
                        iterator.next();
                        iterator.remove();
                    }
                }
            });
        }

        StringBuilder reportBuilder = new StringBuilder();
        StringBuilder logcatBuilder = new StringBuilder();
        long stackCost = Math.max(Constants.DEFAULT_ANR, TraceDataUtils.stackToString(stack, reportBuilder, logcatBuilder));

        // stackKey
        String stackKey = TraceDataUtils.getTreeKey(stack, stackCost);
        MatrixLog.w(TAG, "%s \npostTime:%s curTime:%s",
                printAnr(scene, processStat, memoryInfo, status, logcatBuilder, false, stack.size(),
                        stackKey, dumpStack, inputCost, animationCost, traversalCost, stackCost), token / Constants.TIME_MILLIS_TO_NANO, curTime); // for logcat

        if (stackCost >= Constants.DEFAULT_ANR_INVALID) {
            MatrixLog.w(TAG, "The checked anr task was not executed on time. "
                    + "The possible reason is that the current process has a low priority. just pass this report");
            return;
        }
        // report
//        try {
//            TracePlugin plugin = Matrix.with().getPluginByClass(TracePlugin.class);
//            if (null == plugin) {
//                return;
//            }
//            JSONObject jsonObject = new JSONObject();
//            jsonObject = DeviceUtil.getDeviceInfo(jsonObject, Matrix.with().getApplication());
//            jsonObject.put(SharePluginInfo.ISSUE_STACK_TYPE, Constants.Type.ANR);
//            jsonObject.put(SharePluginInfo.ISSUE_COST, stackCost);
//            jsonObject.put(SharePluginInfo.ISSUE_STACK_KEY, stackKey);
//            jsonObject.put(SharePluginInfo.ISSUE_SCENE, scene);
//            jsonObject.put(SharePluginInfo.ISSUE_TRACE_STACK, reportBuilder.toString());
//            jsonObject.put(SharePluginInfo.ISSUE_THREAD_STACK, Utils.getStack(stackTrace));
//            jsonObject.put(SharePluginInfo.ISSUE_PROCESS_PRIORITY, processStat[0]);
//            jsonObject.put(SharePluginInfo.ISSUE_PROCESS_NICE, processStat[1]);
//            jsonObject.put(SharePluginInfo.ISSUE_PROCESS_FOREGROUND, isForeground);
//            // memory info
//            JSONObject memJsonObject = new JSONObject();
//            memJsonObject.put(SharePluginInfo.ISSUE_MEMORY_DALVIK, memoryInfo[0]);
//            memJsonObject.put(SharePluginInfo.ISSUE_MEMORY_NATIVE, memoryInfo[1]);
//            memJsonObject.put(SharePluginInfo.ISSUE_MEMORY_VM_SIZE, memoryInfo[2]);
//            jsonObject.put(SharePluginInfo.ISSUE_MEMORY, memJsonObject);
//
//            Issue issue = new Issue();
//            issue.setKey(token + "");
//            issue.setTag(SharePluginInfo.TAG_PLUGIN_EVIL_METHOD);
//            issue.setContent(jsonObject);
//            plugin.onDetectIssue(issue);
//
//        } catch (JSONException e) {
//            MatrixLog.e(TAG, "[JSONException error: %s", e);
//        }

    }



    private String printAnr(String scene, int[] processStat, long[] memoryInfo, Thread.State state, StringBuilder stack, boolean isForeground,
                            long stackSize, String stackKey, String dumpStack, long inputCost, long animationCost, long traversalCost, long stackCost) {
        StringBuilder print = new StringBuilder();
        print.append(String.format("-\n>>>>>>>>>>>>>>>>>>>>>>> maybe happens ANR(%s ms)! <<<<<<<<<<<<<<<<<<<<<<<\n", stackCost));
        print.append("|* [Status]").append("\n");
        print.append("|*\t\tScene: ").append(scene).append("\n");
        print.append("|*\t\tForeground: ").append(isForeground).append("\n");
        print.append("|*\t\tPriority: ").append(processStat[0]).append("\tNice: ").append(processStat[1]).append("\n");
        print.append("|*\t\tis64BitRuntime: ").append(DeviceUtil.is64BitRuntime()).append("\n");

        print.append("|* [Memory]").append("\n");
        print.append("|*\t\tDalvikHeap: ").append(memoryInfo[0]).append("kb\n");
        print.append("|*\t\tNativeHeap: ").append(memoryInfo[1]).append("kb\n");
        print.append("|*\t\tVmSize: ").append(memoryInfo[2]).append("kb\n");
        print.append("|* [doFrame]").append("\n");
        print.append("|*\t\tinputCost:animationCost:traversalCost").append("\n");
        print.append("|*\t\t").append(inputCost).append(":").append(animationCost).append(":").append(traversalCost).append("\n");
        print.append("|* [Thread]").append("\n");
        print.append(String.format("|*\t\tStack(%s): ", state)).append(dumpStack);
        print.append("|* [Trace]").append("\n");
        if (stackSize > 0) {
            print.append("|*\t\tStackKey: ").append(stackKey).append("\n");
            print.append(stack.toString());
        } else {
            print.append(String.format("AppMethodBeat is close[%s].", AppMethodBeat.getInstance().isAlive())).append("\n");
        }
        print.append("=========================================================================");
        return print.toString();
    }
}
