package com.leansoftwarelabs.realm.service;

import com.leansoftwarelabs.realm.domain.Role;
import com.leansoftwarelabs.realm.domain.RolePermission;
import com.leansoftwarelabs.realm.domain.User;

import com.leansoftwarelabs.realm.service.UsersRolesPermissionsFacadeBean.UsernameNotAvailableException;

import java.util.List;

import javax.ejb.Local;

@Local
public interface UsersRolesPermissionsFacadeLocal {
    Object queryByRange(String jpqlStmt, List<Object[]> hints, int firstResult, int maxResults);

    void removeRole(Role role);

    void removeUser(User user);

    void removeRolePermission(RolePermission rolePermission);
    
    List<User> getAllUsers();
    
    List<Role> getAllRoles();
    
    void updateUser(User userParam) throws Exception;
    
    void createUser(User userParam) throws Exception;
    
    void saveRole(Role role);
    
    User findUserByUsername(String username);

}
