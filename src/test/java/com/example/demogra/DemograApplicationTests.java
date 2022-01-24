package com.example.demogra;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.demogra.controller.ClientController;
import com.example.demogra.entity.Client;
import com.example.demogra.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class DemograApplicationTests {

	private MockMvc mockMvc;

	ObjectMapper objectMapper = new ObjectMapper();
	ObjectWriter objectWriter = objectMapper.writer();

	@InjectMocks
	private ClientController clientController;

	@Mock
	private ClientService clientService;

	@BeforeAll
	void setUp() {
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
	}

	Client client_1 = new Client(0L,"abc","dfg");
	Client client_2 = new Client(1L,"hij","kmn");
	Client client_3 = new Client(2L,"lop","qrs");

	List<Client> record = new ArrayList<>(Arrays.asList(client_1,client_2,client_3));

	@Test
	public void ContextLoad() throws Exception {
	}

	@Test
	public void GetAllMappingSucess() throws Exception {

		Mockito.when(clientService.getAll()).thenReturn(record);
		mockMvc.perform(MockMvcRequestBuilders
						.get("/client/")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is(client_1.getName())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].name", is(client_2.getName())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].name", is(client_3.getName())));
	}

	@Test
	public void GetOneMappingSucess() throws Exception {

		Mockito.when(clientService.getOne(0l)).thenReturn(client_1);
		mockMvc.perform(MockMvcRequestBuilders
						.get("/client/0")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name", is(client_1.getName())));
	}

	@Test
	public void PostMappingSucess() throws Exception {

		Mockito.when(clientService.save(client_1)).thenReturn(client_1);

		String content = objectWriter.writeValueAsString(client_1);

		System.out.println(content);

		mockMvc.perform(MockMvcRequestBuilders
						.post("/client/")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content(content))
				.andExpect(status().isCreated());
	}

	@Test
	public void PutMappingSucess() throws Exception {

		Mockito.when(clientService.update(client_1)).thenReturn(client_1);

		String content = objectWriter.writeValueAsString(client_1);

		System.out.println(content);

		mockMvc.perform(MockMvcRequestBuilders
						.put("/client/")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content(content))
						.andExpect(status().isOk())
						.andExpect(MockMvcResultMatchers.jsonPath("$.name", is(client_1.getName())));
	}

	@Test
	public void DeleteMappingSucess() throws Exception {

		Mockito.when(clientService.delete(0l)).thenReturn(true);
		String content = objectWriter.writeValueAsString(client_1);

		System.out.println(content);

		mockMvc.perform(MockMvcRequestBuilders
						.delete("/client/0")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}



}
