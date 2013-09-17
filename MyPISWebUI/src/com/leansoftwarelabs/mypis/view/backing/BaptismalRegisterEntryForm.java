package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.ext.adf.EventHandler;
import com.leansoftwarelabs.ext.shiro.SecurityContext;
import com.leansoftwarelabs.mypis.domain.BaptismalRegister;
import com.leansoftwarelabs.mypis.domain.Sponsor;
import com.leansoftwarelabs.mypis.service.BaptismalRegisterFacadeBean;
import com.leansoftwarelabs.view.utils.ADFUtils;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import javax.ejb.EJBTransactionRolledbackException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.http.HttpServletRequest;

import javax.validation.ConstraintViolationException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import oracle.adf.view.rich.component.rich.data.RichTable;

import org.apache.myfaces.trinidad.model.RowKeySet;

public class BaptismalRegisterEntryForm {
    private BaptismalRegister baptismalRegister;
    private BaptismalRegisterFacadeBean service;
    private RichTable sponsorTable;

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
                baptismalRegister = getService().find(registerId);
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
        try {
            this.baptismalRegister = getService().mergeEntity(this.baptismalRegister);
        } catch (Exception e) {
            Throwable t = e.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if (t instanceof ConstraintViolationException) {
                System.out.println("Something went wrong");

            }
        }

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
        ADFUtils.openLink(request.getContextPath() + "/showpdf?filename=" + getBaptismalRegister().getLastName() +
                          "&printId=" + printId);


    }

    public String addSponsor() {
        // Add event code here...
        return null;
    }

    public void addSponsor(ActionEvent actionEvent) {
        baptismalRegister.addSponsor(new Sponsor());
    }

    public void insertSponsor(ActionEvent actionEvent) {
        int index = getSelectedSponsorIndex();
        Sponsor sponsor = new Sponsor();
        if (index != -1) {
            baptismalRegister.getSponsors().add(index, sponsor);
        } else {
            baptismalRegister.addSponsor(sponsor);
        }
    }

    private int getSelectedSponsorIndex() {
        int result = -1;
        RowKeySet rowKeySet = getSponsorTable().getSelectedRowKeys();
        Iterator iterator = rowKeySet.iterator();
        while (iterator.hasNext()) {
            result = (Integer) iterator.next();
        }
        return result;
    }

    public void removeSponsor(ActionEvent actionEvent) {
        int index = getSelectedSponsorIndex();
        if (index != -1) {
            baptismalRegister.getSponsors().remove(index);
        }
    }

    public void setSponsorTable(RichTable sponsorTable) {
        this.sponsorTable = sponsorTable;
    }

    public RichTable getSponsorTable() {
        return sponsorTable;
    }
}
