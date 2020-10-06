package com.qdqtrj.tool.push.logic.msgsender;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.HttpClientConfig;
import com.aliyuncs.profile.DefaultProfile;
import com.qdqtrj.tool.push.App;
import com.qdqtrj.tool.push.logic.PushControl;
import com.qdqtrj.tool.push.logic.msgmaker.AliyunMsgMaker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * <pre>
 * 阿里云模板短信发送器
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
@Slf4j
public class AliYunMsgSender implements IMsgSender {
    /**
     * 阿里云短信client
     */
    public volatile static IAcsClient iAcsClient;

    private AliyunMsgMaker aliyunMsgMaker;

    public AliYunMsgSender() {
        aliyunMsgMaker = new AliyunMsgMaker();
        iAcsClient = getAliyunIAcsClient();
    }

    @Override
    public SendResult send(String[] msgData) {
        SendResult sendResult = new SendResult();

        try {
            //初始化acsClient,暂不支持region化
            SendSmsRequest sendSmsRequest = aliyunMsgMaker.makeMsg(msgData);
            sendSmsRequest.setPhoneNumbers(msgData[0]);
            if (PushControl.dryRun) {
                sendResult.setSuccess(true);
                return sendResult;
            } else {
                SendSmsResponse response = iAcsClient.getAcsResponse(sendSmsRequest);
                if (response.getCode() != null && "OK".equals(response.getCode())) {
                    sendResult.setSuccess(true);
                } else {
                    sendResult.setSuccess(false);
                    sendResult.setInfo(response.getMessage() + ";ErrorCode:" + response.getCode());
                }
            }
        } catch (Exception e) {
            sendResult.setSuccess(false);
            sendResult.setInfo(e.getMessage());
            log.error(ExceptionUtils.getStackTrace(e));
        }

        return sendResult;
    }

    @Override
    public SendResult asyncSend(String[] msgData) {
        return null;
    }

    /**
     * 获取阿里云短信发送客户端
     *
     * @return IAcsClient
     */
    private static IAcsClient getAliyunIAcsClient() {
        if (iAcsClient == null) {
            synchronized (AliYunMsgSender.class) {
                if (iAcsClient == null) {
                    String aliyunAccessKeyId = App.config.getAliyunAccessKeyId();
                    String aliyunAccessKeySecret = App.config.getAliyunAccessKeySecret();

                    // 创建DefaultAcsClient实例并初始化
                    DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliyunAccessKeyId, aliyunAccessKeySecret);

                    // 多个SDK client共享一个连接池，此处设置该连接池的参数，
                    // 比如每个host的最大连接数，超时时间等
                    HttpClientConfig clientConfig = HttpClientConfig.getDefault();
                    clientConfig.setMaxRequestsPerHost(App.config.getMaxThreadPool());
                    clientConfig.setConnectionTimeoutMillis(10000L);

                    profile.setHttpClientConfig(clientConfig);
                    iAcsClient = new DefaultAcsClient(profile);
                }
            }
        }
        return iAcsClient;
    }
}
