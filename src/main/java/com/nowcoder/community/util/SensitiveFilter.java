package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.ILoggerFactory;
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

@Component
public class SensitiveFilter {
    public static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    //替换符
    private static final String REPLACEMENT = "***";

    //根节点
    private TrieNode rootNode = new TrieNode();

    @PostConstruct      //调用构造器后自动调用（服务启动的时候 这个Bean就初始化了）
    public void init(){
        try(
                //会自动关闭流
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));     //字符流 to 缓冲流
        ){
            String keyword;
            while((keyword = reader.readLine()) != null){
                //添加到前缀树
                this.addKeyword(keyword);
            }
        }catch (IOException e){
            logger.error("加载敏感词文件失败 "+ e.getMessage());
        }
    }

    //将一个敏感词添加到前缀树中
    private void addKeyword(String keyword){
        TrieNode tempNode = rootNode;
        for(int i = 0;i<keyword.length();++i){
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);  //已经有子节点为c的情况

            if(subNode == null){
                //初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c,subNode);
            }
            tempNode = subNode;

            //设置结束的标识
            if(i == keyword.length()-1){
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    /*
    * 过滤敏感词
    * @param text 待过滤的文本
    * @return 过滤后的文本
    * */
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }
        //指针1
        TrieNode tempNode = rootNode;
        //指针2
        int begin = 0;
        //指针3
        int position = 0;
        StringBuilder sb = new StringBuilder();

        while(position < text.length()){
            char c = text.charAt(position);

            // 跳过符号
            if(isSymbol(c)){
                //判断tempNode是否是根节点,将此符号计入结果 让指针2向下走
                if(tempNode == rootNode){
                    sb.append(c);
                    begin++;
                }
                //无论符号在哪 指针3都向下走一步
                position++;
                continue;
            }

            //检查下级节点
            tempNode = tempNode.getSubNode(c);
            if(tempNode == null){
                //下级节点找不到 c 那么就说明begin不是敏感词 可以append
                sb.append(text.charAt(begin));
                //进入下一个位置
                ++begin;
                position = begin;
                tempNode = rootNode;
            }else{
                //结束 是敏感词
                if(tempNode.isKeyWordEnd()){
                    sb.append(REPLACEMENT);
                    ++position;
                    begin = position;
                    tempNode = rootNode;
                }else{
                    //没结束 继续检查下一个字符
                    position++;
                }
            }
        }
        //将最后一批字符计入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }

    //判断是否是符号
    public boolean isSymbol(Character c){
        //c>= 0x2E80 && c<=0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c< 0x2E80 || c>0x9FFF);
    }

    //定义前缀树 (内部类)
    private class TrieNode{
        // 关键词结束的标识
        boolean isKeyWordEnd = false;

        //当前节点的子节点(多个  key是下级节点的字符 value 是下级节点)
        private Map<Character,TrieNode> subNodes = new HashMap<>();

        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }

        //添加子节点
        public void addSubNode(Character c,TrieNode node){
            subNodes.put(c,node);
        }

        //获取自己节点
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }
}
