package com.longshihan.collect.apm.anr.task;

import android.os.Process;

import com.longshihan.collect.apm.fps.AppMethodBeat;
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

public class AnalyseTask implements Runnable{
    long[] queueCost;
    long[] data;
    long cpuCost;
    long cost;
    long endMs;
    String scene;
    boolean isForeground;

   public AnalyseTask(boolean isForeground, String scene, long[] data, long[] queueCost, long cpuCost, long cost, long endMs) {
        this.isForeground = isForeground;
        this.scene = scene;
        this.cost = cost;
        this.cpuCost = cpuCost;
        this.data = data;
        this.queueCost = queueCost;
        this.endMs = endMs;
    }

    void analyse() {

        // process
        int[] processStat = Utils.getProcessPriority(Process.myPid());
        String usage = Utils.calculateCpuUsage(cpuCost, cost);
        LinkedList<MethodItem> stack = new LinkedList();
        if (data.length > 0) {
            TraceDataUtils.structuredDataToStack(data, stack, true, endMs);
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
        long stackCost = Math.max(cost, TraceDataUtils.stackToString(stack, reportBuilder, logcatBuilder));
        String stackKey = TraceDataUtils.getTreeKey(stack, stackCost);

        MatrixLog.w(TAG, "%s", printEvil(scene, processStat, isForeground, logcatBuilder, stack.size(), stackKey, usage, queueCost[0], queueCost[1], queueCost[2], cost)); // for logcat

        // report
//        try {
//            TracePlugin plugin = Matrix.with().getPluginByClass(TracePlugin.class);
//            if (null == plugin) {
//                return;
//            }
//            JSONObject jsonObject = new JSONObject();
//            jsonObject = DeviceUtil.getDeviceInfo(jsonObject, Matrix.with().getApplication());
//
//            jsonObject.put(SharePluginInfo.ISSUE_STACK_TYPE, Constants.Type.NORMAL);
//            jsonObject.put(SharePluginInfo.ISSUE_COST, stackCost);
//            jsonObject.put(SharePluginInfo.ISSUE_CPU_USAGE, usage);
//            jsonObject.put(SharePluginInfo.ISSUE_SCENE, scene);
//            jsonObject.put(SharePluginInfo.ISSUE_TRACE_STACK, reportBuilder.toString());
//            jsonObject.put(SharePluginInfo.ISSUE_STACK_KEY, stackKey);
//
//            Issue issue = new Issue();
//            issue.setTag(SharePluginInfo.TAG_PLUGIN_EVIL_METHOD);
//            issue.setContent(jsonObject);
//            plugin.onDetectIssue(issue);
//
//        } catch (JSONException e) {
//            MatrixLog.e(TAG, "[JSONException error: %s", e);
//        }

    }

    @Override
    public void run() {
        analyse();
    }

    private String printEvil(String scene, int[] processStat, boolean isForeground, StringBuilder stack, long stackSize, String stackKey, String usage, long inputCost,
                             long animationCost, long traversalCost, long allCost) {
        StringBuilder print = new StringBuilder();
        print.append(String.format("-\n>>>>>>>>>>>>>>>>>>>>> maybe happens Jankiness!(%sms) <<<<<<<<<<<<<<<<<<<<<\n", allCost));
        print.append("|* [Status]").append("\n");
        print.append("|*\t\tScene: ").append(scene).append("\n");
        print.append("|*\t\tForeground: ").append(isForeground).append("\n");
        print.append("|*\t\tPriority: ").append(processStat[0]).append("\tNice: ").append(processStat[1]).append("\n");
        print.append("|*\t\tis64BitRuntime: ").append(DeviceUtil.is64BitRuntime()).append("\n");
        print.append("|*\t\tCPU: ").append(usage).append("\n");
        print.append("|* [doFrame]").append("\n");
        print.append("|*\t\tinputCost:animationCost:traversalCost").append("\n");
        print.append("|*\t\t").append(inputCost).append(":").append(animationCost).append(":").append(traversalCost).append("\n");
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
