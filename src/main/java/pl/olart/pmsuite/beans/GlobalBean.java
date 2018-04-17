package pl.olart.pmsuite.beans;

import java.io.Serializable;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

/**
 * User: grp
 * Date: 28.09.15
 * Time: 18:22
 */
@ManagedBean
@SessionScoped
public class GlobalBean implements Serializable {
    private Integer activeTab = 0;

    public Integer getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(Integer activeTab) {
        this.activeTab = activeTab;
    }


    public String parseParameter() {
        Map<String, String> map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String param = map.get("param213");
        java.beans.SimpleBeanInfo a;
        return param;
    }


    public String asd2() {
        return "qw";
    }
}
