package com.qdqtrj.tool.push.ui.frame;

import cn.hutool.core.thread.ThreadUtil;
import com.apple.eawt.Application;
import com.qdqtrj.tool.push.ui.Consts;
import com.qdqtrj.tool.push.ui.listener.AboutListener;
import com.qdqtrj.tool.push.ui.listener.BoostListener;
import com.qdqtrj.tool.push.ui.listener.FrameListener;
import com.qdqtrj.tool.push.ui.listener.HelpListener;
import com.qdqtrj.tool.push.ui.listener.MemberListener;
import com.qdqtrj.tool.push.ui.listener.MessageTypeListener;
import com.qdqtrj.tool.push.ui.listener.MsgEditListener;
import com.qdqtrj.tool.push.ui.listener.MsgManageListener;
import com.qdqtrj.tool.push.ui.listener.PushHisListener;
import com.qdqtrj.tool.push.ui.listener.PushListener;
import com.qdqtrj.tool.push.ui.listener.ScheduleListener;
import com.qdqtrj.tool.push.ui.listener.SettingListener;
import com.qdqtrj.tool.push.ui.listener.TabListener;
import com.qdqtrj.tool.push.util.ComponentUtil;
import com.qdqtrj.tool.push.util.SystemUtil;
import org.apache.commons.compress.utils.Lists;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * <pre>
 * 主窗口
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class MainFrame extends JFrame {

    private static final long serialVersionUID = -332963894416012132L;

    public void init() {
        this.setName(Consts.APP_NAME);
        this.setTitle(Consts.APP_NAME);
        List<Image> images = Lists.newArrayList();
        images.add(Consts.IMAGE_LOGO_1024);
        images.add(Consts.IMAGE_LOGO_512);
        images.add(Consts.IMAGE_LOGO_256);
        images.add(Consts.IMAGE_LOGO_128);
        images.add(Consts.IMAGE_LOGO_64);
        images.add(Consts.IMAGE_LOGO_48);
        images.add(Consts.IMAGE_LOGO_32);
        images.add(Consts.IMAGE_LOGO_24);
        images.add(Consts.IMAGE_LOGO_16);
        this.setIconImages(images);
        // Mac系统Dock图标
        if (SystemUtil.isMacOs()) {
            Application application = Application.getApplication();
            application.setDockIconImage(Consts.IMAGE_LOGO_1024);
            application.setEnabledAboutMenu(false);
            application.setEnabledPreferencesMenu(false);
        }

        ComponentUtil.setPreferSizeAndLocateToCenter(this, 0.8, 0.88);
    }

    /**
     * 添加事件监听
     */
    public void addListeners() {
        ThreadUtil.execute(MessageTypeListener::addListeners);
        ThreadUtil.execute(AboutListener::addListeners);
        ThreadUtil.execute(HelpListener::addListeners);
        ThreadUtil.execute(PushHisListener::addListeners);
        ThreadUtil.execute(SettingListener::addListeners);
        ThreadUtil.execute(MsgEditListener::addListeners);
        ThreadUtil.execute(MsgManageListener::addListeners);
        ThreadUtil.execute(MemberListener::addListeners);
        ThreadUtil.execute(PushListener::addListeners);
        ThreadUtil.execute(BoostListener::addListeners);
        ThreadUtil.execute(ScheduleListener::addListeners);
        ThreadUtil.execute(TabListener::addListeners);
        ThreadUtil.execute(FrameListener::addListeners);
    }
}
