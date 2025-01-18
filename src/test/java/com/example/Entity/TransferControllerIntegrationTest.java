package com.example.Entity;

import com.example.Entity.Transfer;
import com.example.Entity.TransferRequest;
import com.example.Entity.TransferResponse;
import com.example.Entity.TransferController;
import com.example.Entity.TransferService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class TransferControllerIntegrationTest {

	private MockMvc mockMvc;
	private TransferService transferService;
	private TransferController transferController;

	@BeforeEach
	public void setup() {
		transferService = Mockito.mock(TransferService.class); // Manually creating a mock
		transferController = new TransferController(transferService); // Inject mock service
		mockMvc = MockMvcBuilders.standaloneSetup(transferController).build(); // Setup MockMvc
	}

	@Test
	public void testFindOptimalTransfers() throws Exception {
		// Arrange
		List<Transfer> transfers = Arrays.asList(
				new Transfer(10, 5), // weight, cost
				new Transfer(15, 10)
		);
		TransferRequest request = new TransferRequest(20, transfers); // Max weight = 20
		TransferResponse expectedResponse = new TransferResponse(transfers, 15, 15);

		when(transferService.findOptimalTransfers(request)).thenReturn(expectedResponse);

		// Act and Assert
		mockMvc.perform(post("/api/transfers/optimal")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"availableTransfers\":[{\"weight\":10,\"cost\":5},{\"weight\":15,\"cost\":10}],\"maxWeight\":20}"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.totalCost").value(15))
				.andExpect(jsonPath("$.totalWeight").value(15))
				.andExpect(jsonPath("$.selectedTransfers.length()").value(2));
	}

	@Test
	public void testFindOptimalTransfers_whenNoTransfersFit() throws Exception {
		// Arrange
		List<Transfer> transfers = Arrays.asList(
				new Transfer(30, 15),  // weight, cost
				new Transfer(40, 20)
		);
		TransferRequest request = new TransferRequest(10, transfers); // Max weight = 10
		TransferResponse expectedResponse = new TransferResponse(Arrays.asList(), 0, 0);

		when(transferService.findOptimalTransfers(request)).thenReturn(expectedResponse);

		// Act and Assert
		mockMvc.perform(post("/api/transfers/optimal")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"availableTransfers\":[{\"weight\":30,\"cost\":15},{\"weight\":40,\"cost\":20}],\"maxWeight\":10}"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.totalCost").value(0))
				.andExpect(jsonPath("$.totalWeight").value(0))
				.andExpect(jsonPath("$.selectedTransfers.length()").value(0));
	}

	@Test
	public void testFindOptimalTransfers_whenEmptyList() throws Exception {
		// Arrange
		TransferRequest request = new TransferRequest(10, Arrays.asList()); // Empty list
		TransferResponse expectedResponse = new TransferResponse(Arrays.asList(), 0, 0);

		when(transferService.findOptimalTransfers(request)).thenReturn(expectedResponse);

		// Act and Assert
		mockMvc.perform(post("/api/transfers/optimal")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"availableTransfers\":[],\"maxWeight\":10}"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.totalCost").value(0))
				.andExpect(jsonPath("$.totalWeight").value(0))
				.andExpect(jsonPath("$.selectedTransfers.length()").value(0));
	}
}
