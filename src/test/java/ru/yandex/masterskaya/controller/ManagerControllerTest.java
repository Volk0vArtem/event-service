package ru.yandex.masterskaya.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.masterskaya.constants.Constants.X_EVENT_MANAGER;
import ru.yandex.masterskaya.model.manager.ManagerRole;
import ru.yandex.masterskaya.model.manager.dto.CreateManagerDto;
import ru.yandex.masterskaya.model.manager.dto.EventTeamDto;
import ru.yandex.masterskaya.model.manager.dto.ManagerDto;
import ru.yandex.masterskaya.service.management.ManagementService;

@WebMvcTest(ManagerController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ManagerControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @MockBean
    private final ManagementService service;

    ManagerDto manager = new ManagerDto(1L, ManagerRole.MANAGER);

    CreateManagerDto createManager = new CreateManagerDto(1L, manager);

    EventTeamDto savedManager = new EventTeamDto(1L, List.of(manager));

    @Test
    void shouldCreateManagerSuccessfully() throws Exception {
        Mockito
            .when(service.createTeam(anyLong(), any(CreateManagerDto.class)))
            .thenReturn(savedManager);

        mockMvc.perform(post("/managers")
                .content(objectMapper.writeValueAsString(createManager))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_EVENT_MANAGER, 1))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(savedManager)));
    }

    @Test
    void shouldFailCreateManager() throws Exception {
        Mockito
            .when(service.createTeam(anyLong(), any(CreateManagerDto.class)))
            .thenReturn(savedManager);

        //отрицательный id в заголовке
        mockMvc.perform(post("/managers")
                .content(objectMapper.writeValueAsString(createManager))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_EVENT_MANAGER, "-1"))
            .andExpect(status().is4xxClientError());

        //отрицательный eventId
        createManager.setEventId(-1L);
        mockMvc.perform(post("/managers")
                .content(objectMapper.writeValueAsString(createManager))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_EVENT_MANAGER, 1))
            .andExpect(status().is4xxClientError());
        createManager.setEventId(1L);

        //отрицательный id менеджера
        createManager.getManager().setUserId(-1L);
        mockMvc.perform(post("/managers")
                .content(objectMapper.writeValueAsString(createManager))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_EVENT_MANAGER, 1))
            .andExpect(status().is4xxClientError());
        createManager.getManager().setUserId(1L);
    }

    @Test
    void shouldChangeManagerStatusSuccessfully() throws Exception {
        Mockito
            .when(service.updateRole(anyLong(), anyLong(), any(ManagerDto.class)))
            .thenReturn(manager);

        mockMvc.perform(patch("/managers/{eventId}", 1)
                .content(objectMapper.writeValueAsString(manager))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_EVENT_MANAGER, 1))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(manager)));
    }

    @Test
    void shouldFailChangeManagerStatus() throws Exception {
        Mockito
            .when(service.updateRole(anyLong(), anyLong(), any(ManagerDto.class)))
            .thenReturn(manager);

        //отрицательный eventId
        mockMvc.perform(patch("/managers/{eventId}", -1)
                .content(objectMapper.writeValueAsString(manager))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_EVENT_MANAGER, 1))
            .andExpect(status().is4xxClientError());

        //отрицательный id в заголовке
        mockMvc.perform(patch("/managers/{eventId}", 1)
                .content(objectMapper.writeValueAsString(manager))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_EVENT_MANAGER, -1))
            .andExpect(status().is4xxClientError());

        //отрицательный userId
        manager.setUserId(-1L);
        mockMvc.perform(patch("/managers/{eventId}", 1)
                .content(objectMapper.writeValueAsString(manager))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_EVENT_MANAGER, 1))
            .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldGetStaffSuccessfully() throws Exception {
        EventTeamDto team = new EventTeamDto(1L, List.of(manager));
        Mockito
            .when(service.getPersonnel(anyLong()))
            .thenReturn(team);

        mockMvc.perform(get("/managers/{eventId}", 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(team)));
    }

    @Test
    void shouldFailGetStaff() throws Exception {
        EventTeamDto team = new EventTeamDto(1L, List.of(manager));
        Mockito
            .when(service.getPersonnel(anyLong()))
            .thenReturn(team);

        //отрицательный eventId
        mockMvc.perform(get("/managers/{eventId}", -1)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldDeleteManagerSuccessfully() throws Exception {
        Mockito
            .doNothing()
            .when(service).delete(anyLong(), anyLong(), anyLong());

        mockMvc.perform(delete("/managers/{eventId}/{managerId}", 1, 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_EVENT_MANAGER, "1"))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    void shouldFailDeleteManager() throws Exception {
        Mockito
            .doNothing()
            .when(service).delete(anyLong(), anyLong(), anyLong());

        //отрицательный eventId
        mockMvc.perform(delete("/managers/{eventId}/{managerId}", -1, 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_EVENT_MANAGER, "1"))
            .andExpect(status().is4xxClientError());

        //managerId
        mockMvc.perform(delete("/managers/{eventId}/{managerId}", 1, -1)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_EVENT_MANAGER, "1"))
            .andExpect(status().is4xxClientError());

        //отрицательный заголовок
        mockMvc.perform(delete("/managers/{eventId}/{managerId}", 1, 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_EVENT_MANAGER, "-1"))
            .andExpect(status().is4xxClientError());
    }
}