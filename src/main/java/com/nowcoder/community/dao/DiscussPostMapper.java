package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface DiscussPostMapper {
   List<DiscussPost> selectDiscussPosts(int user_id,int offset, int limit);
    int selectDiscussPostRows(int user_id);

    Date selectDate(int id);




}
