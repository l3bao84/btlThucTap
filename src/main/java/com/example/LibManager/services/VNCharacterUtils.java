package com.example.LibManager.services;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class VNCharacterUtils {

    // Xóa dấu trong tiếng việt
    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ', 'd').replace('Đ', 'D');
    }

    // Xóa khoảng trắng
    public static String removeSpace(String s) {
        return s.replaceAll(" ", "");
    }
}
