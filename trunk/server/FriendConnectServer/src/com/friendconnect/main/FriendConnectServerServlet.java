package com.friendconnect.main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlrpc.webserver.WebServer;

@SuppressWarnings("serial")
public class FriendConnectServerServlet extends HttpServlet {
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		//initiate the XML-RPC server
		WebServer server = new WebServer(8080);
		
		
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
	}
}
