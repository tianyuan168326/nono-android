package com.seki.noteasklite.Util;

import android.text.Html;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Seki on 2016/1/25.
 */
public class StringProcessor {

    public static String NCR2Unicode(String src){
        StringBuilder dest=new StringBuilder();
        src=src.replace("\\","&#92;");
        Pattern pattern = Pattern.compile("&#[0-9]+;");
        Matcher matcher = pattern.matcher(src);
        List<MatcherHelper> matcherHelperList = new ArrayList<>();
        if(!matcher.find()){
            return src;
        }
        matcherHelperList.add(new MatcherHelper(matcher.start(),matcher.end()) );
        while(matcher.find()){
            matcherHelperList.add(new MatcherHelper(matcher.start(),matcher.end()) );
        }
        int groupNum = matcherHelperList.size();
        for(int j=0;j<groupNum;j++){
            MatcherHelper helper=matcherHelperList.get(j);
            if(j==0){
                dest=dest.append(src.substring(0,helper.index1));
            }else {
                dest=dest.append(src.substring(matcherHelperList.get(j-1).index2,helper.index1));
            }
            int i=Integer.valueOf(src.substring(helper.index1 + 2, helper.index2-1));
            String des=Integer.toHexString(i);
            StringBuffer buffer=new StringBuffer(des);
            switch (des.length()){
                case 1:buffer.insert(0,"000");
                    break;
                case 2:buffer.insert(0,"00");
                    break;
                case 3:buffer.insert(0,"0");
                    break;
            }
            dest.append("\\u"+buffer);
        }
        dest.append(src.substring(matcherHelperList.get(groupNum-1).index2));
        return dest.toString();
    }

    public static String NCR2UTF8(String src){
        StringBuilder dest=new StringBuilder();
        src=src.replace("\\","&#92;");
        Pattern pattern = Pattern.compile("&#[0-9]+;");
        Matcher matcher = pattern.matcher(src);
        List<MatcherHelper> matcherHelperList = new ArrayList<>();
        if(!matcher.find()){
            return src;
        }
        matcherHelperList.add(new MatcherHelper(matcher.start(),matcher.end()) );
        while(matcher.find()){
            matcherHelperList.add(new MatcherHelper(matcher.start(),matcher.end()) );
        }
        int groupNum = matcherHelperList.size();
        for(int j=0;j<groupNum;j++) {
            MatcherHelper helper = matcherHelperList.get(j);
            if (j == 0) {
                dest = dest.append(src.substring(0, helper.index1));
            } else {
                dest = dest.append(src.substring(matcherHelperList.get(j - 1).index2, helper.index1));
            }
            int i = Integer.valueOf(src.substring(helper.index1 + 2, helper.index2 - 1));
            //String des=Integer.toHexString(i);
            //StringBuffer buffer=new StringBuffer(des);
            //switch (des.length()){
            //    case 1:buffer.insert(0,"000");
            //        break;
            //    case 2:buffer.insert(0,"00");
            //        break;
            //    case 3:buffer.insert(0,"0");
            //        break;
            //}
            if (Character.charCount(i) > 1) {
                String str="&#"+i+";";
                dest.append(str);
            } else {
                dest.append(new String(Character.toChars(i)));
            }
        }
        dest.append(src.substring(matcherHelperList.get(groupNum-1).index2));
        return dest.toString();
    }

    public static String Unicode2NCR(String src){
        StringBuilder dest=new StringBuilder();
        Pattern pattern = Pattern.compile("\\\\u",Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(src);
        List<MatcherHelper> matcherHelperList = new ArrayList<>();
        if(!matcher.find()){
            return src;
        }
        matcherHelperList.add(new MatcherHelper(matcher.start(),matcher.end()+4) );
        while(matcher.find()){
            matcherHelperList.add(new MatcherHelper(matcher.start(),matcher.end()+4) );
        }
        int groupNum = matcherHelperList.size();
        for(int j=0;j<groupNum;j++){
            MatcherHelper helper=matcherHelperList.get(j);
            if(j==0){
                dest=dest.append(src.substring(0,helper.index1));
            }else {
                dest=dest.append(src.substring(matcherHelperList.get(j-1).index2,helper.index1));
            }
            int i=Integer.valueOf(src.substring(helper.index1 + 2, helper.index2), 16);
            String des=String.valueOf(i);
            dest.append("&#"+des+";");
        }
        dest.append(src.substring(matcherHelperList.get(groupNum-1).index2));
        return dest.toString();
    }

    public static String Unicode2UTF8(String src){
        StringBuilder dest=new StringBuilder();
        Pattern pattern = Pattern.compile("\\\\u",Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(src);
        List<MatcherHelper> matcherHelperList = new ArrayList<>();
        if(!matcher.find()){
            return src;
        }
        matcherHelperList.add(new MatcherHelper(matcher.start(), matcher.end() + 4));
        while(matcher.find()){
            matcherHelperList.add(new MatcherHelper(matcher.start(),matcher.end()+4) );
        }
        int groupNum = matcherHelperList.size();
        for(int j=0;j<groupNum;j++){
            MatcherHelper helper=matcherHelperList.get(j);
            if(j==0){
                dest=dest.append(src.substring(0,helper.index1));
            }else {
                dest=dest.append(src.substring(matcherHelperList.get(j-1).index2,helper.index1));
            }
            int i=Integer.valueOf(src.substring(helper.index1 + 2, helper.index2),16);
            String des=String.valueOf(i);
            dest.append(new String(Character.toChars(i)));
        }
        dest.append(src.substring(matcherHelperList.get(groupNum-1).index2));
        return dest.toString();
    }

    public static  class MatcherHelper{
        public int index1;
        public int index2;

        public MatcherHelper(int index1, int index2) {
            this.index1 = index1;
            this.index2 = index2;
        }
    }

    public static String subStr(String s,int beginIndex,int length){
        int endIndex = beginIndex + length;
        endIndex=endIndex>s.length()?s.length():endIndex;
        String processedString ;
        char[] chars=new char[1];
        //in some phone,the endIndex is 0
        try{
            s.getChars(endIndex-1, endIndex, chars, 0);
        }catch (Exception e){
            processedString = "";
            return processedString.replace("\n"," ");
        }

        Character.UnicodeBlock unicodeBlock= Character.UnicodeBlock.of(chars[0]);
        if(unicodeBlock== Character.UnicodeBlock.HIGH_SURROGATES
                ||unicodeBlock== Character.UnicodeBlock.HIGH_PRIVATE_USE_SURROGATES){
            endIndex-=1;
        }
        try{
            processedString = s.substring(beginIndex,endIndex);
        }catch (Exception e){
            processedString = "";
        }
        return processedString.replace("\n"," ");
    }

    public static boolean isEmpty(String s){
        if(s ==null){
            return  true;
        }
        if(s.isEmpty()){
            return  true;
        }
        if(s.length()<1){
            return true;
        }
        return false;
    }
}
