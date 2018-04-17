package com.keyten.base.bean;

public class TBLUrlConfig {
    private String ruleid;

    private String columnid;

    private String ruletype;

    private String matchrule;

    private String matchrange;

    public String getRuleid() {
        return ruleid;
    }

    public void setRuleid(String ruleid) {
        this.ruleid = ruleid == null ? null : ruleid.trim();
    }

    public String getColumnid() {
        return columnid;
    }

    public void setColumnid(String columnid) {
        this.columnid = columnid == null ? null : columnid.trim();
    }

    public String getRuletype() {
        return ruletype;
    }

    public void setRuletype(String ruletype) {
        this.ruletype = ruletype == null ? null : ruletype.trim();
    }

    public String getMatchrule() {
        return matchrule;
    }

    public void setMatchrule(String matchrule) {
        this.matchrule = matchrule == null ? null : matchrule.trim();
    }

    public String getMatchrange() {
        return matchrange;
    }

    public void setMatchrange(String matchrange) {
        this.matchrange = matchrange == null ? null : matchrange.trim();
    }
}