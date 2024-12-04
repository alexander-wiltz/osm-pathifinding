package de.hskl.itanalyst.alwi.restcontroller;

import de.hskl.itanalyst.alwi.controller.NodeController;
import de.hskl.itanalyst.alwi.dto.NodeDTO;
import de.hskl.itanalyst.alwi.services.NodeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

@SpringBootTest(classes = {NodeController.class})
public class NodeControllerTest {

    @Mock
    private NodeService nodeService;

    @Test
    public void testGetNodeById_Success() throws Exception {
        NodeDTO mockNode = new NodeDTO(1L, 6.707762, 49.234920);
        Mockito.when(nodeService.findNodeById(1L)).thenReturn(Optional.of(mockNode));
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/nodes/1")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.lat").value(6.707762))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.lon").value(49.234920));
    }

    @Test
    public void testGetNodeById_NotFound() throws Exception {
        Mockito.when(nodeService.findNodeById(2L)).thenReturn(null);
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/nodes/2")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
