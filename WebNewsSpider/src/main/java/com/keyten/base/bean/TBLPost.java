package com.keyten.base.bean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component()
@Scope("prototype")
public class TBLPost {
    private String articleid;

    private String columnid;

    private String columnname;

    private String columnurl;

    private String arttitle;

    private String arturl;

    private String artauthor;

    private String artsource;

    private String artcontent;

    private String artpubdate;

    private String artpubtime;

    private String createdate;

    private String createtime;

    private String syncstatus;

    private String webid;

    private String webname;

    private String webtype;

    private String weburl;

    public String getArticleid() {
        return articleid;
    }

    public void setArticleid(String articleid) {
        this.articleid = articleid == null ? null : articleid.trim();
    }

    public String getColumnid() {
        return columnid;
    }

    public void setColumnid(String columnid) {
        this.columnid = columnid == null ? null : columnid.trim();
    }

    public String getColumnname() {
        return columnname;
    }

    public void setColumnname(String columnname) {
        this.columnname = columnname == null ? null : columnname.trim();
    }

    public String getColumnurl() {
        return columnurl;
    }

    public void setColumnurl(String columnurl) {
        this.columnurl = columnurl == null ? null : columnurl.trim();
    }

    public String getArttitle() {
        return arttitle;
    }

    public void setArttitle(String arttitle) {
        this.arttitle = arttitle == null ? null : arttitle.trim();
    }

    public String getArturl() {
        return arturl;
    }

    public void setArturl(String arturl) {
        this.arturl = arturl == null ? null : arturl.trim();
    }

    public String getArtauthor() {
        return artauthor;
    }

    public void setArtauthor(String artauthor) {
        this.artauthor = artauthor == null ? null : artauthor.trim();
    }

    public String getArtsource() {
        return artsource;
    }

    public void setArtsource(String artsource) {
        this.artsource = artsource == null ? null : artsource.trim();
    }

    public String getArtcontent() {
        return artcontent;
    }

    public void setArtcontent(String artcontent) {
        this.artcontent = artcontent == null ? null : artcontent.trim();
    }

    public String getArtpubdate() {
        return artpubdate;
    }

    public void setArtpubdate(String artpubdate) {
        this.artpubdate = artpubdate == null ? null : artpubdate.trim();
    }

    public String getArtpubtime() {
        return artpubtime;
    }

    public void setArtpubtime(String artpubtime) {
        this.artpubtime = artpubtime == null ? null : artpubtime.trim();
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate == null ? null : createdate.trim();
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime == null ? null : createtime.trim();
    }

    public String getSyncstatus() {
        return syncstatus;
    }

    public void setSyncstatus(String syncstatus) {
        this.syncstatus = syncstatus == null ? null : syncstatus.trim();
    }

    public String getWebid() {
        return webid;
    }

    public void setWebid(String webid) {
        this.webid = webid == null ? null : webid.trim();
    }

    public String getWebname() {
        return webname;
    }

    public void setWebname(String webname) {
        this.webname = webname == null ? null : webname.trim();
    }

    public String getWebtype() {
        return webtype;
    }

    public void setWebtype(String webtype) {
        this.webtype = webtype == null ? null : webtype.trim();
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl == null ? null : weburl.trim();
    }
}