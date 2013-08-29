package com.leansoftwarelabs.realm.shiro;

import java.sql.SQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.JdbcUtils;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;

public class SaltedMutiTenantJdbcRealm extends JdbcRealm {
    
    protected String userTenantIdQuery = "select tenant_id from users where username = ?";
    protected String permissionsQuery = "select permission from roles_permissions where role_name = ? and tenant_id = ?";
    protected String userRolesQuery = "select role_name from user_roles where username = ?";

    public void setUserTenantIdQuery(String userTenantIdQuery) {
        this.userTenantIdQuery = userTenantIdQuery;
    }

    public String getUserTenantIdQuery() {
        return userTenantIdQuery;
    }

    public void setPermissionsQuery(String permissionsQuery) {
        this.permissionsQuery = permissionsQuery;
    }

    public String getPermissionsQuery() {
        return permissionsQuery;
    }

    public void setUserRolesQuery(String userRolesQuery) {
        this.userRolesQuery = userRolesQuery;
    }

    public String getUserRolesQuery() {
        return userRolesQuery;
    }
    
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken userPassToken = (UsernamePasswordToken) token;
        String username = userPassToken.getUsername();
        if (username == null) {
            return null;
        }

        // read password hash and salt from db
        PasswordSalt passwdSalt = getPasswordForUser(username);

        if (passwdSalt == null) {
            return null;
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, passwdSalt.password, getName());
        info.setCredentialsSalt(new SimpleByteSource(passwdSalt.salt));
        return info;
    }

    private PasswordSalt getPasswordForUser(String username) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(authenticationQuery);
            statement.setString(1, username);

            resultSet = statement.executeQuery();
            boolean hasAccount = resultSet.next();
            if (!hasAccount)
                return null;

            byte[] salt = null;
            String password = resultSet.getString(1);
            if (resultSet.getMetaData().getColumnCount() > 1)
                salt = resultSet.getBytes(2);

            if (resultSet.next()) {
                throw new AuthenticationException("More than one user row found for user [" + username +
                                                  "]. Usernames must be unique.");
            }

            return new PasswordSalt(password, salt);
        } catch (SQLException e) {
            final String message = "There was a SQL error while authenticating user [" + username + "]";
            throw new AuthenticationException(message, e);
        } finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(statement);
            JdbcUtils.closeConnection(conn);
        }
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }
        String username = (String) getAvailablePrincipal(principals);
        Connection conn = null;
        Integer tenantId = null;
        Set<String> roleNames = null;
        Set<String> permissions = null;
        try {
            conn = dataSource.getConnection();
            tenantId = getUserTenantId(conn, username);
            // Retrieve roles and permissions from database
            roleNames = getRoleNamesForUser(conn, username, tenantId);
            if (permissionsLookupEnabled) {
                permissions = getPermissions(conn, username, roleNames, tenantId);
            }

        } catch (SQLException e) {
            final String message = "There was a SQL error while authorizing user [" + username + "]";
            // Rethrow any SQL errors as an authorization exception
            throw new AuthorizationException(message, e);
        } finally {
            JdbcUtils.closeConnection(conn);
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
        info.setStringPermissions(permissions);
        return info;

    }

    protected Integer getUserTenantId(Connection conn, String username) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer tenantId = null;
        try {
            ps = conn.prepareStatement(userTenantIdQuery);
            ps.setString(1, username);
            rs = ps.executeQuery();
            // Loop over results and add each returned role to a set
            while (rs.next()) {
                tenantId = rs.getInt(1);
            }
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
        }
        if (tenantId == null) {
            throw new AuthorizationException("Problem encountered in getting the Tenant information of the login user");
        }
        return tenantId;
    }

    protected Set<String> getRoleNamesForUser(Connection conn, String username, Integer tenantId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Set<String> roleNames = new LinkedHashSet<String>();
        try {
            ps = conn.prepareStatement(userRolesQuery);
            ps.setString(1, username);
            ps.setInt(2, tenantId);
            rs = ps.executeQuery();

            // Loop over results and add each returned role to a set
            while (rs.next()) {
                String roleName = rs.getString(1);

                // Add the role to the list of names if it isn't null
                if (roleName != null) {
                    roleNames.add(roleName);
                } else {

                }
            }
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
        }
        return roleNames;
    }

    protected Set<String> getPermissions(Connection conn, String username,
                                         Collection<String> roleNames, Integer tenantId) throws SQLException {
        PreparedStatement ps = null;
        Set<String> permissions = new LinkedHashSet<String>();
        try {
            ps = conn.prepareStatement(permissionsQuery);
            System.out.println(permissionsQuery);
            for (String roleName : roleNames) {
                ps.setString(1, roleName);
                ps.setInt(2, tenantId);
                ResultSet rs = null;
                try {
                    // Execute query
                    rs = ps.executeQuery();
                    // Loop over results and add each returned role to a set
                    while (rs.next()) {
                        String permissionString = rs.getString(1);
                        // Add the permission to the set of permissions
                        permissions.add(permissionString);
                    }
                } finally {
                    JdbcUtils.closeResultSet(rs);
                }
            }
        } finally {
            JdbcUtils.closeStatement(ps);
        }
        return permissions;
    }
}
class PasswordSalt {

    public String password;
    public byte[] salt;

    public PasswordSalt(String password, byte[] salt) {
        super();
        this.password = password;
        this.salt = salt;
    }

}

