package org.zt.sweet.io;

import java.io.File;

public class FilePaths {
    public static boolean isInJar(File file) {
        return isInJar(file.getAbsolutePath());
    }

    public static boolean isInJar(String filePath) {
        return filePath.contains(".jar!");
    }
}
