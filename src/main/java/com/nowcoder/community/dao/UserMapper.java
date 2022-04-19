package com.nowcoder.community.dao;

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserMapper {
    /**
     *  几个查询方法
     *  根据用户名查询
     *  根据id查询
     *  根据邮箱查询
     */
    User selectById(int id);

    User selectByName(String name);

    User selectByEmail(String email);

    /**
     * 插入操作 返回该行的id
     */
    int insertUser(User user);

    /**
     * 更新操作 返回该行的id
     * @param id
     * @param status
     * @return
     */
    int updateStatus(int id, int status);
    int updateHeader(int id, String headUrl);
    int updatePassword(int id,String password);




}
