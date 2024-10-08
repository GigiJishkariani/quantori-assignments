package lesson20240814;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestBenchmark {

    @Test
    public void testGenerateProtein() {
        String protein = Benchmark.generateProtein(10);
        assertEquals(10, protein.length());

        for (char c : protein.toCharArray()) {
            assertTrue(c >= 'A' && c <= 'Z');
        }
    }

    @Test
    public void testGenerateLibrary() {
        List<String> library = Benchmark.generateLibrary(5);
        assertEquals(5, library.size());

        for (String peptide : library) {
            assertEquals(10_000, peptide.length());
        }
    }
}
