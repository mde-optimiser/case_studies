package models.service.composition.surrogates.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FilesUtils {

	/**
	 * Copies a file from a source to a destination.
	 * 
	 * @param sourceFile
	 *            is the source file to be copied
	 * @param destFile
	 *            is the new destination file
	 * @throws IOException
	 */
	public static void copyFile(File sourceFile, File destFile) throws IOException {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;

		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String file) throws IOException {
		String everything = null;

		BufferedReader br = new BufferedReader(new FileReader(new File(file)));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			everything = sb.toString();
		} finally {
			br.close();
		}

		return everything;
	}

	/**
	 * 
	 * @param file
	 * @param content
	 * @return
	 * @throws IOException
	 */
	public static File createTempFile(String content) throws IOException {
		File tempFile = File.createTempFile("old-file", ".tmp");

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(tempFile.getAbsolutePath()));
			writer.write(content);
		} catch (IOException e) {
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
			}
		}

		return tempFile;
	}

	/**
	 * Stores the predictor variables of a population in a .csv file.
	 * 
	 * @param names
	 * @param values
	 * @throws IOException
	 */
	public static File writePredictorsCsv(String names[], int values[]) {
		File outputFile = null;

		try {
			outputFile = createTempFile("");

			FileWriter writer = new FileWriter(outputFile, true);

			// Write the header of the csv file
			for (int i = 0; i < values.length; i++) {
				writer.append(names[i]);
				if (i < names.length - 1)
					writer.append(',');
			}

			writer.append('\n');

			// Write the row value of the csv file
			for (int i = 0; i < values.length; i++) {
				writer.append(String.valueOf(values[i]));
				if (i < values.length - 1)
					writer.append(',');
			}

			writer.append('\n');
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return outputFile;
	}
}