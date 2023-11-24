package com.chitter.server;

import com.chitter.server.controller.PeepController;
import com.chitter.server.model.Peep;
import com.chitter.server.repository.PeepRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(PeepController.class)
public class PeepControllerTests {

    @MockBean
    private PeepRepository peepRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnPeeps() throws Exception {
        List<Peep> peeps = new ArrayList<>(
                Arrays.asList(new Peep("testUsername", "testname", "This is a testPeep", "testDate"),
                        new Peep("testUsername2", "testname2", "This is a testPeep2", "testDate2"),
                        new Peep("testUsername3", "testname3", "This is a testPeep3", "testDate3")));

        when(peepRepository.findAll()).thenReturn(peeps);
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(peeps.size()))
                .andDo(print());
    }

    @Test
    void shouldReturnNoContent() throws Exception {
        List<Peep> peeps = new ArrayList<>();

        when(peepRepository.findAll()).thenReturn(peeps);
        mockMvc.perform(get("/"))
                .andExpect(status().isNoContent())
                .andDo(print());

    }

    @Test
    void shouldCreateNewPeep() throws Exception {
        Peep peep = new Peep("testUsername", "testname", "This is a testPeep", "testDate");

        mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(peep)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void shouldReturnErrorIfNoPeepContent() throws Exception {
        Peep peep = new Peep("testUsername", "testname", null, "testDate");

        mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(peep)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
