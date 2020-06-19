package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_9;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Task8 {

    private final static String PATH_TO_SRC_FOR_ZIP = "";

    private final static String PATH_TO_AND_NAME_OF_FILE_FOR_ZIP = "";

    private final static String PATH_SEPARATOR = File.separator;

    public static void main(String[] args) throws IOException {
        try (
                FileOutputStream fos = new FileOutputStream(PATH_TO_AND_NAME_OF_FILE_FOR_ZIP);
                ZipOutputStream zipOut = new ZipOutputStream(fos)
        ) {
            File fileToZip = new File(PATH_TO_SRC_FOR_ZIP);
            zip(fileToZip, fileToZip.getName(), zipOut);
        }
    }

    private static void zip(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }

        if (fileToZip.isDirectory()) {
            String dirName = fileName.endsWith(PATH_SEPARATOR) ? fileName : fileName + PATH_SEPARATOR;
            zipDirectory(fileToZip, dirName, zipOut);
        } else {
            zipFile(fileToZip, fileName, zipOut);
        }
    }

    private static void zipDirectory(File fileToZip, String dirName, ZipOutputStream zipOut) throws IOException {
        File[] children = fileToZip.listFiles();
        for (File childFile : children) {
            String fullFileName = dirName + childFile.getName();
            zip(childFile, fullFileName, zipOut);
        }
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        try (FileInputStream fis = new FileInputStream(fileToZip)) {
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
        }
    }

}
