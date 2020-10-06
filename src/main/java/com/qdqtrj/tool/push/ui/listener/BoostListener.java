package com.qdqtrj.tool.push.ui.listener;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.pattern.CronPattern;
import cn.hutool.cron.pattern.CronPatternUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.qdqtrj.tool.push.App;
import com.qdqtrj.tool.push.logic.BoostPushRunThread;
import com.qdqtrj.tool.push.logic.MessageTypeEnum;
import com.qdqtrj.tool.push.logic.PushControl;
import com.qdqtrj.tool.push.logic.PushData;
import com.qdqtrj.tool.push.ui.Consts;
import com.qdqtrj.tool.push.ui.dialog.CommonTipsDialog;
import com.qdqtrj.tool.push.ui.form.BoostForm;
import com.qdqtrj.tool.push.ui.form.MainWindow;
import com.qdqtrj.tool.push.ui.form.MessageEditForm;
import com.qdqtrj.tool.push.ui.form.ScheduleForm;
import com.qdqtrj.tool.push.util.ComponentUtil;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpResponse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * 性能模式监听器
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class BoostListener {

    private static final Log logger = LogFactory.get();

    private static ScheduledExecutorService serviceStartAt;

    private static ScheduledExecutorService serviceStartPerDay;

    private static ScheduledExecutorService serviceStartPerWeek;

    public static void addListeners() {
        BoostForm boostForm = BoostForm.getInstance();
        boostForm.getBoostModeHelpLabel().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                CommonTipsDialog dialog = new CommonTipsDialog();
                ComponentUtil.setPreferSizeAndLocateToCenter(dialog, 0.6, 0.7);
                StringBuilder tipsBuilder = new StringBuilder();
                tipsBuilder.append("<h1>什么是性能模式？</h1>");
                tipsBuilder.append("<h2>最大限度利用系统资源，提升性能，实验性地不断优化，以期获得更快速的批量推送效果</h2>");
                tipsBuilder.append("<p>利用异步HTTP、NIO、协程等技术提高批量推送效率</p>");
                tipsBuilder.append("<p>不断学习使用新技术，优化无止境，不择手段地提升批量推送速度</p>");
                tipsBuilder.append("<p>一个人的力量有限，也希望更多技术大佬提供帮助和支持，一起挑战HTTP极限！</p>");
                tipsBuilder.append("<p><strong>注意：性能模式下CPU、内存、网络连接资源占用过大，" +
                        "执行期间如果出现机器卡顿、浏览器无法访问等属正常现象，推送结束即可自动恢复。</strong></p>");

                dialog.setHtmlText(tipsBuilder.toString());
                dialog.pack();
                dialog.setVisible(true);

                super.mousePressed(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                JLabel label = (JLabel) e.getComponent();
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));
                label.setIcon(new ImageIcon(Consts.HELP_FOCUSED_ICON));
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JLabel label = (JLabel) e.getComponent();
                label.setIcon(new ImageIcon(Consts.HELP_ICON));
                super.mouseExited(e);
            }
        });

        // 开始按钮事件
        boostForm.getStartButton().addActionListener((e) -> ThreadUtil.execute(() -> {
            PushData.boostMode = true;
            if (App.config.getMsgType() != MessageTypeEnum.MP_TEMPLATE_CODE) {
                JOptionPane.showMessageDialog(MainWindow.getInstance().getMainPanel(), "性能模式目前仅支持微信模板消息，后续逐步增加对其他消息类型的支持！", "提示",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (PushControl.pushCheck()) {
                int isPush = JOptionPane.showConfirmDialog(boostForm.getBoostPanel(),
                        "确定开始推送吗？\n\n推送消息：" +
                                MessageEditForm.getInstance().getMsgNameField().getText() +
                                "\n推送人数：" + PushData.allUser.size() +
                                "\n\n空跑模式：" +
                                boostForm.getDryRunCheckBox().isSelected() + "\n", "确认推送？",
                        JOptionPane.YES_NO_OPTION);
                if (isPush == JOptionPane.YES_OPTION) {
                    ThreadUtil.execute(new BoostPushRunThread());
                }
            }
        }));

        // 按计划执行按钮事件
        boostForm.getScheduledRunButton().addActionListener((e -> ThreadUtil.execute(() -> {
            PushData.boostMode = true;
            if (App.config.getMsgType() != MessageTypeEnum.MP_TEMPLATE_CODE) {
                JOptionPane.showMessageDialog(MainWindow.getInstance().getMainPanel(), "性能模式目前仅支持微信模板消息，后续逐步增加对其他消息类型的支持！", "提示",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (PushControl.pushCheck()) {

                // 看是否存在设置的计划任务
                boolean existScheduleTask = false;

                // 定时开始
                if (App.config.isRadioStartAt()) {
                    long startAtMills = DateUtil.parse(App.config.getTextStartAt(), DatePattern.NORM_DATETIME_PATTERN).getTime();
                    if (startAtMills < System.currentTimeMillis()) {
                        JOptionPane.showMessageDialog(boostForm.getBoostPanel(), "计划开始推送时间不能小于系统当前时间！\n\n请检查计划任务设置！\n\n", "提示",
                                JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    int isSchedulePush = JOptionPane.showConfirmDialog(boostForm.getBoostPanel(),
                            "将在" +
                                    App.config.getTextStartAt() +
                                    "推送\n\n消息：" +
                                    MessageEditForm.getInstance().getMsgNameField().getText() +
                                    "\n\n推送人数：" + PushData.allUser.size() +
                                    "\n\n空跑模式：" +
                                    boostForm.getDryRunCheckBox().isSelected(), "确认定时推送？",
                            JOptionPane.YES_NO_OPTION);
                    if (isSchedulePush == JOptionPane.YES_OPTION) {
                        PushData.scheduling = true;
                        // 按钮状态
                        boostForm.getScheduledRunButton().setEnabled(false);
                        boostForm.getStartButton().setEnabled(false);
                        boostForm.getStopButton().setText("停止计划任务");
                        boostForm.getStopButton().setEnabled(true);

                        boostForm.getScheduledTaskLabel().setVisible(true);
                        boostForm.getScheduledTaskLabel().setText("计划任务执行中：将在" +
                                App.config.getTextStartAt() +
                                "开始推送");

                        serviceStartAt = Executors.newSingleThreadScheduledExecutor();
                        serviceStartAt.schedule(new BoostPushRunThread(), startAtMills - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
                    }
                    existScheduleTask = true;
                }

                // 每天固定时间开始
                if (App.config.isRadioPerDay()) {
                    long startPerDayMills = DateUtil.parse(DateUtil.today() + " " + App.config.getTextPerDay(), DatePattern.NORM_DATETIME_PATTERN).getTime();

                    int isSchedulePush = JOptionPane.showConfirmDialog(boostForm.getBoostPanel(),
                            "将在每天" +
                                    App.config.getTextPerDay() +
                                    "推送\n\n消息：" +
                                    MessageEditForm.getInstance().getMsgNameField().getText() +
                                    "\n\n推送人数：" + PushData.allUser.size() +
                                    "\n\n空跑模式：" +
                                    boostForm.getDryRunCheckBox().isSelected(), "确认定时推送？",
                            JOptionPane.YES_NO_OPTION);
                    if (isSchedulePush == JOptionPane.YES_OPTION) {
                        PushData.fixRateScheduling = true;
                        // 按钮状态
                        boostForm.getScheduledRunButton().setEnabled(false);
                        boostForm.getStartButton().setEnabled(false);
                        boostForm.getStopButton().setText("停止计划任务");
                        boostForm.getStopButton().setEnabled(true);

                        boostForm.getScheduledTaskLabel().setVisible(true);
                        boostForm.getScheduledTaskLabel().setText("计划任务执行中：将在每天" +
                                App.config.getTextPerDay() +
                                "开始推送");

                        serviceStartPerDay = Executors.newSingleThreadScheduledExecutor();
                        long millisBetween = startPerDayMills - System.currentTimeMillis();
                        long delay = millisBetween < 0 ? millisBetween + 24 * 60 * 60 * 1000 : millisBetween;
                        serviceStartPerDay.scheduleAtFixedRate(new BoostPushRunThread(), delay, 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
                    }
                    existScheduleTask = true;
                }

                // 每周固定时间开始
                if (App.config.isRadioPerWeek()) {

                    long todaySetMills = DateUtil.parse(DateUtil.today() + " " + App.config.getTextPerWeekTime(), DatePattern.NORM_DATETIME_PATTERN).getTime();
                    int dayBetween = ScheduleForm.getDayOfWeek(App.config.getTextPerWeekWeek()) - DateUtil.thisDayOfWeek();
                    long startPerWeekMills = dayBetween < 0 ? (dayBetween + 7) * 24 * 60 * 60 * 1000 : dayBetween * 24 * 60 * 60 * 1000;

                    int isSchedulePush = JOptionPane.showConfirmDialog(boostForm.getBoostPanel(),
                            "将在每周" + App.config.getTextPerWeekWeek() +
                                    App.config.getTextPerWeekTime() +
                                    "推送\n\n消息：" +
                                    MessageEditForm.getInstance().getMsgNameField().getText() +
                                    "\n\n推送人数：" + PushData.allUser.size() +
                                    "\n\n空跑模式：" +
                                    boostForm.getDryRunCheckBox().isSelected(), "确认定时推送？",
                            JOptionPane.YES_NO_OPTION);
                    if (isSchedulePush == JOptionPane.YES_OPTION) {
                        PushData.scheduling = true;
                        PushData.fixRateScheduling = true;
                        // 按钮状态
                        boostForm.getScheduledRunButton().setEnabled(false);
                        boostForm.getStartButton().setEnabled(false);
                        boostForm.getStopButton().setText("停止计划任务");
                        boostForm.getStopButton().setEnabled(true);

                        boostForm.getScheduledTaskLabel().setVisible(true);
                        boostForm.getScheduledTaskLabel().setText("计划任务执行中：将在每周" +
                                App.config.getTextPerWeekWeek() +
                                App.config.getTextPerWeekTime() +
                                "开始推送");

                        serviceStartPerWeek = Executors.newSingleThreadScheduledExecutor();
                        long millisBetween = startPerWeekMills + todaySetMills - System.currentTimeMillis();
                        long delay = millisBetween < 0 ? millisBetween + 7 * 24 * 60 * 60 * 1000 : millisBetween;
                        serviceStartPerWeek.scheduleAtFixedRate(new BoostPushRunThread(), delay, 7 * 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
                    }
                    existScheduleTask = true;
                }

                // 按Cron表达式触发
                if (App.config.isRadioCron()) {

                    List<String> latest5RunTimeList = Lists.newArrayList();
                    Date now = new Date();
                    for (int i = 0; i < 5; i++) {
                        Date date = CronPatternUtil.nextDateAfter(new CronPattern(App.config.getTextCron()), DateUtils.addDays(now, i), true);
                        latest5RunTimeList.add(DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss"));
                    }

                    int isSchedulePush = JOptionPane.showConfirmDialog(boostForm.getBoostPanel(),
                            "将按" +
                                    App.config.getTextCron() +
                                    "表达式触发推送\n\n" +
                                    "最近5次运行时间:\n" +
                                    String.join("\n", latest5RunTimeList) +
                                    "\n\n消息名称：" +
                                    MessageEditForm.getInstance().getMsgNameField().getText() +
                                    "\n推送人数：" + PushData.allUser.size() +
                                    "\n空跑模式：" +
                                    boostForm.getDryRunCheckBox().isSelected(), "确认定时推送？",
                            JOptionPane.YES_NO_OPTION);
                    if (isSchedulePush == JOptionPane.YES_OPTION) {
                        PushData.fixRateScheduling = true;
                        // 按钮状态
                        boostForm.getScheduledRunButton().setEnabled(false);
                        boostForm.getStartButton().setEnabled(false);
                        boostForm.getStopButton().setText("停止计划任务");
                        boostForm.getStopButton().setEnabled(true);

                        boostForm.getScheduledTaskLabel().setVisible(true);
                        boostForm.getScheduledTaskLabel().setText("计划任务执行中，下一次执行时间：" + latest5RunTimeList.get(0));

                        // 支持秒级别定时任务
                        CronUtil.setMatchSecond(true);
                        CronUtil.schedule(App.config.getTextCron(), (Task) () -> new BoostPushRunThread().start());
                        CronUtil.start();
                    }
                    existScheduleTask = true;
                }

                if (!existScheduleTask) {
                    JOptionPane.showMessageDialog(boostForm.getBoostPanel(), "请先设置计划任务！", "提示",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        })));

        // 停止按钮事件
        boostForm.getStopButton().addActionListener((e) -> {
            ThreadUtil.execute(() -> {
                if (PushData.scheduling) {
                    boostForm.getScheduledTaskLabel().setText("");
                    if (serviceStartAt != null) {
                        serviceStartAt.shutdownNow();
                    }
                    boostForm.getStartButton().setEnabled(true);
                    boostForm.getScheduledRunButton().setEnabled(true);
                    boostForm.getStopButton().setText("停止");
                    boostForm.getStopButton().setEnabled(false);
                    boostForm.getStartButton().updateUI();
                    boostForm.getScheduledRunButton().updateUI();
                    boostForm.getStopButton().updateUI();
                    boostForm.getScheduledTaskLabel().setVisible(false);
                    PushData.scheduling = false;
                    PushData.running = false;
                }

                if (PushData.fixRateScheduling) {
                    boostForm.getScheduledTaskLabel().setText("");
                    if (serviceStartPerDay != null) {
                        serviceStartPerDay.shutdownNow();
                    }
                    if (serviceStartPerWeek != null) {
                        serviceStartPerWeek.shutdownNow();
                    }
                    try {
                        CronUtil.stop();
                    } catch (Exception e1) {
                        logger.warn(e1.toString());
                    }
                    boostForm.getStartButton().setEnabled(true);
                    boostForm.getScheduledRunButton().setEnabled(true);
                    boostForm.getStopButton().setText("停止");
                    boostForm.getStopButton().setEnabled(false);
                    boostForm.getStartButton().updateUI();
                    boostForm.getScheduledRunButton().updateUI();
                    boostForm.getStopButton().updateUI();
                    boostForm.getScheduledTaskLabel().setVisible(false);
                    PushData.fixRateScheduling = false;
                    PushData.running = false;
                }

                if (PushData.running) {
                    int isStop = JOptionPane.showConfirmDialog(boostForm.getBoostPanel(),
                            "确定停止当前的推送吗？", "确认停止？",
                            JOptionPane.YES_NO_OPTION);
                    if (isStop == JOptionPane.YES_OPTION) {
                        PushData.running = false;
                        boostForm.getStartButton().setEnabled(true);
                        boostForm.getScheduledRunButton().setEnabled(true);
                        boostForm.getStopButton().setText("停止");
                        boostForm.getStopButton().setEnabled(false);
                        boostForm.getStartButton().updateUI();
                        boostForm.getScheduledRunButton().updateUI();
                        boostForm.getStopButton().updateUI();
                        boostForm.getScheduledTaskLabel().setVisible(false);
                    }
                }
                for (Future<HttpResponse> httpResponseFuture : BoostPushRunThread.futureList) {
                    httpResponseFuture.cancel(true);
                }
            });
        });
    }

    static void refreshPushInfo() {
        BoostForm boostForm = BoostForm.getInstance();
        // 总记录数
        long totalCount = PushData.allUser.size();
        boostForm.getMemberCountLabel().setText("消息总数：" + totalCount);
        // 可用处理器核心
        boostForm.getProcessorCountLabel().setText("可用处理器核心：" + Runtime.getRuntime().availableProcessors());
        // JVM内存占用
        boostForm.getJvmMemoryLabel().setText("JVM内存占用：" + FileUtil.readableFileSize(Runtime.getRuntime().totalMemory()) + "/" + FileUtil.readableFileSize(Runtime.getRuntime().maxMemory()));
    }
}
