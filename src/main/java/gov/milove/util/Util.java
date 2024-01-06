package gov.milove.util;

import gov.milove.exceptions.UtilException;

import java.net.URLDecoder;

public class Util {


    public static String decodeUriComponent(String encodedStr) {
        try {
            return URLDecoder.decode(encodedStr.replace("+", "%2B"), "UTF-8")
                    .replace("%2B", "+");
        } catch (java.io.UnsupportedEncodingException e) {
            throw new UtilException(e.getMessage());
        }
    }
}
