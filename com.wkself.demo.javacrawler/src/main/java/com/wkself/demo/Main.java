package com.wkself.demo;

import com.wkself.demo.bean.LinkTypeData;
import com.wkself.demo.bean.Rule;
import com.wkself.demo.service.ExtractService;

import java.util.List;

/**
 * PackageName com.wkself.demo
 * Created by wangkang on 2017/9/22.
 */
public class Main {
    public static void printf(List<LinkTypeData> datas)
    {
        if(datas == null)
            return;
        for (LinkTypeData data : datas)
        {
            System.out.println(data.getLinkText());
            System.out.println(data.getLinkHref());
            System.out.println("***********************************");
        }
    }
    private static void login(){
        String url = "https://www.zhihu.com/login/phone_num?";
        String _xsrf = "37306362663366352d666130342d346165312d623162322d636339313762666230316564";
        String password = "wkloves0424";
        String captcha_type = "cn";
        String phone_num = "13572171443";
    }
    public static void main(String[] args) {
        Rule rule = new Rule("http://www.zhihu.com",
                null, null,
                "a[href]", Rule.SELECTION, Rule.GET);
        List<LinkTypeData> extracts = ExtractService.extract(rule);
        printf(extracts);
    }
}
