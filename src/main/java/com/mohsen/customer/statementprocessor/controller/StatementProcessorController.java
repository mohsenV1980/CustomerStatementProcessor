package com.mohsen.customer.statementprocessor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mohsen.customer.statementprocessor.entity.CustomerRecord;
import com.mohsen.customer.statementprocessor.entity.StatementResponse;
import com.mohsen.customer.statementprocessor.exception.InvalidCustomerRecordException;
import com.mohsen.customer.statementprocessor.service.StatementProcessorService;

@RestController
public class StatementProcessorController {

	@Autowired
	private StatementProcessorService statementService;

	@PostMapping("/")
	public ResponseEntity<StatementResponse> processStatement(@RequestBody CustomerRecord customerRec) {

		boolean validBalance = statementService.checkEndingBalance(customerRec);
		CustomerRecord dupRec = statementService.findDuplicateRef(customerRec);

		try {
			if (validBalance && dupRec == null)
				statementService.addRecord(customerRec);
		} catch (InvalidCustomerRecordException exp) {
			StatementResponse response = statementService.createResponse(false, null, null, validBalance);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		StatementResponse response = statementService.createResponse(true, customerRec, dupRec, validBalance);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
