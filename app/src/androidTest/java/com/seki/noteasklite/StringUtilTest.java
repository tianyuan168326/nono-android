package com.seki.noteasklite;

import android.test.InstrumentationTestCase;

import ARichText.Util.StringUtils;


/**
 * Created by yuan on 2016/1/13.
 */
public class StringUtilTest extends InstrumentationTestCase {
    public void test() throws Exception {
        String testCase  ="&#65288;&#26234;&#33021;&#26426;&#30340;&#23454;&#26045;&#38;&#42;&#35;&#64;&#42;&#89;&#35;&#42;&#40;&#64;&#89;&#35;&#65289;";
        String unicodeDes = StringUtils.convertNCR2Unicode(testCase);
        String utfStrDes = StringUtils.convertUnicode2UTF8(unicodeDes);
        assertEquals(utfStrDes,"（智能机的实施&*#@*Y#*(@Y#）");
    }
}
