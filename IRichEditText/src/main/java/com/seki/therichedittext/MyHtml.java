package com.seki.therichedittext;

import android.graphics.Typeface;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.ParagraphStyle;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Seki on 2016/4/16.
 */
public class MyHtml {

    public static String toHtml(Spanned text){
        StringBuilder out = new StringBuilder();
        withinHtml(out, text);
        return out.toString();
    }
    private static void withinHtml(StringBuilder out, Spanned text) {
        int len = text.length();

        int next;
        for (int i = 0; i < text.length(); i = next) {
            next = text.nextSpanTransition(i, len, ParagraphStyle.class);
            ParagraphStyle[] style = text.getSpans(i, next, ParagraphStyle.class);
            String elements = " ";
            boolean needDiv = false;

            for(int j = 0; j < style.length; j++) {
                if (style[j] instanceof AlignmentSpan) {
                    Layout.Alignment align =
                            ((AlignmentSpan) style[j]).getAlignment();
                    needDiv = true;
                    if (align == Layout.Alignment.ALIGN_CENTER) {
                        elements = "align=\"center\" " + elements;
                    } else if (align == Layout.Alignment.ALIGN_OPPOSITE) {
                        elements = "align=\"right\" " + elements;
                    } else {
                        elements = "align=\"left\" " + elements;
                    }
                }
            }
            if (needDiv) {
                out.append("<div ").append(elements).append(">");
            }

            withinDiv(out, text, i, next);

            if (needDiv) {
                out.append("</div>");
            }
        }
    }

    private static void withinDiv(StringBuilder out, Spanned text,
                                  int start, int end) {
        int next;
        for (int i = start; i < end; i = next) {
            next = text.nextSpanTransition(i, end, QuoteSpan.class);
            QuoteSpan[] quotes = text.getSpans(i, next, QuoteSpan.class);

            for (QuoteSpan quote : quotes) {
                out.append("<blockquote>");
            }

            withinBlockquote(out, text, i, next);

            for (QuoteSpan quote : quotes) {
                out.append("</blockquote>\n");
            }
        }
    }

    private static String getOpenParaTagWithDirection(Spanned text, int start, int end) {
        //final int len = end - start;
        //final byte[] levels = ArrayUtils.newUnpaddedByteArray(len);
        //final char[] buffer = TextUtils.obtain(len);
        //TextUtils.getChars(text, start, end, buffer, 0);
//
        //int paraDir = AndroidBidi.bidi(Layout.DIR_REQUEST_DEFAULT_LTR, buffer, levels, len,
        //        false /* no info */);
        //switch(paraDir) {
        //    case Layout.DIR_RIGHT_TO_LEFT:
        //        return "<p dir=\"rtl\">";
        //    case Layout.DIR_LEFT_TO_RIGHT:
        //    default:
        //        return "<p dir=\"ltr\">";
        //}
        return "<p>";
    }

    private static void withinBlockquote(StringBuilder out, Spanned text,
                                         int start, int end) {
        out.append(getOpenParaTagWithDirection(text, start, end));

        int next;
        for (int i = start; i < end; i = next) {
            next = TextUtils.indexOf(text, '\n', i, end);
            if (next < 0) {
                next = end;
            }

            int nl = 0;

            while (next < end && text.charAt(next) == '\n') {
                nl++;
                next++;
            }

            if (withinParagraph(out, text, i, next - nl, nl, next == end)) {
                /* Paragraph should be closed */
                out.append("</p>\n");
                out.append(getOpenParaTagWithDirection(text, next, end));
            }
        }

        out.append("</p>\n");
    }

    public static class StyleComparator implements Comparator<CharacterStyle>{
        @Override
        public int compare(CharacterStyle lhs, CharacterStyle rhs) {
            if(lhs instanceof ForegroundColorSpan && rhs instanceof ForegroundColorSpan ){
                return 0;
            }
            return lhs instanceof ForegroundColorSpan?-1:1;
        }
    }

