package com.leansoftwarelabs.realm.service;

import com.leansoftwarelabs.realm.domain.Role;
import com.leansoftwarelabs.realm.domain.RolePK;

import com.leansoftwarelabs.realm.domain.RolePermission;
import com.leansoftwarelabs.realm.domain.RolePermissionPK;
import com.leansoftwarelabs.realm.domain.User;

import java.security.Principal;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.ByteSource;

@Stateless(name = "UsersRolesPermissionsFacade", mappedName = "MyPIS-MyPISService-UsersRolesPermissionsFacade")
public class UsersRolesPermissionsFacadeBean{
    @Resource
    SessionContext sessionContext;
    @Inject
    Principal principal;
    @PersistenceContext(unitName = "JDBCRealm")
    private EntityManager em;

    public UsersRolesPermissionsFacadeBean() {
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object queryByRange(String jpqlStmt, List<Object[]> hints, int firstResult, int maxResults) {
        if (jpqlStmt == null) {
            return Collections.emptyList();
        }
        User currentUser = em.find(User.class, principal.getName());
        if (currentUser == null) {
            return Collections.emptyList();
        }
        Integer tenantId = currentUser.getTenant().getTenantId();
        StringBuffer buffer = new StringBuffer(jpqlStmt);
        if (jpqlStmt.toUpperCase().contains("WHERE")) {
            buffer.append(" AND");
        } else {
            buffer.append(" WHERE");
        }
        buffer.append(" o.tenantId = :tenantId");
        Query query = em.createQuery(buffer.toString());
        query.setParameter("tenantId", tenantId);
        if (firstResult > 0) {
            query = query.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            query = query.setMaxResults(maxResults);
        }
        return query.getResultList();
    }
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public User findUserByUsername(String username){
        User user = em.find(User.class, username);
        user.setPassword(null);
        user.setPasswordSalt(null);
        return user;
    }

    public <T> T persistEntity(T entity) {
        em.persist(entity);
        return entity;
    }

    public <T> T mergeEntity(T entity) {
        return em.merge(entity);
    }

    public void removeRole(Role role) {
        role = em.find(Role.class, new RolePK(role.getRoleName(), role.getTenant()));
        em.remove(role);
    }

    public void removeUser(User user) {
        user = em.find(User.class, user.getUsername());
        em.remove(user);
    }

    public void removeRolePermission(RolePermission rolePermission) {
        rolePermission =
            em.find(RolePermission.class,
                    new RolePermissionPK(rolePermission.getResource().getResourceId(), rolePermission.getRole().getRoleName(),
                                         rolePermission.getRole().getTenant()));
        em.remove(rolePermission);
    }
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<User> getAllUsers() {
        User currentUser = em.find(User.class, principal.getName());
        if (currentUser == null) {
            return Collections.emptyList();
        }
        Query query = em.createNamedQuery("User.findAll");
        query.setParameter("tenant", currentUser.getTenant());
        List<User> resultList= query.getResultList();
        //not in transaction
        for(User user: resultList){
            user.setPassword(null);
            user.setPasswordSalt(null);
        }
        return resultList;
    }
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Role> getAllRoles() {
        User user = em.find(User.class, principal.getName());
        if (user == null) {
            return Collections.emptyList();
        }
        Query query = em.createNamedQuery("Role.findAll");
        query.setParameter("tenantId", user.getTenant().getTenantId());
        return query.getResultList();
    }

    public void updateUser(User userParam) throws UserNotExistException, InsufficentPasswordStrenghtException {
        if (userParam == null) {
            throw new IllegalArgumentException("Param user is null");
        }
        User user = em.find(User.class, userParam.getUsername());
        String password = null;
        if (user == null) { //new user
            throw new UserNotExistException();
        }
        user.setEmail(userParam.getEmail());
        user.setFullName(userParam.getFullName());
        user.setRoleList(userParam.getRoleList());
        password = userParam.getPassword();
        if (password != null) {
            configureSaltedHashedPasswordForUser(user, password);
        }
    }

    private void configureSaltedHashedPasswordForUser(User user,
                                                      String password) throws InsufficentPasswordStrenghtException {
        validatePassword(password);
        RandomNumberGenerator rng = new SecureRandomNumberGenerator();
        ByteSource salt = rng.nextBytes();
        String hashedPasswordBase64 = new Sha256Hash(password, salt, 1024).toBase64();
        user.setPassword(hashedPasswordBase64);
        user.setPasswordSalt(salt.getBytes());
    }
    
    public void createUser(User userParam) throws UsernameNotAvailableException, InsufficentPasswordStrenghtException{
        if (userParam == null) {
            throw new IllegalArgumentException("Param user is null");
        }
        User userCheck = em.find(User.class, userParam.getUsername());
        if(userCheck != null){
            throw new UsernameNotAvailableException();
        }
        String password = userParam.getPassword();
        configureSaltedHashedPasswordForUser(userParam, password);
        em.merge(userParam);
    }

    private void validatePassword(String password) throws InsufficentPasswordStrenghtException {
        if(password == null|| password.length()<6){
            throw new InsufficentPasswordStrenghtException();
        }
    }
    
    public void saveRole(Role role){
        em.merge(role);
    }

    public final class UsernameNotAvailableException extends Exception {
       
    }
    public final class UserNotExistException extends Exception{
    }
    
    public final class InsufficentPasswordStrenghtException extends Exception{
    
    }
}



