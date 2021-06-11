package com.example.lab2.utils;


import lombok.extern.slf4j.Slf4j;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * 敏感词检查机，是一个全局的bean
 */
@Component
@Slf4j
public class SensitiveWordChecker {

    Set<String> sensitiveWords;

    @Value("${sensitive.word.list.path}")
    private String sensitiveWordListPath;


    //AC自动机
    private Trie ahoCorasickTrie;


    /**
     * 在项目启动之后，就从文件中读取敏感词数据。
     * sensitiveWordListPath中的敏感词，一行一个
     */
    @PostConstruct
    public void readSensitiveWordListFromFile() throws FileNotFoundException {
        if (sensitiveWordListPath == null) {
            throw new FileNotFoundException("日志文件配置不存在，请去application.properties中配置sensitive.word.list.path");
        }

        log.info(String.format("正在从文件%s中读取敏感词", sensitiveWordListPath));
        sensitiveWords = new HashSet<>();

        //从敏感词列表中，不断从文件中读取每一行的敏感词
        try (Scanner scanner = new Scanner(new File(sensitiveWordListPath))) {
            while (scanner.hasNext()) {
                String singleSensitiveWord = scanner.nextLine();
                sensitiveWords.add(singleSensitiveWord);
            }

            log.info("正在构建AC自动机");
            //构建AC自动机
            this.ahoCorasickTrie = Trie.builder().addKeywords(sensitiveWords).build();
            log.info("AC自动机构建成功");
        } catch (FileNotFoundException e) {
            log.info(String.format("日志文件%s不存在，因此没有敏感词需要添加", sensitiveWordListPath));
        }

    }

    /**
     * 向敏感词检查其中添加一个敏感词的集合
     *
     * @param sensitiveWordCollection
     */
    public synchronized void addToSensitiveList(Collection<String> sensitiveWordCollection) {

        //添加敏感词到Set中
        this.sensitiveWords.addAll(sensitiveWordCollection);

        //重建AC自动机
        this.ahoCorasickTrie = Trie.builder().addKeywords(sensitiveWordCollection).build();
    }


    /**
     * 从敏感词过滤器中移除collection中所有的词
     *
     * @param sensitiveWordCollection
     */
    public synchronized void removeFromSensitiveList(Collection<String> sensitiveWordCollection) {

        //从set中移除
        this.sensitiveWords.removeAll(sensitiveWordCollection);

        //重建AC自动机
        this.ahoCorasickTrie = Trie.builder().addKeywords(sensitiveWordCollection).build();
    }

    /**
     * 利用AC自动机检查是否存下敏感词。如果有，就返回出去
     *
     * @param text
     * @return
     */
    public Collection<Emit> checkText(String text) {
        return this.ahoCorasickTrie.parseText(text);
    }


    /**
     * 在程序结束后，将敏感词列表写入文本以持久化
     */
    @PreDestroy
    public void saveSensitiveWordsToFile() {
        //先尝试创建文件
        File file = new File(sensitiveWordListPath);
        try {
            boolean res = file.createNewFile();
        } catch (IOException e) {
            log.info(String.format("日志文件%s已存在", sensitiveWordListPath));
        }


        try (PrintWriter printWriter = new PrintWriter(this.sensitiveWordListPath)) {
            //输出到文件
            sensitiveWords.forEach(printWriter::println);
            log.info(String.format("持久化敏感词列表%s成功！", sensitiveWordListPath));
        } catch (FileNotFoundException e) {
            log.info(String.format("由于未知原因，日志文件%s不存在（程序不应该运行到这里，因为我在写入文件之前已手动创建了文件），内存中的敏感词已丢失......", sensitiveWordListPath));
        }

    }
}
