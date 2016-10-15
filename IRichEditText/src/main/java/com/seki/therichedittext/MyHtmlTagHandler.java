package com.seki.therichedittext;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.view.View;

import org.xml.sax.XMLReader;

import java.io.File;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Seki on 2015/10/24.
 */
public class MyHtmlTagHandler implements Html.TagHandler {

    private Context context;
    private boolean tvFlag=false;
    private String fontSize="";

//    private int smallSize;
//    private int mediumSize;
//    private int largeSize;
//    private int scriptSize;

//    private boolean isColor=false;
//    private boolean isBackground=false;
//    private boolean isFontPX=false;
//    private boolean isFontEM=false;
//
//    String background="#FFFFFF";
//    String color = "#000000";
//    int fontPX;//=(int)(mediumSize*context.getResources().getDisplayMetrics().scaledDensity);
//    float fontEM;;//=fontPX;
    public MyHtmlTagHandler(Context context, boolean flag){
        this.context=context;
        tvFlag=flag;
//        if(!tvFlag){
            //smallSize=sp2px(12);
            //mediumSize=sp2px(18);
            //largeSize=sp2px(24);
            //scriptSize=sp2px(9);
//        }else {
//            smallSize=sp2px(10);
//            mediumSize=sp2px(15);
//            largeSize=sp2px(20);
//            scriptSize=sp2px(7.5f);
//        }
    }

    private int sp2px(float sp){
        return (int)(sp*context.getResources().getDisplayMetrics().scaledDensity);
    }

