package pl.olart.pmsuite.beans;

import java.io.Serializable;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * User: grp
 * Date: 28.09.15
 * Time: 18:22
 */
@ManagedBean
@SessionScoped
public class GlobalBean implements Serializable {
    private Integer activeTab = 0;
    private String changeActiveTab;

    public Integer getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(Integer activeTab) {
        this.activeTab = activeTab;
    }


    public void changeActiveTab() {
        Map<String, String> map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String tab = map.get("i");

    }
}
