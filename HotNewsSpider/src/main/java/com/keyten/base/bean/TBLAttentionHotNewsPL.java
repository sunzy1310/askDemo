package com.keyten.base.bean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value="attentionNewsPL")
@Scope("prototype")
public class TBLAttentionHotNewsPL {
    private String plid;

    private String newsid;

    private String plauthor;

    private String plcontent;

    private String pltype;

    private String dzcount;

    private String pubdate;

    private String pubtime;

    private String makedate;

    private String maketime;

    public String getPlid() {
        return plid;
    }

    public void setPlid(String plid) {
        this.plid = plid == null ? null : plid.trim();
    }

    public String getNewsid() {
        return newsid;
    }

    public void setNewsid(String newsid) {
        this.newsid = newsid == null ? null : newsid.trim();
    }

    public String getPlauthor() {
        return plauthor;
    }

    public void setPlauthor(String plauthor) {
        this.plauthor = plauthor == null ? null : plauthor.trim();
    }

    public String getPlcontent() {
        return plcontent;
    }

    public void setPlcontent(String plcontent) {
        this.plcontent = plcontent == null ? null : plcontent.trim();
    }

    public String getPltype() {
        return pltype;
    }

    public void setPltype(String pltype) {
        this.pltype = pltype == null ? null : pltype.trim();
    }

    public String getDzcount() {
        return dzcount;
    }

    public void setDzcount(String dzcount) {
        this.dzcount = dzcount == null ? null : dzcount.trim();
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate == null ? null : pubdate.trim();
    }

    public String getPubtime() {
        return pubtime;
    }

    public void setPubtime(String pubtime) {
        this.pubtime = pubtime == null ? null : pubtime.trim();
    }

    public String getMakedate() {
        return makedate;
    }

    public void setMakedate(String makedate) {
        this.makedate = makedate == null ? null : makedate.trim();
    }

    public String getMaketime() {
        return maketime;
    }

    public void setMaketime(String maketime) {
        this.maketime = maketime == null ? null : maketime.trim();
    }
}