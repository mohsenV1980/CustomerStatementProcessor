package com.mohsen.customer.statementprocessor;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigInteger;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mohsen.customer.statementprocessor.entity.CustomerRecord;
import com.mohsen.customer.statementprocessor.entity.ErrorRecord;
import com.mohsen.customer.statementprocessor.entity.StatementResponse;

@SpringBootTest
class CustomerStatementProcessorIntegrationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@BeforeEach
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	private String createResponseJson(String result, List<ErrorRecord> errorList) throws JsonProcessingException {
		StatementResponse response = new StatementResponse();
		response.setResult(result);
		response.getErrorRecords().addAll(errorList);
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(response);
	}

	@Test
	public void integrationTestValidRequest() throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		CustomerRecord rec = null;
		String jsonStr = "";

		// Testing incorrect balance condition

		rec = new CustomerRecord(BigInteger.valueOf(111), "TST001", 10.0, 50.0, 50.0, "");
		jsonStr = objectMapper.writeValueAsString(rec);
		mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(jsonStr).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(createResponseJson("INCORRECT_END_BALANCE",
						List.of(new ErrorRecord(rec.getTransactionRef(), rec.getAccountNo())))));

		// Testing successful statement processing

		rec = new CustomerRecord(BigInteger.valueOf(111), "TST001", 0.0, 50.0, 50.0, "");
		jsonStr = objectMapper.writeValueAsString(rec);
		mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(jsonStr).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(createResponseJson("SUCCESSFUL", List.of())));

		// Testing Duplicate reference condition

		CustomerRecord rec2 = new CustomerRecord(BigInteger.valueOf(111), "TST002", 10.0, 30.0, 40.0, "");
		jsonStr = objectMapper.writeValueAsString(rec2);
		mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(jsonStr).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(createResponseJson("DUPLICATE_REFERENCE",
						List.of(new ErrorRecord(rec.getTransactionRef(), rec.getAccountNo())))));

		// Testing Duplicate reference & incorrect balance condition

		rec2 = new CustomerRecord(BigInteger.valueOf(111), "TST002", 10.0, 30.0, 50.0, "");
		jsonStr = objectMapper.writeValueAsString(rec2);
		mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(jsonStr).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(createResponseJson("DUPLICATE_REFERENCE_INCORRECT_END_BALANCE",
						List.of(new ErrorRecord(rec.getTransactionRef(), rec.getAccountNo()),
								new ErrorRecord(rec2.getTransactionRef(), rec2.getAccountNo())))));

		// Testing another successful statement condition

		rec = new CustomerRecord(BigInteger.valueOf(112), "TST003", 100.0, -50.0, 50.0, "");
		System.out.println(jsonStr);
		jsonStr = objectMapper.writeValueAsString(rec);
		mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(jsonStr).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(createResponseJson("SUCCESSFUL", List.of())));

	}

	@Test
	public void integrationTestInValidRequest() throws Exception {

		String jsonStr = "";

		// Testing incorrect JSON in request

		jsonStr = "{accountNo:112}";
		mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(jsonStr).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(content().json(createResponseJson("BAD_REQUEST", List.of())));

		// Testing incomplete JSON String in request

		jsonStr = "{\"Transaction reference\":\"12444466\",\"Account number\": \"TG6667xdWW\","
				+ "    \"Start Balance\": \"0\", \"Mutation\" : \"+90.4\","
				+ "    \"Description\" : \"testing\",\"End Balance\" : \"+90.4\"";
		mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(jsonStr).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(content().json(createResponseJson("BAD_REQUEST", List.of())));

		// Testing incorrect JSON fields in request

		jsonStr = "{\"reference\":\"12444466\",\"Account number\": \"TG6667xdWW\","
				+ "    \"Start Balance\": \"0\", \"Mutation\" : \"+90.4\","
				+ "    \"Description\" : \"testing\",\"End Balance\" : \"+90.4\"}";
		mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(jsonStr).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(content().json(createResponseJson("BAD_REQUEST", List.of())));
	}
}
