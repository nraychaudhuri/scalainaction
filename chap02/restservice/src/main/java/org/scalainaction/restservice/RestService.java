package org.scalainaction.restservice;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.*;

public class RestService extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("Get method called");
		out.println("parameters: " + parameters(request));
		out.println("headers" + headers(request));
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("Post method called");
		out.println("parameters: " + parameters(request));
		out.println("headers" + headers(request));
	}

	public void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("Delete method called");
	}

	private String headers(HttpServletRequest request) {
		StringBuffer builder = new StringBuffer();
		for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
			String name = (String) e.nextElement();
			builder.append("|" + name + "->" + request.getParameter(name));
		}
		return builder.toString();
	}

	private String parameters(HttpServletRequest request) {
		StringBuffer builder = new StringBuffer();
		for (Enumeration e = request.getHeaderNames(); e.hasMoreElements();) {
			String name = (String) e.nextElement();
			builder.append("|" + name + "->" + request.getHeader(name));
		}
		return builder.toString();
	}
}