    private static boolean withinParagraph(StringBuilder out, Spanned text,
                                           int start, int end, int nl,
                                           boolean last) {
        int next;
        for (int i = start; i < end; i = next) {
            next = text.nextSpanTransition(i, end, CharacterStyle.class);
            CharacterStyle[] style = text.getSpans(i, next,
                    CharacterStyle.class);
            Arrays.sort(style,new StyleComparator());
            List<RelativeSizeSpan> relativeSizeSpanList=new ArrayList<>();
            for (int j = 0; j < style.length; j++) {
                if (style[j] instanceof StyleSpan) {
                    int s = ((StyleSpan) style[j]).getStyle();

                    if ((s & Typeface.BOLD) != 0) {
                        out.append("<span style=\"font-weight:bold\">");
                    }
                    if ((s & Typeface.ITALIC) != 0) {
                        out.append("<span style=\"font-style:italic\">");
                    }
                }
                if (style[j] instanceof TypefaceSpan) {
                    String s = ((TypefaceSpan) style[j]).getFamily();

                    if ("monospace".equals(s)) {
                        out.append("<tt>");
                    }
                }
                if (style[j] instanceof SuperscriptSpan) {
                    out.append("<sup>");
                }
                if (style[j] instanceof SubscriptSpan) {
                    out.append("<sub>");
                }
                if (style[j] instanceof UnderlineSpan) {
                    out.append("<u>");
                }
                if (style[j] instanceof StrikethroughSpan) {
                    out.append("<del>");
                }
                if (style[j] instanceof URLSpan) {
                    out.append("<a href=\"");
                    out.append(((URLSpan) style[j]).getURL());
                    out.append("\">");
                }
                if (style[j] instanceof ImageSpan) {
                    out.append("<img src=\"");
                    out.append(((ImageSpan) style[j]).getSource());
                    out.append("\"/>");

                    // Don't output the dummy character underlying the image.
                    i = next;
                }
                if (style[j] instanceof AbsoluteSizeSpan) {
                    out.append("<span style =\"font-size:");
                    out.append(((AbsoluteSizeSpan) style[j]).getSize());
                    out.append("px\">");
                }
                if (style[j] instanceof ForegroundColorSpan) {
                    out.append("<span style =\"color:#");
                    String color = Integer.toHexString(((ForegroundColorSpan)
                            style[j]).getForegroundColor() + 0x01000000);
                    while (color.length() < 6) {
                        color = "0" + color;
                    }
                    out.append(color);
                    out.append("\">");
                }
                if (style[j] instanceof BackgroundColorSpan) {
                    out.append("<span style =\"background-color:#");
                    String color = Integer.toHexString(((BackgroundColorSpan)
                            style[j]).getBackgroundColor() + 0x01000000);
                    while (color.length() < 6) {
                        color = "0" + color;
                    }
                    out.append(color);
                    out.append("\">");
                }
                if(style[j] instanceof RelativeSizeSpan) {
                    relativeSizeSpanList.add((RelativeSizeSpan)style[j]);
                    //out.append("<span style =\"font-size:");
                    //String em = String.valueOf(((RelativeSizeSpan) style[j]).getSizeChange());
                    //out.append(em);
                    //out.append("em\">");
                }
            }
            float size=1.0f;
            for(RelativeSizeSpan span:relativeSizeSpanList){
                if(span.getSizeChange()!=1.0f){
                    size=span.getSizeChange();
                }
            }
            out.append("<span style =\"font-size:");
            String em = String.valueOf(size);
            out.append(em);
            out.append("em\">");

            withinStyle(out, text, i, next);

            //if(relativeSizeSpanList.size()>0){
                out.append("</span>");
            //}

            for (int j = style.length - 1; j >= 0; j--) {
                //if(style[j] instanceof RelativeSizeSpan) {
                //        out.append("</span>");
                //}
                if (style[j] instanceof BackgroundColorSpan) {
                    out.append("</span>");
                }
                if (style[j] instanceof ForegroundColorSpan) {
                    out.append("</span>");
                }
                if (style[j] instanceof AbsoluteSizeSpan) {
                    out.append("</span>");
                }
                if (style[j] instanceof URLSpan) {
                    out.append("</a>");
                }
                if (style[j] instanceof StrikethroughSpan) {
                    out.append("</del>");
                }
                if (style[j] instanceof UnderlineSpan) {
                    out.append("</u>");
                }
                if (style[j] instanceof SubscriptSpan) {
                    out.append("</sub>");
                }
                if (style[j] instanceof SuperscriptSpan) {
                    out.append("</sup>");
                }
                if (style[j] instanceof TypefaceSpan) {
                    String s = ((TypefaceSpan) style[j]).getFamily();

                    if (s.equals("monospace")) {
                        out.append("</tt>");
                    }
                }
                if (style[j] instanceof StyleSpan) {
                    int s = ((StyleSpan) style[j]).getStyle();

                    if ((s & Typeface.BOLD) != 0) {
                        out.append("</span>");
                    }
                    if ((s & Typeface.ITALIC) != 0) {
                        out.append("</span>");
                    }
                }
            }
        }

        if (nl == 1) {
            out.append("<br>\n");
            return false;
        } else {
            for (int i = 2; i < nl; i++) {
                out.append("<br>");
            }
            return !last;
        }
    }


    private static void withinStyle(StringBuilder out, CharSequence text,
                                    int start, int end) {
        for (int i = start; i < end; i++) {
            char c = text.charAt(i);

            if (c == '<') {
                out.append("&lt;");
            } else if (c == '>') {
                out.append("&gt;");
            } else if (c == '&') {
                out.append("&amp;");
            } else if (c >= 0xD800 && c <= 0xDFFF) {
                if (c < 0xDC00 && i + 1 < end) {
                    char d = text.charAt(i + 1);
                    if (d >= 0xDC00 && d <= 0xDFFF) {
                        i++;
                        int codepoint = 0x010000 | (int) c - 0xD800 << 10 | (int) d - 0xDC00;
                        out.append("&#").append(codepoint).append(";");
                    }
                }
            } else if (c > 0x7E || c < ' ') {
                out.append("&#").append((int) c).append(";");
            } else if (c == ' ') {
                while (i + 1 < end && text.charAt(i + 1) == ' ') {
                    out.append("&nbsp;");
                    i++;
                }

                out.append(' ');
            } else {
                out.append(c);
            }
        }
    }


}
