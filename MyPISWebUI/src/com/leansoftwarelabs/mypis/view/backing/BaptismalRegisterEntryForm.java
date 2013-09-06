package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.ext.adf.EventHandler;
import com.leansoftwarelabs.ext.shiro.SecurityContext;
import com.leansoftwarelabs.mypis.domain.BaptismalRegister;
import com.leansoftwarelabs.mypis.service.BaptismalRegisterFacadeBean;
import com.leansoftwarelabs.view.utils.ADFUtils;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class BaptismalRegisterEntryForm {
    private BaptismalRegister baptismalRegister;
    private BaptismalRegisterFacadeBean service;

    @PostConstruct
    public void init() {

    }

    public void setBaptismalRegister(BaptismalRegister baptismalRegister) {
        this.baptismalRegister = baptismalRegister;
    }

    public BaptismalRegister getBaptismalRegister() {
        if (baptismalRegister == null) {
            Integer registerId = (Integer) ADFUtils.getPageFlowScope().get("registerId");
            if (registerId == -1) {
                baptismalRegister = new BaptismalRegister();
            } else {
                baptismalRegister = getService().findBaptismalRegisterById(registerId);
            }
        }
        return baptismalRegister;
    }

    public BaptismalRegisterFacadeBean getService() {
        if (service == null) {
            try {
                final Context context = new InitialContext();
                service =
                    (BaptismalRegisterFacadeBean) context.lookup("java:comp/env/ejb/local/BaptismalRegisterFacade");
            } catch (Exception ex) {
                //TODO : bubble up exception or put in log file.
                ex.printStackTrace();
            }
        }
        return service;
    }

    public void edit() {
        editing(true);
    }

    public String cancel() {
        editing(false);
        if (baptismalRegister.getRegisterId() == null) {
            return "done";
        } else {
            baptismalRegister = null; // reset to focer requery upon call to get
        }
        return null;
    }

    private void editing(boolean editMode) {
        ADFUtils.getPageFlowScope().put("editMode", editMode);
        EventHandler eventHandler = (EventHandler) ADFUtils.getPageFlowScope().get("eventHandler");
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("isDirty", editMode);
        eventHandler.handleEvent("dirtyEvent", payload);
    }

    public void save() {
        this.baptismalRegister = getService().mergeEntity(this.baptismalRegister);
        editing(false);
    }

    public void printToPDF(ActionEvent actionEvent) {
        List data = new ArrayList();
        data.add(getBaptismalRegister());
        Map parameters = new HashMap<String, String>();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        InputStream stream = externalContext.getResourceAsStream("/reports/certificates/BaptismalCertificate.jasper");
        JasperPrint print = null;
        try {
            JRDataSource datasource = new JRBeanCollectionDataSource(data, true);
            print = JasperFillManager.fillReport(stream, parameters, datasource);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        String printId = "JasperPrint_" + UUID.randomUUID().toString();
        request.getSession().setAttribute(printId, print);
        ADFUtils.openLink(request.getContextPath()+"/showpdf?filename=" + getBaptismalRegister().getLastName()+"&printId="+printId);


    }
}
