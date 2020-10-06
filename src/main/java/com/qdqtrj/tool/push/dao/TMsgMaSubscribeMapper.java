package com.qdqtrj.tool.push.dao;

import com.qdqtrj.tool.push.domain.TMsgMaSubscribe;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TMsgMaSubscribeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TMsgMaSubscribe record);

    int insertSelective(TMsgMaSubscribe record);

    TMsgMaSubscribe selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TMsgMaSubscribe record);

    int updateByPrimaryKey(TMsgMaSubscribe record);

    List<TMsgMaSubscribe> selectByMsgTypeAndMsgName(@Param("msgType") int msgType, @Param("msgName") String msgName);

    int updateByMsgTypeAndMsgName(TMsgMaSubscribe tMsgMaSubscribe);

    List<TMsgMaSubscribe> selectByMsgType(int msgType);

    int deleteByMsgTypeAndName(@Param("msgType") int msgType, @Param("msgName") String msgName);
}