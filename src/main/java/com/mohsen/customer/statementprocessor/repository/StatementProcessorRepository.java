package com.mohsen.customer.statementprocessor.repository;

import java.util.ArrayList;
import org.springframework.stereotype.Repository;

import com.mohsen.customer.statementprocessor.entity.CustomerRecord;

@Repository
public class StatementProcessorRepository {

	private ArrayList<CustomerRecord> customerRecords;

	public StatementProcessorRepository() {
		customerRecords = new ArrayList<CustomerRecord>();
	}

	public boolean addRecord(CustomerRecord rec) {
		return customerRecords.add(rec);
	}

	public CustomerRecord findDuplicateRef(CustomerRecord rec) {
		return customerRecords.stream().filter((item) -> item.getTransactionRef().equals(rec.getTransactionRef()))
				.findAny().orElse(null);
	}

}
