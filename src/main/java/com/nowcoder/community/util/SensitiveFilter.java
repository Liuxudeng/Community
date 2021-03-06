package com.nowcoder.community.util;


import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 敏感词过滤操作类
 */

@Component
public class SensitiveFilter {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);
    //替换符号
    private static final String REPLACEMENT="***";
    //根节点
    private TrieNode rootNode = new TrieNode();


    @PostConstruct
    public void init(){

        try(        //得到字节流文件 is
                InputStream is =  this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
             //把字节流转化成缓冲流 效率高一点
             BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ){
            //定义变量存储敏感词
            String keyword;

            while ((keyword= reader.readLine())!=null){
                this.addKeyword(keyword);
            }




        } catch (IOException e) {
            logger.error("加载敏感词文件失败"+e.getMessage());
           // e.printStackTrace();
        }


    }


    /**
     * 将敏感词添加到前缀树中
     */
    private void addKeyword(String keyword){
        TrieNode tempNode = rootNode;

        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);

            TrieNode subNode = tempNode.getSubNode(c);

            //如果节点中没有c
            if(subNode==null){
                //初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c,subNode);
            }

            //如果节点中有值为c的节点，那么指向子节点
            tempNode = subNode;

            //设置结束的标识
            if(i==keyword.length()-1){
                tempNode.setKeywordEnd(true);
            }
        }

    }


    //定义前缀树的节点
    private class TrieNode{

        //关键词结束标识
        public boolean isKeywordEnd = false;

        //子节点(key是下级字符，value是下级节点)
        private Map<Character,TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd(){
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd){
            isKeywordEnd = keywordEnd;
        }

        //添加子节点
        public void addSubNode(Character c,TrieNode node){
            subNodes.put(c,node);
        }

        //获取子节点
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }

    }

    /**
     * 判断是否为符号，放置通过添加符号规避敏感词
     */

    private boolean isSymbol(Character c){
        //0x2E80~0x9FFF是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c)&&(c<0x2E80||c>0x9FFF);
    }


    /**
     * 过滤敏感词方法
     *
     */

    public String filter(String text){
        //判断字符串是否为空
        if(StringUtils.isBlank(text)){
            return text;
        }

        /**
         * 如果不为空，声明三个变量
         */
        /**
         * 指针1 指向根节点
         *
         */

        TrieNode tempNode = rootNode;
        /**
         * 指针2 ，第二个指针指向字符串起始位置
         */

        int begin = 0;
        /**
         * 指针3 在指针2发现敏感词后向后遍历
         */
        int position = 0;

        /**
         * 结果
         */

        StringBuffer sb = new StringBuffer();

        while(position<text.length()){
            char c = text.charAt(position);
            //  如果是特殊符号，那么跳过符号
            if(isSymbol(c)){
                //并且特殊字符出现在根节点位置,将此符号计入结果，让指针2向下走一步
                if(tempNode==rootNode){
                    sb.append(c);
                    begin++;
                }
                //无论特殊符号在开头还是中间，指针3都向下走一步
                position++;
                continue;


            }

            /**
             * 检测下级节点
             */

            tempNode = tempNode.getSubNode(c);
            if(tempNode==null){
                //tempnode为空说明在begin和position之间的字符串不是敏感词,
                sb.append(text.charAt(begin));
                // 如果以begin开头的字符串不是敏感词,从begin+1开始继续检查
              position= ++begin;

                // 重新指向根节点
                tempNode = rootNode;

            }else if(tempNode.isKeywordEnd()){
                 //发现begin和position之间的是敏感词 把敏感词替换为***
                sb.append(REPLACEMENT);
                //在吧第二个指针 第三个指针指向position+1
                begin = ++position;
                //重新指向根节点
                tempNode = rootNode;
            }else {
                //处于检查中状态，父节点匹配完了，继续在前缀树中向下检查下一个字符
                if(position<text.length()-1){
                    position++;
                }

            }
        }

        //将最后一批字符计入结果
        sb.append(text.substring(begin));


        return sb.toString();

    }
}
