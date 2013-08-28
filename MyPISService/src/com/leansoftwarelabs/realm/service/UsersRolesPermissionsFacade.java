package com.leansoftwarelabs.realm.service;

import com.leansoftwarelabs.realm.domain.Role;
import com.leansoftwarelabs.realm.domain.RolePermission;
import com.leansoftwarelabs.realm.domain.User;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface UsersRolesPermissionsFacade {
    Object queryByRange(String jpqlStmt, List<Object[]> hints, int firstResult, int maxResults);

    void removeRole(Role role);

    void removeUser(User user);

    void removeRolePermission(RolePermission rolePermission);
    
    List<User> getAllUsers();
    
    List<Role> getAllRoles();
}
