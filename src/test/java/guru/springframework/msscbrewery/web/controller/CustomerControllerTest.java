package guru.springframework.msscbrewery.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbrewery.services.CustomerService;
import guru.springframework.msscbrewery.web.model.CustomerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @MockBean
    CustomerService customerService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    CustomerDto validCustomer;

    @BeforeEach
    void setUp() {
        validCustomer=CustomerDto.builder().customerId(UUID.randomUUID())
                .customerName("selvi")
                .build();
    }

    @Test
    void getCustomer() throws Exception {
        given(customerService.getCustomerById(any(UUID.class))).willReturn(validCustomer);
        mockMvc.perform(get("/api/v1/customer/"+validCustomer.getCustomerId().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId",is(validCustomer.getCustomerId().toString())))
                .andExpect(jsonPath("$.customerName",is("selvi")));
    }

    @Test
    void saveCustomer() throws Exception {
        CustomerDto customerDto=validCustomer;
        customerDto.setCustomerId(null);
        CustomerDto savedCustomer= CustomerDto.builder().customerId(UUID.randomUUID())
                .customerName("New Customer").build();
        String customerToJson=objectMapper.writeValueAsString(customerDto);
        given(customerService.saveNewCustomer(any())).willReturn(savedCustomer);
        mockMvc.perform(post("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerToJson))
                .andExpect(status().isCreated());
    }

    @Test
    void updateCustomer() throws Exception {
        //given
        CustomerDto customerDto=validCustomer;
        String customerToJson=objectMapper.writeValueAsString(customerDto);
        //when
        mockMvc.perform(put("/api/v1/customer/"+customerDto.getCustomerId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(customerToJson))
                .andExpect(status().isNoContent());
        then(customerService).should().updateCustomer(any(),any());
    }

    @Test
    void deleteCustomer() throws Exception {
        //given
        CustomerDto customerDto=validCustomer;
        //when
        mockMvc.perform(delete("/api/v1/customer/"+validCustomer.getCustomerId()))
                .andExpect(status().isNoContent());
        then(customerService).should().deleteCustomer(any(UUID.class));
    }
}