<<<<<<< HEAD
package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface DiscussPostMapper {
   List<DiscussPost> selectDiscussPosts(int user_id,int offset, int limit);
    int selectDiscussPostRows(int user_id);






}
=======
package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface DiscussPostMapper {
   List<DiscussPost> selectDiscussPosts(int user_id,int offset, int limit);
    int selectDiscussPostRows(int user_id);






}
>>>>>>> 4175b6f9ff52e242ec0c9f39bf254a8b79ef3635
