package pl.olart.pmsuite.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * User: grp
 * Date: 29.09.15
 * Time: 15:04
 */
public class FacesUtils {
    public static void addInfoMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    public static void addErrorMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
