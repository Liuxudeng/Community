package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

@Repository("mybatis")
public class AlphaDaoMybatisImpl implements AlphaDao{
    @Override
    public String select() {
        return "Mybatis";
    }
}
