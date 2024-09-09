package lesson20240814;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class PeptidesTest {
	private Peptides peptide;

	@BeforeEach
	public void setUp() {
		String protein = "ABCDEFGHIJKL";
		List<String> library = Arrays.asList("ABCDEFGH", "IJKLMNOP");
		peptide = new Peptides(Peptides.DEFAULT_PEPTIDE_SIZE, protein, library);
	}

	@Test
	public void testCreateKMersDictionary() {
		peptide.createKMersDictionary();
		assertEquals(5, peptide.kmers.size());

		List<Integer> positions = peptide.kmers.get("ABCDEFGH");
		assertNotNull(positions);
		assertTrue(positions.contains(0));
	}

	@Test
	public void testConvertToLong() {
		String peptideString = "ABCDEFGH";
		long expectedLong = peptide.convertStringToLong(peptideString);

		long calculatedValue = 0;
		for (char c : peptideString.toCharArray()) {
			calculatedValue = calculatedValue * 26 + (c - 'A');
		}
		assertEquals(calculatedValue, expectedLong);
	}

	@Test
	public void testConvertPeptidesToLong() {
		peptide.convertPeptidesToLong();
		long expectedLong1 = peptide.convertStringToLong("ABCDEFGH");
		long expectedLong2 = peptide.convertStringToLong("IJKLMNOP");

		assertTrue(peptide.longPeptides.containsKey(expectedLong1));
		assertTrue(peptide.longPeptides.containsKey(expectedLong2));
	}

	@Test
	public void testReverseSearch() {
		peptide.convertPeptidesToLong();

		peptide.reverseSearch();
		assertTrue(peptide.longPeptides.get(peptide.convertStringToLong("ABCDEFGH")).contains(0));
	}

	@Test
	public void testBinarySearch() {
		List<String> library = Arrays.asList("ABCDEFGH", "IJKLMNOP");
		Peptides peptide = new Peptides(Peptides.DEFAULT_PEPTIDE_SIZE, "ABCDEFGHIJKL", library);

		long peptideLong = peptide.convertStringToLong("ABCDEFGH");
		assertTrue(peptide.performBinarySearch(peptideLong));

		long nonExistentPeptideLong = peptide.convertStringToLong("ZZZZZZZZ");
		assertFalse(peptide.performBinarySearch(nonExistentPeptideLong));
	}

	@Test
	public void testBinarySearchForKmers() {
		List<String> library = Arrays.asList("ABCDEFGH", "IJKLMNOP");
		Peptides peptide = new Peptides(Peptides.DEFAULT_PEPTIDE_SIZE, "ABCDEFGHIJKL", library);

		peptide.searchKmersUsingBinarySearch();
	}
}









