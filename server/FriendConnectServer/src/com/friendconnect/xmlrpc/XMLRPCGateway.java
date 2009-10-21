package com.friendconnect.xmlrpc;

public class XMLRPCGateway {

	public XMLRPCGateway() {
		
	}
	
	public String getFirstname(){
		return "test";
	}
	
	
	public int getSimpleRCPTestResult(int x){
		return x++;
	}
}
