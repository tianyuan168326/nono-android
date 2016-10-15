package com.seki.noteasklite.ViewHolder;

import com.hyphenate.chat.EMMessage;
import com.yuantian.com.easeuitransplant.EaseNotifier;

import java.util.List;

/**
 * Created by yuan-tian01 on 2016/3/6.
 */
public class NONoSuperNotifier extends EaseNotifier {
   public  void onNewCmdMessage(EMMessage message){} ;
    public void onNewCmdMessage(List<EMMessage> messages){} ;
}
