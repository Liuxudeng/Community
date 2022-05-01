package com.nowcoder.community.dao;


import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
public interface LoginTicketMapper {

    /**
     *  插入数据
     */
    @Insert({"insert into login_ticket(user_id,ticket,status,expired) "
            ,"values (#{userId},#{ticket},#{status},#{expired})"
    })

    @Options(useGeneratedKeys = true)
    int insertLoginTicket(LoginTicket loginTicket);

    /**
     *  按照ticket查询
     */
  @Select({
          "select id,user_id,ticket,status,expired",
          "from login_ticket where ticket=#{ticket}"
  })
    LoginTicket selectByTicket(String ticket);

    /**
     *  修改状态
     */
   @Update({
           "update login_ticket set status=#{status} where ticket=#{ticket}"
   })
    int updateStatus(String ticket, int status);

}
