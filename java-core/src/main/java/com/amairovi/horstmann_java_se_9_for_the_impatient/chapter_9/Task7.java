package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_9;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

public class Task7 {

    private static String digestStr(Path path) throws NoSuchAlgorithmException, IOException {
        byte[] digest = digest(path);
        BigInteger no = new BigInteger(1, digest);

        // Convert message digest into hex value
        String hashtext = no.toString(16);

        // Add preceding 0s to make it 32 bit
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }

        return hashtext;
    }

    private static byte[] digest(Path path) throws NoSuchAlgorithmException, IOException {
        byte[] bytes = Files.readAllBytes(path);

        MessageDigest md = MessageDigest.getInstance("SHA-512");

        md.update(bytes);

        return md.digest();
    }

    // here is the version for Mac
    private static String getSha512WithBuiltInUtils(String pathToFile) throws InterruptedException, IOException {
        Process start = new ProcessBuilder()
                .command("/bin/sh", "-c", "cat " + pathToFile + " | shasum -a 512")
                .start();
        start.waitFor();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(start.getInputStream()))) {
            return reader.readLine().split(" ")[0];
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InterruptedException {
        String pathToFile = "/Users/amairovi/text.txt";
        Path path = Paths.get(pathToFile);

        String actual = digestStr(path);
        System.out.println(actual);

        String expected = getSha512WithBuiltInUtils(pathToFile);
        System.out.println(expected);

        assertThat(actual).isEqualTo(expected);
    }

}
