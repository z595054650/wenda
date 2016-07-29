package com.zhifu.service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import sun.text.normalizer.Trie;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thinkpad on 2016/7/26.
 */
@Service
public class SensitiveService implements InitializingBean {
    private static final Logger logger= LoggerFactory.getLogger(SensitiveService.class);

    //敏感词的代替词
    private static final String DEFAULT_REPLACEMENT="***";

    //true 表示关键词的终结，false 表示继续
    private class TrieNode{
        private boolean end=false;
        //一个字符对应一个节点
        private Map<Character,TrieNode> subNodes=new HashMap<Character,TrieNode>();

        void addSubNode(Character key,TrieNode node){
            subNodes.put(key,node);
        }

        TrieNode getSubNode(Character key){
            return subNodes.get(key);
        }

        boolean isKeywordEnd(){
            return end;
        }

        void setKeywordEnd(boolean end){
            this.end=end;
        }

        int getSubNodesCount(){
            return subNodes.size();
        }
    }

    //建立根节点
    private TrieNode root=new TrieNode();

    /**
     * 判断是否是一个符号
     */
    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF 东亚文字范围,CharUtils.isAsciiAlphanumeric(c)表示英文
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }


    /**
     * 向树中添加敏感词
     * */
    public void addWord(String lineTxt){
        TrieNode tempNode=root;
        //加入每个字符
        for(int i=0;i<lineTxt.length();i++){
            Character c=lineTxt.charAt(i);

            // 过滤空格
            if (isSymbol(c)) {
                continue;
            }

            TrieNode node=tempNode.getSubNode(c);
            if(node==null){
                node=new TrieNode();
                tempNode.addSubNode(c,node);
            }
            tempNode=node;
            if(i==lineTxt.length()-1){
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词汇
     * */
    public String filter(String content){

        if(StringUtils.isBlank(content)){
            return content;
        }
        StringBuffer sb=new StringBuffer();
        String replacement=DEFAULT_REPLACEMENT;

        TrieNode tempNode=root;
        //你叫开始的位置
        int begin=0;
        //字符比较的位置
        int position=0;
        while(position<content.length()){
            Character c=content.charAt(position);
            // 空格直接跳过
            if (isSymbol(c)) {
                if (tempNode == root) {
                    sb.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }
            tempNode=tempNode.getSubNode(c);

            if(tempNode==null){
                sb.append(content.charAt(begin));
                position=begin+1;
                begin=position;
                //回到根节点
                tempNode=root;
            }else if(tempNode.isKeywordEnd()){
                sb.append(replacement);
                position=position+1;
                begin=position;
                //回到根节点
                tempNode=root;
            }else{
                position++;
            }
        }
        sb.append(content.substring(begin));
        return sb.toString();
    }

    /**
     * 初始化必要的信息
     * 此处加载敏感词
     * */
    @Override
    public void afterPropertiesSet() {

        try{
            InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader br=new BufferedReader(isr);
            String lineTxt=null;
            while((lineTxt=br.readLine())!=null){
                addWord(lineTxt.trim());
            }
            br.close();
            isr.close();
            is.close();
        }catch(Exception e){
            logger.error("加载敏感词失败"+e.getMessage());
        }
    }

    public static void main(String[] args){
        SensitiveService s = new SensitiveService();
        s.addWord("色情");
        s.addWord("好色");
        System.out.print(s.filter("你好X色**情XX"));




    }
}
