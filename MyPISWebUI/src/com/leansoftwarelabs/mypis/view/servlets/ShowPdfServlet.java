package com.leansoftwarelabs.mypis.view.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@WebServlet(name = "ShowPdfServlet", urlPatterns = { "/showpdf" })
public class ShowPdfServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "application/pdf";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filename = request.getParameter("filename");
        String printId = request.getParameter("printId");
        JasperPrint jasperPrint = (JasperPrint) request.getSession().getAttribute(printId);
        request.getSession().removeAttribute(printId);
        response.setContentType(CONTENT_TYPE);
        response.addHeader("Content-Type", "application/pdf");
        response.addHeader("Content-disposition", "inline;filename=" + filename + ".pdf");
        try {      
            ServletOutputStream servletOutputStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
    }
}
