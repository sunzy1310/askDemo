package com.keyten.spider.collect.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class IKAnalyzerUtil {
	public static List<String> segWord(String text) throws IOException {
		List<String> wordList = new ArrayList<>();
        StringReader sr=new StringReader(text);  
        IKSegmenter ik = new IKSegmenter(sr,true); 
        Lexeme lex=null;  
        while((lex = ik.next())!=null){  
            wordList.add(lex.getLexemeText()); 
        } 
        return wordList;
	}
	public static void main(String[] args) {
		try {
			IKAnalyzerUtil.segWord("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
