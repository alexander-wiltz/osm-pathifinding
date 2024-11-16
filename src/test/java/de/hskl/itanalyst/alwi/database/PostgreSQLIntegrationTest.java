package de.hskl.itanalyst.alwi.database;

import de.hskl.itanalyst.alwi.PathfindingApplication;
import de.hskl.itanalyst.alwi.dto.NodeDTO;
import de.hskl.itanalyst.alwi.services.NodeService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {PathfindingApplication.class})
public class PostgreSQLIntegrationTest {

    private List<NodeDTO> nodeDTOs;
    private NodeDTO nodeA;

    @Mock
    private EntityManager mockEntityManager;

    @Mock
    private NodeService nodeService;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdatabase")
            .withUsername("sa")
            .withPassword("sa");

    @Test
    public void testPostgreSQLContainer() {
        Assertions.assertTrue(postgresContainer.isRunning());
    }

    @BeforeEach
    public void setUpTestData() {
        nodeA = new NodeDTO(1L, 52.5200, 13.4050); // Berlin
        assertThat(nodeA).matches(n -> n.getId() != null && n.getLatitude() == 52.5200 && n.getLongitude() == 13.4050);

        nodeDTOs = new ArrayList<>();
        nodeDTOs.add(nodeA);
        assertThat(nodeDTOs).matches(n -> n.size() == 1);
    }

    @Test
    public void testSaveNodes() {
        nodeService.saveAllNodes(nodeDTOs);
        verify(mockEntityManager).persist(nodeDTOs);
    }

    @Test
    public void testGetNodes() {
        when(mockEntityManager.find(NodeDTO.class, nodeA.getId())).thenReturn(nodeA);
        NodeDTO node1 = mockEntityManager.find(NodeDTO.class, 1L);
        verify(mockEntityManager).find(NodeDTO.class, 1L);

        assertThat(node1).isNotNull();
    }

    @Test
    public void testDeleteNodes() {
        when(mockEntityManager.contains(nodeA)).thenReturn(true);
        mockEntityManager.remove(nodeA);
        verify(mockEntityManager).remove(nodeA);
    }
}
