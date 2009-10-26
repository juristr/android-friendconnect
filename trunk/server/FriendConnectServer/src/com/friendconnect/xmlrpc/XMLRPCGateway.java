package com.friendconnect.xmlrpc;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.friendconnect.model.TestDTO;

public class XMLRPCGateway {

	public XMLRPCGateway() {

	}

	public String getFirstname() {
		return "test";
	}

	public String answer(String incoming) {
		return incoming + " - Hi there!";
	}

	public Map<String, Object> modifyObject(Map<String, Object> person) throws IllegalArgumentException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		ObjectDeserializer<TestDTO> deserializer = new ObjectDeserializer<TestDTO>(
				TestDTO.class);

		TestDTO dto = deserializer.deSerialize(person);
		dto.setLastname("Strumpflohner");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("firstname", dto.getFirstname());
		result.put("lastname", dto.getLastname());
		return result;
	}

	public int getSimpleRCPTestResult(int x) {
		return x++;
	}
}
