package com.leansoftwarelabs.mypis.client;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StoredProcedureClient {
    public StoredProcedureClient() {
        super();
    }

    public static void main(String[] args) {
        try {
            Connection conn =
                DriverManager.getConnection("jdbc:mysql://localhost:3306/mypis?user=mypis_admin&password=mypis_admin&noAccessToProcedureBodies=true");
            System.out.println(conn);
            CallableStatement cStmt = conn.prepareCall("{CALL mypis.getBalanceSheet()}");
            cStmt.execute();
            ResultSet rs1 = cStmt.getResultSet();
            while (rs1.next()) {
                System.out.println(rs1.getString(1) + "\t" + rs1.getString(2) + "\t" + rs1.getString(3) + "\t" + rs1.getString(4) +"\t" + rs1.getString(5));
            }
            rs1.close();
            int i = 1;
            /* process second result set */
            if (cStmt.getMoreResults()) {
                System.out.println("Another Resultset " + i++);
                ResultSet rs2 = cStmt.getResultSet();
                while (rs2.next()) {
                    System.out.println(rs2.getInt(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                }
                rs2.close();
            }
            cStmt.close();
            conn.close();
        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }
    }
}
