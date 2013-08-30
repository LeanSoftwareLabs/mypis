package com.leansoftwarelabs.mypis.jasper;


import com.leansoftwarelabs.mypis.domain.BaptismalRegister;
import com.leansoftwarelabs.mypis.service.ServiceFacade;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.JRException;


public class DataSourceFactory {
    public DataSourceFactory() {
    };

    public static List<BaptismalRegister> createBaptismalRegisterData() {
        List<BaptismalRegister> registerList = new ArrayList();
        BaptismalRegister register = new BaptismalRegister();
        register.setBaptismalDate(new Date());
        register.setLastName("Pino");
        register.setFirstName("Rommel");
        registerList.add(register);
        return registerList;
    }

    public static void main(String[] args) {
        try {
            Map parameters = new HashMap<String, String>();
            InputStream reportStream = new FileInputStream("C:/leansoftwarelabs/SampleReports/BaptismalCertificate.jasper");
            JRDataSource datasource = new JRBeanCollectionDataSource(createBaptismalRegisterData(), true);
            JasperPrint print = JasperFillManager.fillReport(reportStream, parameters, datasource);
            JasperPrintManager.printReport(print, true);
        } catch (JRException jre) {
            // TODO: Add catch code
            jre.printStackTrace();
        } catch (FileNotFoundException fnfe) {
            // TODO: Add catch code
            fnfe.printStackTrace();
        }
    }
}
