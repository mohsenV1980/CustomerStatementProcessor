package com.mohsen.customer.statementprocessor.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatementResponse {
	private String result;
	private List<ErrorRecord> errorRecords;

	public StatementResponse() {
		result = "";
		errorRecords = new ArrayList<ErrorRecord>();
	}
}
