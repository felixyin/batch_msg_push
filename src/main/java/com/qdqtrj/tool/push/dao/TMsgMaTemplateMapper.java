package com.qdqtrj.tool.push.dao;

import com.qdqtrj.tool.push.domain.TMsgMaTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TMsgMaTemplateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TMsgMaTemplate record);

    int insertSelective(TMsgMaTemplate record);

    TMsgMaTemplate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TMsgMaTemplate record);

    int updateByPrimaryKey(TMsgMaTemplate record);

    List<TMsgMaTemplate> selectByMsgTypeAndMsgName(@Param("msgType") int msgType, @Param("msgName") String msgName);

    int updateByMsgTypeAndMsgName(TMsgMaTemplate tMsgMaTemplate);

    List<TMsgMaTemplate> selectByMsgType(int msgType);

    int deleteByMsgTypeAndName(@Param("msgType") int msgType, @Param("msgName") String msgName);
}