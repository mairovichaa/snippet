package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_8;

public class Task6 {

    private static boolean isAlphabetic(String str) {
        return str.codePoints()
                .allMatch(Character::isAlphabetic);
    }

    private static boolean isCalidIdentifier(String str) {
        if (str.length() == 0) {
            return true;
        }

        return Character.isJavaIdentifierStart(str.charAt(0)) &&
                str.codePoints()
                        .skip(1)
                        .allMatch(Character::isAlphabetic);
    }

}
