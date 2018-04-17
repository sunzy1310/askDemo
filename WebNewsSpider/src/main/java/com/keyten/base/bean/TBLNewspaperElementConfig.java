package com.keyten.base.bean;

import java.math.BigDecimal;

public class TBLNewspaperElementConfig {
    private String ruleid;

    private String webruleid;

    private String elementtype;

    private String ruletype;

    private String matchrule;

    private BigDecimal matchorder;

    private String childlabel;

    public String getRuleid() {
        return ruleid;
    }

    public void setRuleid(String ruleid) {
        this.ruleid = ruleid == null ? null : ruleid.trim();
    }

    public String getWebruleid() {
        return webruleid;
    }

    public void setWebruleid(String webruleid) {
        this.webruleid = webruleid == null ? null : webruleid.trim();
    }

    public String getElementtype() {
        return elementtype;
    }

    public void setElementtype(String elementtype) {
        this.elementtype = elementtype == null ? null : elementtype.trim();
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

    public BigDecimal getMatchorder() {
        return matchorder;
    }

    public void setMatchorder(BigDecimal matchorder) {
        this.matchorder = matchorder;
    }

    public String getChildlabel() {
        return childlabel;
    }

    public void setChildlabel(String childlabel) {
        this.childlabel = childlabel == null ? null : childlabel.trim();
    }
}