    public void handleTag( boolean opening, String tag, Editable output,
                           XMLReader xmlReader) {
        if(tag.equalsIgnoreCase( "del")||tag.equalsIgnoreCase("strike")||tag.equalsIgnoreCase("s")) {
            processStrike(opening, output);
        }
        if(tag.equalsIgnoreCase( "subfont")) {
            processSub(opening, output);
        }
        if(tag.equalsIgnoreCase( "supfont")) {
            processSup(opening, output);
        }
        if(tag.equalsIgnoreCase("img")&&tvFlag){
            processImg(opening, output);
        }
        if(tag.equalsIgnoreCase("fontsize")){
            processFont(opening, output, xmlReader);
        }
        if(tag.equalsIgnoreCase("span")){
            processSpan(opening, output, xmlReader);
        }
    }
    private void processSpan(boolean opening, Editable output, XMLReader xmlReader) {
        int len = output.length();
            Field elementField = null;
        if(opening) {
            try {
                // get the private attributes of the xmlReader by reflection by rekire
                //http://stackoverflow.com/questions/6952243/how-to-get-an-attribute-from-an-xmlreader?rq=1
                elementField = xmlReader.getClass().getDeclaredField("theNewElement");
                elementField.setAccessible(true);
                Object element = elementField.get(xmlReader);
                Field attsField = element.getClass().getDeclaredField("theAtts");
                attsField.setAccessible(true);
                Object atts = attsField.get(element);
                Field dataField = atts.getClass().getDeclaredField("data");
                dataField.setAccessible(true);
                String[] data = (String[])dataField.get(atts);
                Field lengthField = atts.getClass().getDeclaredField("length");
                lengthField.setAccessible(true);
                int length = (Integer)lengthField.get(atts);
                String attribute = data[4];
                int start=0;
                while (attribute.indexOf(":",start)>=0) {
                    String parse = attribute.substring(start,attribute.indexOf(":"));
                    int ts=parse.indexOf(";",start)+1;
                    String tempParse;
                    if(ts<=0){
                        tempParse=attribute.substring(start);
                    }else{
                        tempParse=attribute.substring(start,ts);
                    }
                    start=ts;
                    if(parse.contains("background-color")){
                        Pattern p  =Pattern.compile("#[\\w]{6}");
                        Matcher m = p.matcher(tempParse);
                        if(m.find()){
                            String background = m.group();
                            //isBackground=true;
                            output.setSpan(new BackgroundColorSpan(Color.parseColor(background)),len,len,Spannable.SPAN_MARK_MARK);
                        //}else {
                    //  //      isBackground=false;
                        }
                    }
                    if(parse.contains("color")&&!parse.contains("background-color")){
                        Pattern p  =Pattern.compile("#[\\w]{6}");
                        Matcher m = p.matcher(tempParse);
                        if(m.find()){
                            String color = m.group();
                            //isColor=true;
                            output.setSpan(new ForegroundColorSpan(Color.parseColor(color)),len,len,Spannable.SPAN_MARK_MARK);
//                        }else {
//                      //      isColor=false;
                        }
                    }
                    if(parse.contains("font-size")){
                        Pattern p  =Pattern.compile("[0-9]+(\\s)?px");
                        Matcher m = p.matcher(tempParse);
                        if(m.find()){
                            String text = m.group();
                            int fontPX=Integer.valueOf(text);
                            //isFontPX=true;
                            output.setSpan(new AbsoluteSizeSpan(fontPX),len,len,Spannable.SPAN_MARK_MARK);
                            //output.setSpan(new BackgroundColorSpan(Color.parseColor(color)),len,len,Spannable.SPAN_MARK_MARK);
                        }else {
                            //isFontPX=false;
                            if(tempParse.toLowerCase().contains("em")) {
                                p = Pattern.compile("[0-9]+.?+[0-9]+");
                                m = p.matcher(tempParse);
                                if (m.find()) {
                                    String text = m.group();
                                    float fontEM=Float.valueOf(text);
                                    //isFontEM = true;
                                    output.setSpan(new RelativeSizeSpan(fontEM), len, len, Spannable.SPAN_MARK_MARK);
//                                }else {
//                                    //isFontPX=false;
//                                    //isFontEM=false;
                                }
                            }
                        }
                    }
                    if(parse.contains("font-weight")){
                        output.setSpan(new StyleSpan(Typeface.BOLD),len,len,Spannable.SPAN_MARK_MARK);
                    }
                    if(parse.contains("font-style")){
                        if(tempParse.contains("italic")||tempParse.contains("oblique")){
                            output.setSpan(new StyleSpan(Typeface.ITALIC),len,len,Spannable.SPAN_MARK_MARK);
                        }
                    }
                    if(start<=0){
                        break;
                    }
                }
                //Pattern p  =Pattern.compile("#[\\w]{6}");
                //Matcher m = p.matcher(attribute);
                //if(m.find()){
                //    color = m.group();
                //    output.setSpan(new BackgroundColorSpan(Color.parseColor(color)),len,len,Spannable.SPAN_MARK_MARK);
                //}
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }

        } else {
        //    if(isFontEM){
                Object obj = getLast(output, RelativeSizeSpan.class);
                int where = output.getSpanStart(obj);
                output.removeSpan(obj);
                if (where != len&&where>=0) {
                    if (((RelativeSizeSpan) obj).getSizeChange() != 1.0f) {
                        output.setSpan(new RelativeSizeSpan(((RelativeSizeSpan) obj).getSizeChange()), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            //}
       //     if(isFontPX){
                obj = getLast(output, AbsoluteSizeSpan.class);
                where = output.getSpanStart(obj);
                output.removeSpan(obj);
                if (where != len&&where>=0) {
                    output.setSpan(new AbsoluteSizeSpan(((AbsoluteSizeSpan)obj).getSize(),false), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            //}
        //    if(isBackground){
                obj = getLast(output, BackgroundColorSpan.class);
                where = output.getSpanStart(obj);
                output.removeSpan(obj);
                if (where != len&&where>=0) {
                    output.setSpan(new BackgroundColorSpan(((BackgroundColorSpan)obj).getBackgroundColor()), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
         //   }
            //if(isColor){
                obj = getLast(output, ForegroundColorSpan.class);
                where = output.getSpanStart(obj);
                output.removeSpan(obj);
                if (where != len&&where>=0) {
                    output.setSpan(new ForegroundColorSpan(((ForegroundColorSpan)obj).getForegroundColor()), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
           // }
                obj=getLast(output,StyleSpan.class);
                where=output.getSpanStart(obj);
                output.removeSpan(obj);
                if(where!=len&&where>=0){
                    output.setSpan(new StyleSpan(((StyleSpan)obj).getStyle()),where,len,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            //Object obj = getLast(output, BackgroundColorSpan.class);
            //int where = output.getSpanStart(obj);
            //output.removeSpan(obj);
            //if (where != len) {
            //        output.setSpan(new BackgroundColorSpan(Color.parseColor(color)), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //}
        }
    }

    private void processFont(boolean opening, Editable output,XMLReader xmlReader) {
        int len = output.length();
        if(opening) {
            Field elementField = null;
            try {
                // get the private attributes of the xmlReader by reflection by rekire
                //http://stackoverflow.com/questions/6952243/how-to-get-an-attribute-from-an-xmlreader?rq=1
                elementField = xmlReader.getClass().getDeclaredField("theNewElement");
                elementField.setAccessible(true);
                Object element = elementField.get(xmlReader);
                Field attsField = element.getClass().getDeclaredField("theAtts");
                attsField.setAccessible(true);
                Object atts = attsField.get(element);
                Field dataField = atts.getClass().getDeclaredField("data");
                dataField.setAccessible(true);
                String[] data = (String[])dataField.get(atts);
                Field lengthField = atts.getClass().getDeclaredField("length");
                lengthField.setAccessible(true);
                int length = (Integer)lengthField.get(atts);
                fontSize = data[4];
                float scaleDensity=context.getResources().getDisplayMetrics().scaledDensity;
                if(fontSize.contentEquals("small")) {
                    output.setSpan(new RelativeSizeSpan(0.75f), len, len, Spannable.SPAN_MARK_MARK);
                }else if(fontSize.contentEquals("large")){
                    output.setSpan(new RelativeSizeSpan(1.25f), len, len, Spannable.SPAN_MARK_MARK);
                }else {
                    output.setSpan(new RelativeSizeSpan(1.0f), len, len, Spannable.SPAN_MARK_MARK);
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            Object obj = getLast(output, AbsoluteSizeSpan.class);
            int where = output.getSpanStart(obj);

            output.removeSpan(obj);

            if (where != len&&where>=0) {
                float scaleDensity=context.getResources().getDisplayMetrics().scaledDensity;
                if(fontSize.contentEquals("small")) {
                    output.setSpan(new RelativeSizeSpan(0.75f), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else if(fontSize.contentEquals("large")){
                    output.setSpan(new RelativeSizeSpan(1.25f), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else {
                    output.setSpan(new RelativeSizeSpan(1.0f), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }

    private void processImg(boolean opening, final Editable output) {
        Spanned s = output;
        ImageSpan[] imageSpans = s.getSpans(0, s.length(), ImageSpan.class);

        for (ImageSpan span : imageSpans) {
            final String image_src = span.getSource();
            final int start = s.getSpanStart(span);
            final int end = s.getSpanEnd(span);
            File file;
            if(image_src.startsWith("http")) {
                file = new File(context.getExternalFilesDir(null), image_src.substring(image_src.lastIndexOf("/"), image_src.length()));
            }else {
                file = new File(image_src);
            }
            final File files=file;
            ClickableSpan click_span = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    if (files.exists()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(files), "image/*");
                        context.startActivity(intent);
                    }
                }
            };
            ClickableSpan[] click_spans = s.getSpans(start, end,
                    ClickableSpan.class);
            if (click_spans.length != 0) {
                for (ClickableSpan c_span : click_spans) {
                    ((Spannable) s).removeSpan(c_span);
                }
            }
            output.setSpan(click_span, start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private void processStrike(boolean opening, Editable output) {
        int len = output.length();
        if(opening) {
            output.setSpan(new StrikethroughSpan(), len, len, Spannable.SPAN_MARK_MARK);
        } else {
            Object obj = getLast(output, StrikethroughSpan.class);
            int where = output.getSpanStart(obj);

            output.removeSpan(obj);

            if (where != len&&where>=0) {
                output.setSpan(new StrikethroughSpan(), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private void processSub( boolean opening, Editable output) {

        int len = output.length();
        if(opening) {
            //output.setSpan(new RelativeSizeSpan(0.5f), len, len, Spannable.SPAN_MARK_MARK);
            output.setSpan(new SubscriptSpan(), len, len, Spannable.SPAN_MARK_MARK);
            //output.setSpan(new AbsoluteSizeSpan(scriptSize,false), len, len, Spannable.SPAN_MARK_MARK);
        } else {
            Object obj = getLast(output, SubscriptSpan.class);
            int where = output.getSpanStart(obj);
            output.removeSpan(obj);
            //obj = getLast(output, RelativeSizeSpan.class);
            //output.removeSpan(obj);
            if (where != len&&where>=0) {
                //output.setSpan(new AbsoluteSizeSpan(scriptSize, false), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                output.setSpan(new RelativeSizeSpan(0.7f), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                output.setSpan(new SubscriptSpan(), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
           // obj = getLast(output, AbsoluteSizeSpan.class);
           // where = output.getSpanStart(obj);
           // output.removeSpan(obj);
           // if (where != len) {
           //     output.setSpan(new AbsoluteSizeSpan(scriptSize, false), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
           //     //output.setSpan(new RelativeSizeSpan(0.5f), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
           // }
        }
    }

    private void processSup( boolean opening, Editable output) {
        int len = output.length();
        if(opening) {
            //output.setSpan(new AbsoluteSizeSpan(scriptSize,false), len, len, Spannable.SPAN_MARK_MARK);
            //output.setSpan(new RelativeSizeSpan(0.5f), len, len, Spannable.SPAN_MARK_MARK);
            output.setSpan(new SuperscriptSpan(), len, len, Spannable.SPAN_MARK_MARK);
        } else {
            Object obj = getLast(output, SuperscriptSpan.class);
            int where = output.getSpanStart(obj);
            output.removeSpan(obj);
            //obj = getLast(output, RelativeSizeSpan.class);
            //output.removeSpan(obj);
            if (where != len&&where>=0) {
                //output.setSpan(new AbsoluteSizeSpan(scriptSize, false), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                output.setSpan(new RelativeSizeSpan(0.7f), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                output.setSpan(new SuperscriptSpan(), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            //obj = getLast(output, AbsoluteSizeSpan.class);
            //where = output.getSpanStart(obj);
            //output.removeSpan(obj);
            //if (where != len) {
            //    output.setSpan(new AbsoluteSizeSpan(scriptSize, false), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //    //output.setSpan(new RelativeSizeSpan(0.5f), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //}
        }
    }

    private Object getLast(Editable text, Class kind) {
        Object[] objs = text.getSpans(0, text.length(), kind);

        if (objs.length == 0) {
            return null;
        } else {
            for(int i = objs.length;i>0;i--) {
                if(text.getSpanFlags(objs[i-1]) == Spannable.SPAN_MARK_MARK) {
                    return objs[i-1];
                }
            }
            return null;
        }
    }
}
