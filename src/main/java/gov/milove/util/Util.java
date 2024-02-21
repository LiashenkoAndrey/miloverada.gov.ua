package gov.milove.util;

import gov.milove.exceptions.UtilException;

import java.net.URLDecoder;

public class Util {

    public static String createContentDispositionHeaderFromFileName(String fileName) {
        return String.format("attachment; filename=\"%s\"", fileName);
    }

    public static String decodeUriComponent(String encodedStr) {
        try {
            return URLDecoder.decode(encodedStr.replace("+", "%2B"), "UTF-8")
                    .replace("%2B", "+");
        } catch (java.io.UnsupportedEncodingException e) {
            throw new UtilException(e.getMessage());
        }
    }

    public static String getFileFormat(String filename) {
        if (filename == null) throw new UtilException("filename is null");
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
