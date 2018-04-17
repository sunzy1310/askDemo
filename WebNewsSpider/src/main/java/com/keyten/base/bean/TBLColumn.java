package com.keyten.base.bean;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class TBLColumn {
    private String columnid;

    private String columnname;

    private String columnurl;

    private String columncharset;

    private String matchtype;

    private String bcontent;

    private String ismonitor;

    private String urlcfgstatus;

    private String elementcfgstatus;

    private String operator;

    private String comcode;

    private String makedate;

    private String maketime;

    private String webid;

    private String webname;

    private String weburl;

    private String checkstatus;

    private String checkdate;

    private String checktime;

    private String pubstatus;

    private String pubdate;

    private String pubtime;

    private String serverid;

    private String ipaddr;

    private String lastcollectdate;

    private String lastcollecttime;

    private BigDecimal collectinterval;

    private BigDecimal expirydate;

    private BigDecimal pubrate;

    private String webtype;
    
    private List<TBLUrlConfig> urlcfg;
    
    private List<TBLElementConfig> elecfg;
    
    public List<TBLUrlConfig> getUrlcfg() {
		return urlcfg;
	}

	public void setUrlcfg(List<TBLUrlConfig> urlcfg) {
		this.urlcfg = urlcfg;
	}

	public List<TBLElementConfig> getElecfg() {
		return elecfg;
	}

	public void setElecfg(List<TBLElementConfig> elecfg) {
		this.elecfg = elecfg;
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

    public String getColumncharset() {
        return columncharset;
    }

    public void setColumncharset(String columncharset) {
        this.columncharset = columncharset == null ? null : columncharset.trim();
    }

    public String getMatchtype() {
        return matchtype;
    }

    public void setMatchtype(String matchtype) {
        this.matchtype = matchtype == null ? null : matchtype.trim();
    }

    public String getBcontent() {
        return bcontent;
    }

    public void setBcontent(String bcontent) {
        this.bcontent = bcontent == null ? null : bcontent.trim();
    }

    public String getIsmonitor() {
        return ismonitor;
    }

    public void setIsmonitor(String ismonitor) {
        this.ismonitor = ismonitor == null ? null : ismonitor.trim();
    }

    public String getUrlcfgstatus() {
        return urlcfgstatus;
    }

    public void setUrlcfgstatus(String urlcfgstatus) {
        this.urlcfgstatus = urlcfgstatus == null ? null : urlcfgstatus.trim();
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

    public BigDecimal getCollectinterval() {
        return collectinterval;
    }

    public void setCollectinterval(BigDecimal collectinterval) {
        this.collectinterval = collectinterval;
    }

    public BigDecimal getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(BigDecimal expirydate) {
        this.expirydate = expirydate;
    }

    public BigDecimal getPubrate() {
        return pubrate;
    }

    public void setPubrate(BigDecimal pubrate) {
        this.pubrate = pubrate;
    }

    public String getWebtype() {
        return webtype;
    }

    public void setWebtype(String webtype) {
        this.webtype = webtype == null ? null : webtype.trim();
    }
}