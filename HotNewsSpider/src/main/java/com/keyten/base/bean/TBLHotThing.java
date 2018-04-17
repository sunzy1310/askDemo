package com.keyten.base.bean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("hotThing")
@Scope("prototype")
public class TBLHotThing {
    private String thingid;

    private String thingword;

    private String makedate;

    private String maketime;

    public String getThingid() {
        return thingid;
    }

    public void setThingid(String thingid) {
        this.thingid = thingid == null ? null : thingid.trim();
    }

    public String getThingword() {
        return thingword;
    }

    public void setThingword(String thingword) {
        this.thingword = thingword == null ? null : thingword.trim();
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