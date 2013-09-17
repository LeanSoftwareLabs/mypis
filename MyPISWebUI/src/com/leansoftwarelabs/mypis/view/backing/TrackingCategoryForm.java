package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.mypis.domain.TrackingCategory;
import com.leansoftwarelabs.mypis.domain.TrackingValue;
import com.leansoftwarelabs.mypis.service.ApplicationConstraintViolationException;
import com.leansoftwarelabs.mypis.service.ServiceException;
import com.leansoftwarelabs.mypis.service.TrackingCategoryFacadeBean;

import com.leansoftwarelabs.realm.domain.User;

import java.util.List;

import javax.annotation.PostConstruct;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.naming.Context;
import javax.naming.InitialContext;

import oracle.adf.view.rich.component.rich.data.RichTable;

import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.adf.view.rich.event.DialogEvent;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.RowKeySetImpl;

public class TrackingCategoryForm {
    private TrackingCategoryFacadeBean service;
    private List<TrackingCategory> trackingCategoryList;
    private RichTable trackingCategoryTable;
    private TrackingCategory selectedTrackingCategory;
    private RichPanelGroupLayout trackingCategoryPanelGroup;
    private RowKeySet rowKeySet;
    private RichTable trackingValueTable;

    public RowKeySet getRowKeySet() {
        return rowKeySet;
    }

    @PostConstruct
    public void init() {
        trackingCategoryList = getService().findAll();
        if (trackingCategoryList != null && !trackingCategoryList.isEmpty()) {
            rowKeySet = new RowKeySetImpl();
            rowKeySet.add(0); //first row
            selectedTrackingCategory = trackingCategoryList.get(0);
        }
    }


    public void setRowKeySet(RowKeySet rowKeySet) {
        this.rowKeySet = rowKeySet;
    }


    public TrackingCategory getSelectedTrackingCategory() {
        return selectedTrackingCategory;
    }


    public TrackingCategoryFacadeBean getService() {
        if (service == null) {
            try {
                final Context context = new InitialContext();
                service = (TrackingCategoryFacadeBean) context.lookup("java:comp/env/ejb/local/TrackingCategoryFacade");
            } catch (Exception ex) {
                //TODO : bubble up exception or put in log file.
                ex.printStackTrace();
            }
        }
        return service;
    }

    public List<TrackingCategory> getTrackingCategoryList() {
        return trackingCategoryList;
    }

    public void setTrackingCategoryTable(RichTable trackingCategoryTable) {
        this.trackingCategoryTable = trackingCategoryTable;
    }

    public RichTable getTrackingCategoryTable() {
        return trackingCategoryTable;
    }

    public void handleTrackingCategorySelection(SelectionEvent selectionEvent) {
        selectedTrackingCategory = (TrackingCategory) getTrackingCategoryTable().getSelectedRowData();
        AdfFacesContext.getCurrentInstance().addPartialTarget(getTrackingCategoryPanelGroup());
    }

    public void setTrackingCategoryPanelGroup(RichPanelGroupLayout trackingCategoryPanelGroup) {
        this.trackingCategoryPanelGroup = trackingCategoryPanelGroup;
    }

    public RichPanelGroupLayout getTrackingCategoryPanelGroup() {
        return trackingCategoryPanelGroup;
    }

    public void edit(ActionEvent actionEvent) {
        getTrackingCategoryTable().setRowSelection(RichTable.ROW_SELECTION_NONE);
        FormUtils.editing(true);
    }

    public void cancel(ActionEvent actionEvent) {
        getTrackingCategoryTable().setRowSelection(RichTable.ROW_SELECTION_SINGLE);
        FormUtils.editing(false);
    }

    public void apply(ActionEvent actionEvent) {
        try {
            getService().mergeEntity(selectedTrackingCategory);
            trackingCategoryList = getService().findAll();
            getTrackingCategoryTable().setRowSelection(RichTable.ROW_SELECTION_SINGLE);
            FormUtils.editing(false);
        } catch (ApplicationConstraintViolationException ce) {
            FormUtils.addFacesErrorMessage(ce.getViolations());
        } catch (ServiceException se) {
            FormUtils.addFacesErrorMessage(se);
        }
    }

    public void addTrackingCategory(ActionEvent actionEvent) {
        selectedTrackingCategory = new TrackingCategory();
        edit(actionEvent);
    }

    public void addTrackingValue(ActionEvent actionEvent) {
        TrackingValue value = new TrackingValue();
        selectedTrackingCategory.addTrackingValue(value);
    }

    public void deleteTrackingValue(ActionEvent actionEvent) {
        selectedTrackingCategory.removeTrackingValue((TrackingValue) getTrackingValueTable().getSelectedRowData());
    }

    public void setTrackingValueTable(RichTable trackingValueTable) {
        this.trackingValueTable = trackingValueTable;
    }

    public RichTable getTrackingValueTable() {
        return trackingValueTable;
    }

    public void handleDeleteCategoryConfirmationDialogReturn(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome() == DialogEvent.Outcome.ok) {
            getService().remove(selectedTrackingCategory);
            trackingCategoryList = getService().findAll();
            AdfFacesContext.getCurrentInstance().addPartialTarget(getTrackingCategoryTable());
        }
    }

}
