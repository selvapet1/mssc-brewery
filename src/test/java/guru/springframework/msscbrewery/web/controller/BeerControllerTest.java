package guru.springframework.msscbrewery.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbrewery.services.BeerService;
import guru.springframework.msscbrewery.web.model.BeerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
;import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @MockBean
    BeerService beerService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;

    BeerDto validBeer;

    @BeforeEach
    void setUp() {
        validBeer=BeerDto.builder().id(UUID.randomUUID())
                .beerName("Leech")
                .beerStyle("FreeStyle")
                .upc(123456789L)
                .build();
    }

    @Test
    void getBeer() throws Exception{
        given(beerService.getBeerById(any(UUID.class))).willReturn(validBeer);
        mockMvc.perform(get("/api/v1/beer/"+validBeer.getId().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id",is(validBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName",is("Leech")));
    }

    @Test
    void saveBeer() throws Exception {
        BeerDto beerDto=validBeer;
        beerDto.setId(null);
        BeerDto savedBeer= BeerDto.builder().id(UUID.randomUUID()).beerName("New Beer").build();
        String beerToJson=objectMapper.writeValueAsString(beerDto);
        given(beerService.saveNewBeer(any())).willReturn(savedBeer);
        mockMvc.perform(post("/api/v1/beer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerToJson))
                .andExpect(status().isCreated());
    }

    @Test
    void updateBeer() throws Exception {
        //given
        BeerDto beerDto=validBeer;
        String beerToJson=objectMapper.writeValueAsString(beerDto);
        //when
        mockMvc.perform(put("/api/v1/beer/"+beerDto.getId())
               .contentType(MediaType.APPLICATION_JSON)
                .content(beerToJson))
                .andExpect(status().isNoContent());
        then(beerService).should().updateBeer(any(),any());

    }

    @Test
    public void deleteBeer() throws Exception
    {
        //given
        BeerDto beerDto=validBeer;
        //when
        mockMvc.perform(delete("/api/v1/beer/"+beerDto.getId()))
                .andExpect(status().isNoContent());
        then(beerService).should().deleteBeer(any(UUID.class));
    }
}