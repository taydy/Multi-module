package com.patsnap.inspad.service.utils;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.savedrequest.SavedRequest;

/**
 * Web request util.
 */
public class WebUtil {

    private WebUtil() {

    }

    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    private static final String X_REQUESTED_WITH = "X-Requested-With";

    private static final String CONTENT_TYPE = "Content-type";
    private static final String CONTENT_TYPE_JSON = "application/json";

    /**
     * is ajax request?
     * @param request HttpServletRequest
     * @return true is ajax request
     */
    public static boolean isAjax(HttpServletRequest request) {
        return XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WITH));
    }

    /**
     * is ajax request?
     * @param request SavedRequest
     * @return true is ajax request
     */
    public static boolean isAjax(SavedRequest request) {
        return request.getHeaderValues(X_REQUESTED_WITH).contains(XML_HTTP_REQUEST);
    }

    /**
     * content-type is json?
     * @param request SavedRequest
     * @return true is json
     */
    public static boolean isContentTypeJson(SavedRequest request) {
        return request.getHeaderValues(CONTENT_TYPE).contains(CONTENT_TYPE_JSON);
    }

}
