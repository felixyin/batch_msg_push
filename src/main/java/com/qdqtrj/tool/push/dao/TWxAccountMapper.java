package com.qdqtrj.tool.push.dao;

import com.qdqtrj.tool.push.domain.TWxAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TWxAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TWxAccount record);

    int insertSelective(TWxAccount record);

    TWxAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TWxAccount record);

    int updateByPrimaryKey(TWxAccount record);

    List<TWxAccount> selectByAccountTypeAndAccountName(@Param("accountType") String accountType, @Param("accountName") String accountName);

    List<TWxAccount> selectByAccountType(String wxAccountType);
}