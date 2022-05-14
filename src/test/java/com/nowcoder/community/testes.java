//package com.nowcoder.community;
//
//
//import com.nowcoder.CommunityApplication;
//import com.nowcoder.community.dao.DiscussPostMapper;
//import com.nowcoder.community.dao.elasticsearch.DiscussPostRepository;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//
////@ContextConfiguration(classes = CommunityApplication.class)
//public class testes {
//
//    @Autowired
//    private DiscussPostRepository discussPostRepository;
//    @Autowired
//    private DiscussPostMapper discussPostMapper;
//
//    @Autowired
//    private ElasticsearchTemplate elasticsearchTemplate;
//
//
//
//    @Test
//    public void testInsert(){
//        discussPostRepository.save(discussPostMapper.selectDiscussPostById(241));
//        discussPostRepository.save(discussPostMapper.selectDiscussPostById(242));
//        discussPostRepository.save(discussPostMapper.selectDiscussPostById(243));
//    }
//
//}