package com.keyten.base.bean;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class TBLNewspaperWebConfig {
    private String webruleid;

    private String webrule;

    private String columnrule;

    private String articlerule;

    private String columncharset;

    private String webid;

    private String webname;

    private String weburl;

    private String ismonitor;

    private String elementcfgstatus;

    private String operator;

    private String comcode;

    private String makedate;

    private String maketime;

    private String checkstatus;

    private String checkdate;

    private String checktime;

    private String pubstatus;

    private String pubdate;

    private String pubtime;

    private String serverid;

    private BigDecimal collectinterval;

    private String ipaddr;

    private String lastcollectdate;

    private String lastcollecttime;

    private String webruletype;

    private String lastnormalcollectdate;

    private String lastnormalcollecttime;

    private String proxystatus;

    private BigDecimal pubrate;

    private BigDecimal expirydate;
    
    private List<TBLNewspaperElementConfig> elementCfgs;
    

    public List<TBLNewspaperElementConfig> getElementCfgs() {
		return elementCfgs;
	}

	public void setElementCfgs(List<TBLNewspaperElementConfig> elementCfgs) {
		this.elementCfgs = elementCfgs;
	}

	public String getWebruleid() {
        return webruleid;
    }

    public void setWebruleid(String webruleid) {
        this.webruleid = webruleid == null ? null : webruleid.trim();
    }

    public String getWebrule() {
        return webrule;
    }

    public void setWebrule(String webrule) {
        this.webrule = webrule == null ? null : webrule.trim();
    }

    public String getColumnrule() {
        return columnrule;
    }

    public void setColumnrule(String columnrule) {
        this.columnrule = columnrule == null ? null : columnrule.trim();
    }

    public String getArticlerule() {
        return articlerule;
    }

    public void setArticlerule(String articlerule) {
        this.articlerule = articlerule == null ? null : articlerule.trim();
    }

    public String getColumncharset() {
        return columncharset;
    }

    public void setColumncharset(String columncharset) {
        this.columncharset = columncharset == null ? null : columncharset.trim();
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

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl == null ? null : weburl.trim();
    }

    public String getIsmonitor() {
        return ismonitor;
    }

    public void setIsmonitor(String ismonitor) {
        this.ismonitor = ismonitor == null ? null : ismonitor.trim();
    }

    public String getElementcfgstatus() {
        return elementcfgstatus;
    }

    public void setElementcfgstatus(String elementcfgstatus) {
        this.elementcfgstatus = elementcfgstatus == null ? null : elementcfgstatus.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getComcode() {
        return comcode;
    }

    public void setComcode(String comcode) {
        this.comcode = comcode == null ? null : comcode.trim();
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

    public String getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(String checkstatus) {
        this.checkstatus = checkstatus == null ? null : checkstatus.trim();
    }

    public String getCheckdate() {
        return checkdate;
    }

    public void setCheckdate(String checkdate) {
        this.checkdate = checkdate == null ? null : checkdate.trim();
    }

    public String getChecktime() {
        return checktime;
    }

    public void setChecktime(String checktime) {
        this.checktime = checktime == null ? null : checktime.trim();
    }

    public String getPubstatus() {
        return pubstatus;
    }

    public void setPubstatus(String pubstatus) {
        this.pubstatus = pubstatus == null ? null : pubstatus.trim();
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

    public String getServerid() {
        return serverid;
    }

    public void setServerid(String serverid) {
        this.serverid = serverid == null ? null : serverid.trim();
    }

    public BigDecimal getCollectinterval() {
        return collectinterval;
    }

    public void setCollectinterval(BigDecimal collectinterval) {
        this.collectinterval = collectinterval;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr == null ? null : ipaddr.trim();
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

    public String getWebruletype() {
        return webruletype;
    }

    public void setWebruletype(String webruletype) {
        this.webruletype = webruletype == null ? null : webruletype.trim();
    }

    public String getLastnormalcollectdate() {
        return lastnormalcollectdate;
    }

    public void setLastnormalcollectdate(String lastnormalcollectdate) {
        this.lastnormalcollectdate = lastnormalcollectdate == null ? null : lastnormalcollectdate.trim();
    }

    public String getLastnormalcollecttime() {
        return lastnormalcollecttime;
    }

    public void setLastnormalcollecttime(String lastnormalcollecttime) {
        this.lastnormalcollecttime = lastnormalcollecttime == null ? null : lastnormalcollecttime.trim();
    }

    public String getProxystatus() {
        return proxystatus;
    }

    public void setProxystatus(String proxystatus) {
        this.proxystatus = proxystatus == null ? null : proxystatus.trim();
    }

    public BigDecimal getPubrate() {
        return pubrate;
    }

    public void setPubrate(BigDecimal pubrate) {
        this.pubrate = pubrate;
    }

    public BigDecimal getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(BigDecimal expirydate) {
        this.expirydate = expirydate;
    }
}