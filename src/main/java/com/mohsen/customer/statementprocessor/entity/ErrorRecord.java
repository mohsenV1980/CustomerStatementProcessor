package com.mohsen.customer.statementprocessor.entity;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorRecord {

	private BigInteger reference;

	@JsonProperty("AccountNumber")
	private String AccountNo;

}
