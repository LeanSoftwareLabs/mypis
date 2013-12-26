package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.adf.query.AttributeDef;
import com.leansoftwarelabs.adf.query.ListOfValuesModelImpl;
import com.leansoftwarelabs.adf.query.QueryModelImpl;
import com.leansoftwarelabs.ext.ServiceProvider;
import com.leansoftwarelabs.mypis.domain.CashVoucher;
import com.leansoftwarelabs.mypis.domain.GLAccount;
import com.leansoftwarelabs.mypis.domain.GLEntry;
import com.leansoftwarelabs.mypis.domain.GLEntryLine;
import com.leansoftwarelabs.mypis.service.ApplicationConstraintViolationException;
import com.leansoftwarelabs.mypis.service.GLAccountFacadeBean;
import com.leansoftwarelabs.mypis.service.GLEntryFacadeBean;
import com.leansoftwarelabs.mypis.service.ServiceException;
import com.leansoftwarelabs.mypis.view.util.AttributeDefMapProviderUtil;
import com.leansoftwarelabs.mypis.view.util.FormUtils;
import com.leansoftwarelabs.trinidad.model.FilterableCollectionModel;
import com.leansoftwarelabs.trinidad.model.JpqlLazyDataModel;
import com.leansoftwarelabs.view.utils.ADFUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import javax.faces.event.ActionEvent;

import javax.faces.model.SelectItem;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.model.ListOfValuesModel;

import org.apache.myfaces.trinidad.model.RowKeySet;

public class CashVoucherForm {
    private CashVoucher cashVoucher;
    private ListOfValuesModel glAccountLOVModel;
    private RichTable richTable;
    private List<SelectItem> bankCashItems;

    @PostConstruct
    public void init() {
        ServiceProvider<GLEntryFacadeBean> serviceProvider = FormUtils.getService("glEntryFacade");
        Integer glEntryId = (Integer) ADFUtils.getPageFlowScope().get("glEntryId");
        GLEntry entry = null;
        if (glEntryId == -1) {
            entry = new GLEntry();
            entry.setTransType("PAY");
            entry.setTransDate(Calendar.getInstance().getTime());
            entry.setReportingDate(entry.getTransDate());
            for (int i = 0; i < 7; i++) {
                GLEntryLine entryLine = new GLEntryLine();
                entry.addGLEntryLine(entryLine);
            }
        } else {
            entry = serviceProvider.getService().find(glEntryId);

        }
        cashVoucher = new CashVoucher(entry);
    }


    public List<SelectItem> getBankCashItems() {
        if (bankCashItems == null) {
            bankCashItems = new ArrayList<SelectItem>();
            ServiceProvider<GLAccountFacadeBean> serviceProvider = FormUtils.getService("glAccountFacade");
            List<GLAccount> bankCashAccounts = serviceProvider.getService().findCashAndBankAccounts();
            for (GLAccount account : bankCashAccounts) {
                bankCashItems.add(new SelectItem(account, account.getCode() + ":" + account.getName()));
            }
        }
        return bankCashItems;
    }


    public CashVoucher getCashVoucher() {
        return cashVoucher;
    }

    public void edit() {
        FormUtils.editing(true);
    }

    public String cancel() {
        FormUtils.editing(false);
        if (cashVoucher.getId() == null) {
            return "done";
        } else {
            init();
        }
        return null;
    }


    public void save() {
        ServiceProvider<GLEntryFacadeBean> serviceProvider = FormUtils.getService("glEntryFacade");
        try {
            cashVoucher = serviceProvider.getService().saveCashVoucher(cashVoucher);
            FormUtils.editing(false);
        } catch (ApplicationConstraintViolationException ce) {
            FormUtils.addFacesErrorMessage(ce.getViolations());
        } catch (ServiceException se) {
            FormUtils.addFacesErrorMessage(se);
        }

    }


    public void addLine(ActionEvent actionEvent) {
        GLEntryLine lineDetail = new GLEntryLine();
        cashVoucher.addLineDetail(lineDetail);
    }

    public void removeLine(ActionEvent actionEvent) {
        cashVoucher.removeLineDetail(getSelectedIndex());
    }

    private int getSelectedIndex() {
        int result = -1;
        RowKeySet rowKeySet = getRichTable().getSelectedRowKeys();
        Iterator iterator = rowKeySet.iterator();
        while (iterator.hasNext()) {
            return (Integer) iterator.next();
        }
        return result;
    }


    public ListOfValuesModel getGlAccountLOVModel() {
        final ServiceProvider<GLAccountFacadeBean> serviceProvider = FormUtils.getService("glAccountFacade");
        if (glAccountLOVModel == null) {
            Map<String, AttributeDef> attributes = AttributeDefMapProviderUtil.getGLAccountAttributeDefMap();
            QueryModelImpl queryModel = new QueryModelImpl("GLAccountQuery", attributes, null, null);
            FilterableCollectionModel collectionModel = new JpqlLazyDataModel("GLAccount", 20) {
                protected List queryByRange(String jpqlStmt, int first, int pageSize) {
                    return serviceProvider.getService().queryByRange(jpqlStmt, null, first, pageSize);
                }
            };
            glAccountLOVModel = new ListOfValuesModelImpl(new String[] { "code", "name" }, queryModel, collectionModel);
        }
        return glAccountLOVModel;
    }

    public void setRichTable(RichTable richTable) {
        this.richTable = richTable;
    }

    public RichTable getRichTable() {
        return richTable;
    }

    public void postCashVoucher(ActionEvent actionEvent) {
        ServiceProvider<GLEntryFacadeBean> serviceProvider = FormUtils.getService("glEntryFacade");
        try {
            cashVoucher = serviceProvider.getService().postCashVoucher(cashVoucher);
            FormUtils.editing(false);
        } catch (ApplicationConstraintViolationException ce) {
            FormUtils.addFacesErrorMessage(ce.getViolations());
        } catch (ServiceException se) {
            FormUtils.addFacesErrorMessage(se);
        }
    }
}
