package com.keyten.base.bean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("hotNews")
@Scope("prototype")
public class TBLHotNews {
    private String newsid;

    private String targetid;

    private String columnid;

    private String thingid;

    private String newsurl;

    private String newsplurl;

    private String newstitle;

    private String newsauthor;

    private String newssource;

    private String newscontent;

    private String pubdate;

    private String pubtime;

    private String makedate;

    private String maketime;

    private String plpage;

    private String collectcount;

    private String lastcollectdate;

    private String lastcollecttime;

    public String getNewsid() {
        return newsid;
    }

    public void setNewsid(String newsid) {
        this.newsid = newsid == null ? null : newsid.trim();
    }

    public String getTargetid() {
        return targetid;
    }

    public void setTargetid(String targetid) {
        this.targetid = targetid == null ? null : targetid.trim();
    }

    public String getColumnid() {
        return columnid;
    }

    public void setColumnid(String columnid) {
        this.columnid = columnid == null ? null : columnid.trim();
    }

    public String getThingid() {
        return thingid;
    }

    public void setThingid(String thingid) {
        this.thingid = thingid == null ? null : thingid.trim();
    }

    public String getNewsurl() {
        return newsurl;
    }

    public void setNewsurl(String newsurl) {
        this.newsurl = newsurl == null ? null : newsurl.trim();
    }

    public String getNewsplurl() {
        return newsplurl;
    }

    public void setNewsplurl(String newsplurl) {
        this.newsplurl = newsplurl == null ? null : newsplurl.trim();
    }

    public String getNewstitle() {
        return newstitle;
    }

    public void setNewstitle(String newstitle) {
        this.newstitle = newstitle == null ? null : newstitle.trim();
    }

    public String getNewsauthor() {
        return newsauthor;
    }

    public void setNewsauthor(String newsauthor) {
        this.newsauthor = newsauthor == null ? null : newsauthor.trim();
    }

    public String getNewssource() {
        return newssource;
    }

    public void setNewssource(String newssource) {
        this.newssource = newssource == null ? null : newssource.trim();
    }

    public String getNewscontent() {
        return newscontent;
    }

    public void setNewscontent(String newscontent) {
        this.newscontent = newscontent == null ? null : newscontent.trim();
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

    public String getPlpage() {
        return plpage;
    }

    public void setPlpage(String plpage) {
        this.plpage = plpage == null ? null : plpage.trim();
    }

    public String getCollectcount() {
        return collectcount;
    }

    public void setCollectcount(String collectcount) {
        this.collectcount = collectcount == null ? null : collectcount.trim();
    }

    public String getLastcollectdate() {
        return lastcollectdate;
    }

    public void setLastcollectdate(String lastcollectdate) {
        this.lastcollectdate = lastcollectdate == null ? null : lastcollectdate.trim();
    }

    public String getLastcollecttime() {
        return lastcollecttime;
    }

    public void setLastcollecttime(String lastcollecttime) {
        this.lastcollecttime = lastcollecttime == null ? null : lastcollecttime.trim();
    }
}