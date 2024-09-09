package lesson20240814;

import java.util.*;

public class Peptides {

	public static final int DEFAULT_PEPTIDE_SIZE = 8;

	private String protein;
	private int peptideSize;

	public HashMap<String, List<Integer>> kmers = new LinkedHashMap<>();

	private List<String> library;

	Map<Long, List<Integer>> longPeptides = new HashMap<>();

	private long[] peptideLongArray;

	public Peptides(int peptideSize, String protein, List<String> library) {
		this.peptideSize = peptideSize;
		this.protein = protein;
		this.library = library;
		createKMersDictionary();
		convertPeptidesToLong();
		peptidesToLongArray(library);
	}



	// Approach 1
	void createKMersDictionary() {
		for (int i = 0; i < protein.length() - peptideSize + 1; i++) {
			String kmer = protein.substring(i, i + peptideSize);
			List<Integer> positions = kmers.get(kmer);
			if (positions == null) {
				positions = new ArrayList<>();
				kmers.put(kmer, positions);
			}
			positions.add(i);
		}
	}

	// Approach 1
	public Map<String, List<Integer>> searchLibrary() {
		LinkedHashMap<String, List<Integer>> existingPeptides = new LinkedHashMap<String, List<Integer>>();
		for (String peptide : library) {
			List<Integer> positions = kmers.get(peptide);
			if (null == positions)
				continue;
			existingPeptides.put(peptide, positions);
		}
		return existingPeptides;
	}

	// Approach 2
	void reverseSearch() {
		boolean isMatchFound = false;

		for (int index = 0; index <= protein.length() - peptideSize; index++) {
			String fragment = protein.substring(index, index + peptideSize);
			long fragmentAsLong = convertStringToLong(fragment);

			if (longPeptides.containsKey(fragmentAsLong)) {
				longPeptides.get(fragmentAsLong).add(index);
				System.out.println("Match: " + fragment + " found at position " + index);
				isMatchFound = true;
			}
		}

		if (!isMatchFound) {
			System.out.println("No matches from the library were found within the protein sequence.");
		}
	}

	// Approach 3
	void convertPeptidesToLong() {
		for (String peptideSequence : library) {
			long encodedPeptide = convertStringToLong(peptideSequence);
			longPeptides.putIfAbsent(encodedPeptide, new ArrayList<>());
		}
	}


	// Approach 4
	void peptidesToLongArray(List<String> peptideLibrary) {
		peptideLongArray = new long[peptideLibrary.size()];

		for (int index = 0; index < peptideLibrary.size(); index++) {
			peptideLongArray[index] = convertStringToLong(peptideLibrary.get(index));
		}

		Arrays.sort(peptideLongArray);
	}

	// Approach 4
	void searchKmersUsingBinarySearch() {
		boolean isFound = false;

		for (int i = 0; i <= protein.length() - peptideSize; i++) {
			String kmer = protein.substring(i, i + peptideSize);
			long kmerEncoded = convertStringToLong(kmer);

			if (performBinarySearch(kmerEncoded)) {
				System.out.println("Match found: " + kmer + " at position " + i);
				isFound = true;
			}
		}

		if (!isFound) {
			System.out.println("No matching peptides from the library were found in the protein.");
		}
	}

	boolean performBinarySearch(long kmerEncoded) {
		return Arrays.binarySearch(peptideLongArray, kmerEncoded) >= 0;
	}

	long convertStringToLong(String peptideSequence) {
		long encodedValue = 0;
		for (char aminoAcid : peptideSequence.toCharArray()) {
			encodedValue = encodedValue * 26 + (aminoAcid - 'A');
		}
		return encodedValue;
	}


}
