package com.helloscala.web.utils;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Component
public class SensitiveUtil {
    private static final String REPLACEMENT = "***";
    private static final String SENSITIVE_WORD = "sensitive-words.txt";
    private static final Logger logger = LoggerFactory.getLogger(SensitiveUtil.class);

    private static final TrieNode ROOT_NODE = new TrieNode();

    @PostConstruct
    public void init() {
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream(SENSITIVE_WORD);
                BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(is)))
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                this.addKeyword(keyword);
            }
        } catch (Exception e) {
            logger.error("Failed to load sensitive words file!", e);
        }
    }

    private void addKeyword(String keyword) {
        TrieNode tempNode = ROOT_NODE;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);

            if (subNode == null) {
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }

            tempNode = subNode;
            if (i == keyword.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    public static String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        TrieNode tempNode = ROOT_NODE;
        int begin = 0;
        int position = 0;
        StringBuilder sb = new StringBuilder();

        while (position < text.length()) {
            char c = text.charAt(position);
            if (isSymbol(c)) {
                if (tempNode == ROOT_NODE) {
                    sb.append(c);
                    begin++;
                }
                position++;
                continue;
            }

            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                sb.append(text.charAt(begin));
                position = ++begin;
                tempNode = ROOT_NODE;
            } else if (tempNode.isKeywordEnd()) {
                sb.append(REPLACEMENT);
                begin = ++position;
                tempNode = ROOT_NODE;
            } else {
                position++;
            }
        }
        sb.append(text.substring(begin));
        return sb.toString();
    }

    private static boolean isSymbol(Character c) {
        return !isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    public static boolean isAsciiAlpha(char ch) {
        return isAsciiAlphaUpper(ch) || isAsciiAlphaLower(ch);
    }

    public static boolean isAsciiAlphaUpper(char ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    public static boolean isAsciiAlphaLower(char ch) {
        return ch >= 'a' && ch <= 'z';
    }

    public static boolean isAsciiNumeric(char ch) {
        return ch >= '0' && ch <= '9';
    }

    public static boolean isAsciiAlphanumeric(char ch) {
        return isAsciiAlpha(ch) || isAsciiNumeric(ch);
    }

    private static class TrieNode {
        private boolean isKeywordEnd = false;

        private final Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }

}


