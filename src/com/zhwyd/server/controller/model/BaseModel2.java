package com.zhwyd.server.controller.model;
import com.zhwyd.server.web.page.Pager;
public class BaseModel2 {
    private Pager pager;

    public Pager getPager() {
        if (pager == null) {
            pager = new Pager();
        }
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }
}
