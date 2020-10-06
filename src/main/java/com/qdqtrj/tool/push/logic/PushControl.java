package com.qdqtrj.tool.push.logic;

import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.qdqtrj.tool.push.App;
import com.qdqtrj.tool.push.dao.TPushHistoryMapper;
import com.qdqtrj.tool.push.domain.TPushHistory;
import com.qdqtrj.tool.push.logic.msgmaker.MsgMakerFactory;
import com.qdqtrj.tool.push.logic.msgmaker.WxMaTemplateMsgMaker;
import com.qdqtrj.tool.push.logic.msgmaker.WxMpTemplateMsgMaker;
import com.qdqtrj.tool.push.logic.msgsender.IMsgSender;
import com.qdqtrj.tool.push.logic.msgsender.MailMsgSender;
import com.qdqtrj.tool.push.logic.msgsender.MsgSenderFactory;
import com.qdqtrj.tool.push.logic.msgsender.SendResult;
import com.qdqtrj.tool.push.ui.Consts;
import com.qdqtrj.tool.push.ui.form.MainWindow;
import com.qdqtrj.tool.push.ui.form.MessageEditForm;
import com.qdqtrj.tool.push.ui.form.PushForm;
import com.qdqtrj.tool.push.ui.form.PushHisForm;
import com.qdqtrj.tool.push.ui.form.ScheduleForm;
import com.qdqtrj.tool.push.ui.form.SettingForm;
import com.qdqtrj.tool.push.ui.listener.MemberListener;
import com.qdqtrj.tool.push.util.ConsoleUtil;
import com.qdqtrj.tool.push.util.MybatisUtil;
import com.qdqtrj.tool.push.util.SqliteUtil;
import com.qdqtrj.tool.push.util.SystemUtil;
import com.opencsv.CSVWriter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <pre>
 * 推送控制
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class PushControl {

    /**
     * 是否空跑
     */
    public static boolean dryRun;

    public volatile static boolean saveResponseBody = false;

    private static TPushHistoryMapper pushHistoryMapper = MybatisUtil.getSqlSession().getMapper(TPushHistoryMapper.class);

    /**
     * 模板变量前缀
     */
    public static final String TEMPLATE_VAR_PREFIX = "var";

    /**
     * 预览消息
     */
    public static List<SendResult> preview() {
        List<SendResult> sendResultList = new ArrayList<>();
        if (!configCheck()) {
            return null;
        }
        List<String[]> msgDataList = new ArrayList<>();
        for (String data : MessageEditForm.getInstance().getPreviewUserField().getText().split(";")) {
            msgDataList.add(data.split(MemberListener.TXT_FILE_DATA_SEPERATOR_REGEX));
        }

        // 准备消息构造器
        prepareMsgMaker();
        IMsgSender msgSender = MsgSenderFactory.getMsgSender();

        dryRun = false;
        if (msgSender != null) {
            for (String[] msgData : msgDataList) {
                sendResultList.add(msgSender.send(msgData));
            }
        } else {
            return null;
        }

        return sendResultList;
    }

    /**
     * 推送前检查
     *
     * @return boolean
     */
    public static boolean pushCheck() {
        MainWindow mainWindow = MainWindow.getInstance();
        PushForm pushForm = PushForm.getInstance();

        if (StringUtils.isEmpty(MessageEditForm.getInstance().getMsgNameField().getText())) {
            JOptionPane.showMessageDialog(mainWindow.getMainPanel(), "请先选择一条消息！", "提示",
                    JOptionPane.INFORMATION_MESSAGE);
            mainWindow.getTabbedPane().setSelectedIndex(2);

            return false;
        }
        if (PushData.allUser == null || PushData.allUser.size() == 0) {
            int msgType = App.config.getMsgType();
            String tipsTitle = "请先准备目标用户！";
            if (msgType == MessageTypeEnum.HTTP_CODE) {
                tipsTitle = "请先准备消息变量！";
            }
            JOptionPane.showMessageDialog(mainWindow.getMainPanel(), tipsTitle, "提示",
                    JOptionPane.INFORMATION_MESSAGE);

            return false;
        }
        if (!PushData.boostMode) {
            if ("0".equals(pushForm.getMaxThreadPoolTextField().getText()) || StringUtils.isEmpty(pushForm.getMaxThreadPoolTextField().getText())) {
                JOptionPane.showMessageDialog(pushForm.getPushPanel(), "请设置每页分配用户数！", "提示",
                        JOptionPane.INFORMATION_MESSAGE);

                return false;
            }
            if ("0".equals(pushForm.getThreadCountTextField().getText()) || StringUtils.isEmpty(pushForm.getThreadCountTextField().getText())) {
                JOptionPane.showMessageDialog(pushForm.getPushPanel(), "请设置每个线程分配的页数！", "提示",
                        JOptionPane.INFORMATION_MESSAGE);

                return false;
            }
        }

        return configCheck();
    }

    /**
     * 配置检查
     *
     * @return
     */
    public static boolean configCheck() {
        SettingForm settingForm = SettingForm.getInstance();

        int msgType = App.config.getMsgType();
        switch (msgType) {
            case MessageTypeEnum.MP_TEMPLATE_CODE:
            case MessageTypeEnum.KEFU_CODE:
            case MessageTypeEnum.KEFU_PRIORITY_CODE:
            case MessageTypeEnum.WX_UNIFORM_MESSAGE_CODE: {
                if (App.config.isMpUseOutSideAt()) {
                    if (App.config.isMpManualAt() &&
                            (StringUtils.isEmpty(App.config.getMpAt()) || StringUtils.isEmpty(App.config.getMpAtExpiresIn()))) {
                        JOptionPane.showMessageDialog(settingForm.getSettingPanel(), "请先在设置中填写并保存手动输入的外部accessToken信息！", "提示",
                                JOptionPane.INFORMATION_MESSAGE);
                        return false;
                    }
                    if (App.config.isMpApiAt() && StringUtils.isEmpty(App.config.getMpAtApiUrl())) {
                        JOptionPane.showMessageDialog(settingForm.getSettingPanel(), "请先在设置中填写并保存用于获取外部accessToken的URL！", "提示",
                                JOptionPane.INFORMATION_MESSAGE);
                        return false;
                    }
                    return true;
                }
                if (StringUtils.isEmpty(App.config.getWechatAppId()) || StringUtils.isEmpty(App.config.getWechatAppSecret())) {
                    JOptionPane.showMessageDialog(settingForm.getSettingPanel(), "请先在设置中填写并保存公众号相关配置！", "提示",
                            JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
                break;
            }
            case MessageTypeEnum.MA_TEMPLATE_CODE:
            case MessageTypeEnum.MA_SUBSCRIBE_CODE:
                if (StringUtils.isEmpty(App.config.getMiniAppAppId()) || StringUtils.isEmpty(App.config.getMiniAppAppSecret())) {
                    JOptionPane.showMessageDialog(settingForm.getSettingPanel(), "请先在设置中填写并保存小程序相关配置！", "提示",
                            JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
                break;
            case MessageTypeEnum.ALI_YUN_CODE:
                String aliyunAccessKeyId = App.config.getAliyunAccessKeyId();
                String aliyunAccessKeySecret = App.config.getAliyunAccessKeySecret();

                if (StringUtils.isEmpty(aliyunAccessKeyId) || StringUtils.isEmpty(aliyunAccessKeySecret)) {
                    JOptionPane.showMessageDialog(settingForm.getSettingPanel(),
                            "请先在设置中填写并保存阿里云短信相关配置！", "提示",
                            JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
                break;
            case MessageTypeEnum.TX_YUN_CODE:
                String txyunAppId = App.config.getTxyunAppId();
                String txyunAppKey = App.config.getTxyunAppKey();

                if (StringUtils.isEmpty(txyunAppId) || StringUtils.isEmpty(txyunAppKey)) {
                    JOptionPane.showMessageDialog(settingForm.getSettingPanel(),
                            "请先在设置中填写并保存腾讯云短信相关配置！", "提示",
                            JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
                break;
            case MessageTypeEnum.QI_NIU_YUN_CODE:
                String qiniuAccessKey = App.config.getQiniuAccessKey();
                String qiniuSecretKey = App.config.getQiniuSecretKey();

                if (StringUtils.isEmpty(qiniuAccessKey) || StringUtils.isEmpty(qiniuSecretKey)) {
                    JOptionPane.showMessageDialog(settingForm.getSettingPanel(),
                            "请先在设置中填写并保存七牛云短信相关配置！", "提示",
                            JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
                break;
            case MessageTypeEnum.UP_YUN_CODE:
                String upAuthorizationToken = App.config.getUpAuthorizationToken();

                if (StringUtils.isEmpty(upAuthorizationToken)) {
                    JOptionPane.showMessageDialog(settingForm.getSettingPanel(),
                            "请先在设置中填写并保存又拍云短信相关配置！", "提示",
                            JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
                break;
            case MessageTypeEnum.BD_YUN_CODE:
                String bdAccessKeyId = App.config.getBdAccessKeyId();
                String bdSecretAccessKey = App.config.getBdSecretAccessKey();

                if (StringUtils.isEmpty(bdAccessKeyId) || StringUtils.isEmpty(bdSecretAccessKey)) {
                    JOptionPane.showMessageDialog(settingForm.getSettingPanel(),
                            "请先在设置中填写并保存百度云短信相关配置！", "提示",
                            JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
                break;
            case MessageTypeEnum.HW_YUN_CODE:
                String hwAppKey = App.config.getHwAppKey();
                String hwAppSecretPassword = App.config.getHwAppSecretPassword();

                if (StringUtils.isEmpty(hwAppKey) || StringUtils.isEmpty(hwAppSecretPassword)) {
                    JOptionPane.showMessageDialog(settingForm.getSettingPanel(),
                            "请先在设置中填写并保存华为云短信相关配置！", "提示",
                            JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
                break;
            case MessageTypeEnum.YUN_PIAN_CODE:
                String yunpianApiKey = App.config.getYunpianApiKey();
                if (StringUtils.isEmpty(yunpianApiKey)) {
                    JOptionPane.showMessageDialog(settingForm.getSettingPanel(),
                            "请先在设置中填写并保存云片网短信相关配置！", "提示",
                            JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
                break;
            case MessageTypeEnum.EMAIL_CODE:
                String mailHost = App.config.getMailHost();
                String mailFrom = App.config.getMailFrom();
                if (StringUtils.isBlank(mailHost) || StringUtils.isBlank(mailFrom)) {
                    JOptionPane.showMessageDialog(settingForm.getSettingPanel(),
                            "请先在设置中填写并保存E-Mail相关配置！", "提示",
                            JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
                break;
            case MessageTypeEnum.WX_CP_CODE:
                String wxCpCorpId = App.config.getWxCpCorpId();
                if (StringUtils.isBlank(wxCpCorpId)) {
                    JOptionPane.showMessageDialog(settingForm.getSettingPanel(),
                            "请先在设置中填写并保存微信企业号/企业微信相关配置！", "提示",
                            JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
                break;
            default:
        }
        return true;
    }

    /**
     * 推送停止或结束后保存数据
     */
    static void savePushData() throws IOException {
        MessageEditForm messageEditForm = MessageEditForm.getInstance();
        File pushHisDir = new File(SystemUtil.configHome + "data" + File.separator + "push_his");
        if (!pushHisDir.exists()) {
            pushHisDir.mkdirs();
        }

        String msgName = messageEditForm.getMsgNameField().getText();
        String nowTime = DateUtil.now().replace(":", "_").replace(" ", "_");
        CSVWriter writer;
        int msgType = App.config.getMsgType();

        List<File> fileList = new ArrayList<>();
        // 保存已发送
        if (PushData.sendSuccessList.size() > 0) {
            File sendSuccessFile = new File(SystemUtil.configHome + "data" +
                    File.separator + "push_his" + File.separator + MessageTypeEnum.getName(msgType) + "-" + msgName +
                    "-发送成功-" + nowTime + ".csv");
            FileUtil.touch(sendSuccessFile);
            writer = new CSVWriter(new FileWriter(sendSuccessFile));

            for (String[] str : PushData.sendSuccessList) {
                writer.writeNext(str);
            }
            writer.close();

            savePushResult(msgName, "发送成功", sendSuccessFile);
            fileList.add(sendSuccessFile);
            // 保存累计推送总数
            App.config.setPushTotal(App.config.getPushTotal() + PushData.sendSuccessList.size());
            App.config.save();
        }

        // 保存未发送
        for (String[] str : PushData.sendSuccessList) {
            if (msgType == MessageTypeEnum.HTTP_CODE && PushControl.saveResponseBody) {
                str = ArrayUtils.remove(str, str.length - 1);
                String[] finalStr = str;
                PushData.toSendList = PushData.toSendList.stream().filter(strings -> !JSONUtil.toJsonStr(strings).equals(JSONUtil.toJsonStr(finalStr))).collect(Collectors.toList());
            } else {
                PushData.toSendList.remove(str);
            }
        }
        for (String[] str : PushData.sendFailList) {
            if (msgType == MessageTypeEnum.HTTP_CODE && PushControl.saveResponseBody) {
                str = ArrayUtils.remove(str, str.length - 1);
                String[] finalStr = str;
                PushData.toSendList = PushData.toSendList.stream().filter(strings -> !JSONUtil.toJsonStr(strings).equals(JSONUtil.toJsonStr(finalStr))).collect(Collectors.toList());
            } else {
                PushData.toSendList.remove(str);
            }
        }

        if (PushData.toSendList.size() > 0) {
            File unSendFile = new File(SystemUtil.configHome + "data" + File.separator +
                    "push_his" + File.separator + MessageTypeEnum.getName(msgType) + "-" + msgName + "-未发送-" + nowTime +
                    ".csv");
            FileUtil.touch(unSendFile);
            writer = new CSVWriter(new FileWriter(unSendFile));
            for (String[] str : PushData.toSendList) {
                writer.writeNext(str);
            }
            writer.close();

            savePushResult(msgName, "未发送", unSendFile);
            fileList.add(unSendFile);
        }

        // 保存发送失败
        if (PushData.sendFailList.size() > 0) {
            File failSendFile = new File(SystemUtil.configHome + "data" + File.separator +
                    "push_his" + File.separator + MessageTypeEnum.getName(msgType) + "-" + msgName + "-发送失败-" + nowTime + ".csv");
            FileUtil.touch(failSendFile);
            writer = new CSVWriter(new FileWriter(failSendFile));
            for (String[] str : PushData.sendFailList) {
                writer.writeNext(str);
            }
            writer.close();

            savePushResult(msgName, "发送失败", failSendFile);
            fileList.add(failSendFile);
        }

        PushHisForm.init();

        // 发送推送结果邮件
        if ((PushData.scheduling || PushData.fixRateScheduling)
                && ScheduleForm.getInstance().getSendPushResultCheckBox().isSelected()) {
            ConsoleUtil.consoleWithLog("发送推送结果邮件开始");
            String mailResultTo = ScheduleForm.getInstance().getMailResultToTextField().getText().replace("；", ";").replace(" ", "");
            String[] mailTos = mailResultTo.split(";");
            ArrayList<String> mailToList = new ArrayList<>(Arrays.asList(mailTos));

            MailMsgSender mailMsgSender = new MailMsgSender();
            String title = "BatchMsgPush推送结果：【" + messageEditForm.getMsgNameField().getText()
                    + "】" + PushData.sendSuccessList.size() + "成功；" + PushData.sendFailList.size() + "失败；"
                    + PushData.toSendList.size() + "未发送";
            StringBuilder contentBuilder = new StringBuilder();
            contentBuilder.append("<h2>BatchMsgPush推送结果</h2>");
            contentBuilder.append("<p>消息类型：" + MessageTypeEnum.getName(App.config.getMsgType()) + "</p>");
            contentBuilder.append("<p>消息名称：" + messageEditForm.getMsgNameField().getText() + "</p>");
            contentBuilder.append("<br/>");

            contentBuilder.append("<p style='color:green'><strong>成功数：" + PushData.sendSuccessList.size() + "</strong></p>");
            contentBuilder.append("<p style='color:red'><strong>失败数：" + PushData.sendFailList.size() + "</strong></p>");
            contentBuilder.append("<p>未推送数：" + PushData.toSendList.size() + "</p>");
            contentBuilder.append("<br/>");

            contentBuilder.append("<p>开始时间：" + DateFormatUtils.format(new Date(PushData.startTime), "yyyy-MM-dd HH:mm:ss") + "</p>");
            contentBuilder.append("<p>完毕时间：" + DateFormatUtils.format(new Date(PushData.endTime), "yyyy-MM-dd HH:mm:ss") + "</p>");
            contentBuilder.append("<p>总耗时：" + DateUtil.formatBetween(PushData.endTime - PushData.startTime, BetweenFormater.Level.SECOND) + "</p>");
            contentBuilder.append("<br/>");

            contentBuilder.append("<p>详情请查看附件</p>");

            contentBuilder.append("<br/>");
            contentBuilder.append("<hr/>");
            contentBuilder.append("<p>来自BatchMsgPush，一款专注于批量推送的小而美的工具</p>");
            contentBuilder.append("<img alt=\"BatchMsgPush\" src=\"" + Consts.INTRODUCE_QRCODE_URL + "\">");

            File[] files = new File[fileList.size()];
            fileList.toArray(files);
            mailMsgSender.sendPushResultMail(mailToList, title, contentBuilder.toString(), files);
            ConsoleUtil.consoleWithLog("发送推送结果邮件结束");
        }
    }

    /**
     * 保存结果到DB
     *
     * @param msgName
     * @param resultInfo
     * @param file
     */
    private static void savePushResult(String msgName, String resultInfo, File file) {
        TPushHistory tPushHistory = new TPushHistory();
        String now = SqliteUtil.nowDateForSqlite();
        tPushHistory.setMsgType(App.config.getMsgType());
        tPushHistory.setMsgName(msgName);
        tPushHistory.setResult(resultInfo);
        tPushHistory.setCsvFile(file.getAbsolutePath());
        tPushHistory.setCreateTime(now);
        tPushHistory.setModifiedTime(now);

        pushHistoryMapper.insertSelective(tPushHistory);
    }

    /**
     * 准备消息构造器
     */
    static void prepareMsgMaker() {
        if (App.config.getMsgType() == MessageTypeEnum.WX_UNIFORM_MESSAGE_CODE) {
            new WxMpTemplateMsgMaker().prepare();
            new WxMaTemplateMsgMaker().prepare();
        } else {
            MsgMakerFactory.getMsgMaker().prepare();
        }
    }

    /**
     * 重新导入目标用户(定时任务)
     */
    public static void reimportMembers() {
        if (PushData.fixRateScheduling && ScheduleForm.getInstance().getReimportCheckBox().isSelected()) {
            switch ((String) ScheduleForm.getInstance().getReimportComboBox().getSelectedItem()) {
                case "通过SQL导入":
                    MemberListener.importFromSql();
                    break;
                case "通过文件导入":
                    MemberListener.importFromFile();
                    break;
                case "导入所有关注公众号的用户":
                    MemberListener.importWxAll();
                    break;
                case "导入企业通讯录中所有用户":
                    MemberListener.importWxCpAll();
                    break;
                default:
            }
        }
    }

}