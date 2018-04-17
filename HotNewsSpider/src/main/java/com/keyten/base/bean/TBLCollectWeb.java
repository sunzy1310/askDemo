package com.keyten.base.bean;

public class TBLCollectWeb {
    private String columnid;

    private String targetid;

    private String columnname;

    private String webtype;

    private String columnurl;

    private String urlrule;

    private String urlhost;

    private String runstatus;

    private String makedate;

    private String maketime;

    public String getColumnid() {
        return columnid;
    }

    public void setColumnid(String columnid) {
        this.columnid = columnid == null ? null : columnid.trim();
    }

    public String getTargetid() {
        return targetid;
    }

    public void setTargetid(String targetid) {
        this.targetid = targetid == null ? null : targetid.trim();
    }

    public String getColumnname() {
        return columnname;
    }

    public void setColumnname(String columnname) {
        this.columnname = columnname == null ? null : columnname.trim();
    }

    public String getWebtype() {
        return webtype;
    }

    public void setWebtype(String webtype) {
        this.webtype = webtype == null ? null : webtype.trim();
    }

    public String getColumnurl() {
        return columnurl;
    }

    public void setColumnurl(String columnurl) {
        this.columnurl = columnurl == null ? null : columnurl.trim();
    }

    public String getUrlrule() {
        return urlrule;
    }

    public void setUrlrule(String urlrule) {
        this.urlrule = urlrule == null ? null : urlrule.trim();
    }

    public String getUrlhost() {
        return urlhost;
    }

    public void setUrlhost(String urlhost) {
        this.urlhost = urlhost == null ? null : urlhost.trim();
    }

    public String getRunstatus() {
        return runstatus;
    }

    public void setRunstatus(String runstatus) {
        this.runstatus = runstatus == null ? null : runstatus.trim();
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