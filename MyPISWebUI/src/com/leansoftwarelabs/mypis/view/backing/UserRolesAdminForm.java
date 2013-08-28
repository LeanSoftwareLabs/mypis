package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.adf.query.AttributeDef;
import com.leansoftwarelabs.adf.query.FilterableQueryDescriptorImpl;
import com.leansoftwarelabs.adf.query.SavedSearchDef;
import com.leansoftwarelabs.adf.query.SearchFieldDef;
import com.leansoftwarelabs.ext.adf.EventHandler;
import com.leansoftwarelabs.realm.domain.Role;
import com.leansoftwarelabs.realm.domain.User;
import com.leansoftwarelabs.realm.service.UsersRolesPermissionsFacadeLocal;
import com.leansoftwarelabs.trinidad.model.FilterableCollectionModel;
import com.leansoftwarelabs.trinidad.model.InMemoryFilterableCollectionModel;

import com.leansoftwarelabs.view.utils.ADFUtils;

import com.leansoftwarelabs.view.utils.JSFUtils;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import javax.naming.Context;
import javax.naming.InitialContext;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.layout.RichPanelFormLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.QueryEvent;
import oracle.adf.view.rich.model.FilterableQueryDescriptor;

import org.apache.commons.collections.functors.FalsePredicate;
import org.apache.myfaces.trinidad.event.SelectionEvent;

public class UserRolesAdminForm {
    private UsersRolesPermissionsFacadeLocal service;
    private FilterableCollectionModel userDataModel;
    private FilterableQueryDescriptor descriptor;
    private RichTable userTable;
    private User selectedUser;
    private String confirmPassword;

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
    private RichPanelGroupLayout userInfoPanelGroup;
    private List<SelectItem> roleItems;

    public List<SelectItem> getRoleItems() {
        if(roleItems == null){
            initRoleItems();
        }
        return roleItems;
    }

    private void initRoleItems() {
        List<Role> roleList = getService().getAllRoles();
        roleItems = new ArrayList<SelectItem>();
        for(Role role: roleList){
            roleItems.add(new SelectItem(role, role.getRoleName()));
        }
    }

    public void setUserDataModel(FilterableCollectionModel userDataModel) {
        this.userDataModel = userDataModel;
    }

    public FilterableCollectionModel getUserDataModel() {
        if (userDataModel == null) {
            List userList = getService().getAllUsers();
            userDataModel = new InMemoryFilterableCollectionModel(userList);
        }
        return userDataModel;
    }

    public UserRolesAdminForm() {
        super();
    }

    public void init() {

    }
    
    public String prepare(){
        return "continue";
    }

    public User getSelectedUser() {
        return selectedUser;
    }
    
    public void editUser(){
        getUserTable().setRowSelection(RichTable.ROW_SELECTION_NONE);
        getUserTable().setFilterVisible(false);
        FormUtils.editing(true);
    }
    
    public void saveUser(){
        User user = getSelectedUser();
        String password = user.getPassword();
        if(password != null && !password.equals(confirmPassword)){
            JSFUtils.addFacesErrorMessage("Passwords do not match");
            return;
        }
        getService().createOrUpdateUser(getSelectedUser());
        getUserTable().setRowSelection(RichTable.ROW_SELECTION_SINGLE);
        getUserTable().setFilterVisible(true);
        FormUtils.editing(false);
    }
    
    public void cancel(){
        getUserTable().setRowSelection(RichTable.ROW_SELECTION_SINGLE);
        getUserTable().setFilterVisible(true);
        FormUtils.editing(false);
    }
    

    public UsersRolesPermissionsFacadeLocal getService() {
        if (service == null) {
            try {
                final Context context = new InitialContext();
                service =
                    (UsersRolesPermissionsFacadeLocal) context.lookup("java:comp/env/ejb/local/UsersRolesPermissionsFacade");
            } catch (Exception ex) {
                //TODO : bubble up exception or put in log file.
                ex.printStackTrace();
            }
        }
        return service;
    }

    public void handleFilter(QueryEvent event) {
        FilterableQueryDescriptorImpl descriptor = (FilterableQueryDescriptorImpl) event.getDescriptor();
        RichTable table = (RichTable) event.getComponent();
        FilterableCollectionModel model = (FilterableCollectionModel) table.getValue();
        model.setFilterCriteria(descriptor.getFilterConjunctionCriterion_());
        table.getSelectedRowKeys().clear();
        this.selectedUser = null;
        AdfFacesContext.getCurrentInstance().addPartialTarget(getUserInfoPanelGroup());
    }

    public FilterableQueryDescriptor getDescriptor() {
        if (descriptor == null) {
            Map<String, AttributeDef> attributes = new HashMap<String, AttributeDef>();
            attributes.put("username", new AttributeDef("username", String.class, null, AttributeDef.INPUT_TEXT));
            attributes.put("fullName", new AttributeDef("fullName", String.class, null, AttributeDef.INPUT_TEXT));
            attributes.put("email", new AttributeDef("email", BigDecimal.class, null, AttributeDef.INPUT_TEXT));
            SavedSearchDef ssd = new SavedSearchDef();
            ssd.setName("Table Filter 101");
            ssd.setReadOnly(true);
            ssd.setDefaultSearch(true);
            for (Map.Entry<String, AttributeDef> entry : attributes.entrySet()) {
                AttributeDef attr = entry.getValue();
                SearchFieldDef sfd = new SearchFieldDef();
                sfd.setAttrName(attr.getName());
                sfd.setOperator(attr.getDefaultOperator());
                ssd.addSearchFieldDef(sfd);
            }
            descriptor = new FilterableQueryDescriptorImpl(ssd, attributes);
        }
        return descriptor;
    }


    public void setUserTable(RichTable userTable) {
        this.userTable = userTable;
    }

    public RichTable getUserTable() {
        return userTable;
    }

    public void handleUserRowSelection(SelectionEvent selectionEvent) {
        selectedUser = (User) getUserTable().getSelectedRowData();
        AdfFacesContext.getCurrentInstance().addPartialTarget(getUserInfoPanelGroup());
    }


    public void setUserInfoPanelGroup(RichPanelGroupLayout userInfoPanelGroup) {
        this.userInfoPanelGroup = userInfoPanelGroup;
    }

    public RichPanelGroupLayout getUserInfoPanelGroup() {
        return userInfoPanelGroup;
    }

    public String refreshRoleItems() {
        initRoleItems();
        return null;
    }
}
