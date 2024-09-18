package alwi.algorithm;

import de.hskl.itanalyst.alwi.dto.NodeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class AStarAlgorithmMockTest {

    @Test
    public void test() {
        List<NodeDTO> testroute = new ArrayList<>();

        // TODO compute Route!
        testroute.add(new NodeDTO(1L, 6.707762, 49.234920));
        testroute.add(new NodeDTO(2L, 6.706711, 49.234899));
        testroute.add(new NodeDTO(3L, 6.706695, 49.235384));
        testroute.add(new NodeDTO(4L, 6.705992, 49.235391));
        testroute.add(new NodeDTO(5L, 6.705496, 49.235382));
        testroute.add(new NodeDTO(6L, 6.704935, 49.235380));
        testroute.add(new NodeDTO(7L, 6.704672, 49.235386));
        testroute.add(new NodeDTO(8L, 6.704667, 49.235508));
        testroute.add(new NodeDTO(9L, 6.703940, 49.235515));
        testroute.add(new NodeDTO(10L, 6.703921, 49.235876));
        testroute.add(new NodeDTO(11L, 6.703927, 49.235918));
    }
}
