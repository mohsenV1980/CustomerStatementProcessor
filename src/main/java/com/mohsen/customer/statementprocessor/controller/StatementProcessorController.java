package com.mohsen.customer.statementprocessor.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mohsen.customer.statementprocessor.entity.CustomerRecord;
import com.mohsen.customer.statementprocessor.service.StatementProcessorService;

@RestController
public class StatementProcessorController {

	@Autowired
	private ArrayList<CustomerRecord> customerRecords;
	
	@PostMapping("/")
	public String processStatement(@RequestBody CustomerRecord customerRec) {
	
		return "OK";
	}
	
	
}
