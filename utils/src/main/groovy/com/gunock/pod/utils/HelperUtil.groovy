package com.gunock.pod.utils

import java.nio.file.Files
import java.nio.file.Paths

class HelperUtil {

    static String readFile(String path) throws FileNotFoundException {
        return Files.readString(Paths.get(path))
    }

    static void writeFile(String path, String text) {
        File file = new File(path)
        file.write(text, 'UTF-8')
    }

}
