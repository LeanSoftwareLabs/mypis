package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.adf.query.AttributeDef;
import com.leansoftwarelabs.adf.query.QueryModelImpl;
import com.leansoftwarelabs.realm.domain.Resource;
import com.leansoftwarelabs.realm.domain.Role;
import com.leansoftwarelabs.realm.domain.RolePermission;
import com.leansoftwarelabs.realm.domain.User;
import com.leansoftwarelabs.realm.service.ResourceFacadeBean;
import com.leansoftwarelabs.realm.service.UsersRolesPermissionsFacadeBean;

import com.leansoftwarelabs.trinidad.model.JpqlLazyDataModel;
import com.leansoftwarelabs.trinidad.model.LazyDataModel;
import com.leansoftwarelabs.view.utils.ADFUtils;
import com.leansoftwarelabs.view.utils.JSFUtils;

import java.awt.Dialog;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import java.util.Map;

import javax.faces.event.ActionEvent;

import javax.naming.Context;
import javax.naming.InitialContext;

import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.layout.RichPanelSplitter;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.adf.view.rich.event.QueryEvent;
import oracle.adf.view.rich.model.QueryModel;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.RowKeySetImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class RolesPermissionsForm {
    private UsersRolesPermissionsFacadeBean service;
    private List<Role> roleList;
    private Role role;
    private Role selectedRole;
    private QueryModel queryModel;
    private LazyDataModel lazyDataModel;
    private ResourceFacadeBean resourceFacade;

    private RichTable roleTable;
    private RichPopup rolePopup;
    private RichPanelSplitter rolePermissionPanelSplitter;
    private RichTable resourceTable;

    public Role getRole() {
        return role;
    }

    public Role getSelectedRole() {
        if (selectedRole == null) {
            RichTable table = getRoleTable();
            if (table != null && table.getEstimatedRowCount() > 0) {
                selectedRole = (Role) table.getSelectedRowData();
                if (selectedRole == null) {
                    RowKeySet rowKeySet = new RowKeySetImpl();
                    rowKeySet.add(0);
                    table.setSelectedRowKeys(rowKeySet);
                    selectedRole = (Role) table.getSelectedRowData();
                }
            }
        }
        return selectedRole;
    }


    public List<Role> getRoleList() {
        if (roleList == null) {
            roleList = getService().getAllRoles();
        }
        return roleList;
    }

    public void editRole(ActionEvent ev) {
        ADFUtils.showDialog(getRolePopup());
    }

    public void createRole() {
        this.role = new Role();
        ADFUtils.showDialog(getRolePopup());
    }
    
    public void editPermissions(ActionEvent ev){
        getRoleTable().setRowSelection(RichTable.ROW_SELECTION_NONE);
        FormUtils.editing(true);
    }
    
    public void cancel(ActionEvent ev){
        roleList = getService().getAllRoles();
        selectedRole = null;//reset
        getRoleTable().setRowSelection(RichTable.ROW_SELECTION_SINGLE);
        FormUtils.editing(false);
    }
    
    public void savePermissions(ActionEvent ev){
        getService().saveRole(getSelectedRole());
        getRoleTable().setRowSelection(RichTable.ROW_SELECTION_SINGLE);
        FormUtils.editing(false);
    }


    public UsersRolesPermissionsFacadeBean getService() {
        if (service == null) {
            try {
                final Context context = new InitialContext();
                service =
                    (UsersRolesPermissionsFacadeBean) context.lookup("java:comp/env/ejb/local/UsersRolesPermissionsFacade");
            } catch (Exception ex) {
                //TODO : bubble up exception or put in log file.
                ex.printStackTrace();
            }
        }
        return service;
    }

    public void setRoleTable(RichTable roleTable) {
        this.roleTable = roleTable;
    }

    public RichTable getRoleTable() {
        return roleTable;
    }

    public void handleRoleDialogReturn(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome() == DialogEvent.Outcome.ok) {
            if (role.getTenant() == null) {
                User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
                role.setTenant(user.getTenant().getTenantId());
            }
            getService().saveRole(role);
            roleList = getService().getAllRoles();
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(getRoleTable());
    }

    public void setRolePopup(RichPopup rolePopup) {
        this.rolePopup = rolePopup;
    }

    public RichPopup getRolePopup() {
        return rolePopup;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String deleteRole() {
        if (getRoleTable().getEstimatedRowCount() > 0) {
            Role selected = (Role) getRoleTable().getSelectedRowData();
            if (selected != null) {
                getService().removeRole(selected);
            }
        }
        return null;
    }

    public void setRolePermissionPanelSplitter(RichPanelSplitter rolePermissionPanelSplitter) {
        this.rolePermissionPanelSplitter = rolePermissionPanelSplitter;
    }

    public RichPanelSplitter getRolePermissionPanelSplitter() {
        return rolePermissionPanelSplitter;
    }

    public void handleRoleSelection(SelectionEvent selectionEvent) {
        RichTable table = (RichTable) selectionEvent.getComponent();
        selectedRole = (Role) table.getSelectedRowData();
        AdfFacesContext.getCurrentInstance().addPartialTarget(getRolePermissionPanelSplitter());
    }

    public QueryModel getQueryModel() {
        if (queryModel == null) {
            Map<String, AttributeDef> attributes = new LinkedHashMap<String, AttributeDef>();
            attributes.put("name", new AttributeDef("name", String.class, null, AttributeDef.INPUT_TEXT));
            attributes.put("description", new AttributeDef("description", String.class, null, AttributeDef.INPUT_TEXT));
            attributes.put("value", new AttributeDef("value", String.class, null, AttributeDef.INPUT_TEXT));
            queryModel = new QueryModelImpl("resourceQuery", attributes, null, null);
        }
        return queryModel;
    }
    
    public void tableFilter(QueryEvent event) {
        this.lazyDataModel.setFilterCriteria(event.getDescriptor().getConjunctionCriterion());
    }

    public LazyDataModel getLazyDataModel() {
        if (lazyDataModel == null) {
            lazyDataModel = new JpqlLazyDataModel("Resource", 30) {
                protected List queryByRange(String jpqlStmt, int first, int pageSize) {
                    return (List) getResourceFacade().queryByRange(jpqlStmt, null, first, pageSize);
                }
            };
        }
        return lazyDataModel;
    }

    public ResourceFacadeBean getResourceFacade() {
        if (resourceFacade == null) {
            try {
                final Context context = new InitialContext();
                resourceFacade = (ResourceFacadeBean) context.lookup("java:comp/env/ejb/local/ResourceFacade");
            } catch (Exception ex) {
                //TODO : bubble up exception or put in log file.
                ex.printStackTrace();
            }
        }
        return resourceFacade;
    }

    public void handleResourceDialogReturn(DialogEvent dialogEvent) {
        if(dialogEvent.getOutcome() == DialogEvent.Outcome.ok){
            RichTable table = getResourceTable();
            RowKeySet rowKeySet = table.getSelectedRowKeys();
            Object origRowKey = table.getRowKey();
            Iterator iterator = rowKeySet.iterator();
            Role role = getSelectedRole();
            while(iterator.hasNext()){
                Object key = iterator.next();
                table.setRowKey(key);
                Resource resource = (Resource) table.getSelectedRowData();
                RolePermission permission= new RolePermission();
                permission.setResource(resource);
                role.addRolePermission(permission);
            }
            table.setRowKey(origRowKey);
            AdfFacesContext.getCurrentInstance().addPartialTarget(getRolePermissionPanelSplitter());
        }
        
    }

    public void setResourceTable(RichTable resourceTable) {
        this.resourceTable = resourceTable;
    }

    public RichTable getResourceTable() {
        return resourceTable;
    }
}
