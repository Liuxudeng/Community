package com.nowcoder.community.controller;


import com.nowcoder.community.dao.CommentMapper;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private HostHolder hostHolder;


    @RequestMapping(path="/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment){
       // return "redirect:/discuss/detail"+discussPostId;
        System.out.println("--------------------"+new Date());
        comment.setUserId(hostHolder.getUser().getId());
        System.out.println("--------------------"+new Date());

        comment.setStatus(0);
        comment.setCreateTime(new Date());
        System.out.println("--------------------"+new Date());
        commentService.addComment(comment);
        System.out.println("--------------------"+new Date());
        return "redirect:/discuss/detail/"+discussPostId;

    }
}
