package com.mmall.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义的会话管理
 *
 * @author hyc
 * @since 2018-09-15
 */
public class MySessionManager extends DefaultWebSessionManager {

    private static final Logger log = LoggerFactory.getLogger(DefaultWebSessionManager.class);
    private String authorization = "Authorization";

    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);
        ServletRequest request = null;
        if (sessionKey instanceof WebSessionKey) {
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }
        if (request != null && sessionId != null) {
            Session session = (Session) request.getAttribute(sessionId.toString());
            if (session != null) {
                return session;
            }
        }
        Session session = super.retrieveSession(sessionKey);
        if (request != null && sessionId != null) {
            request.setAttribute(sessionId.toString(), session);
        }
        return session;
    }

    /**
     * 重写获取sessionId的方法调用当前Manager的获取方法
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        return this.getReferencedSessionId(request, response);
    }

    /**
     * 获取sessionId从请求中
     *
     * @param request
     * @param response
     * @return
     */
    private Serializable getReferencedSessionId(ServletRequest request, ServletResponse response) {
        String id = this.getSessionIdCookieValue(request, response);
        HttpServletRequest req = (HttpServletRequest)request;
        String requestURI = req.getRequestURI();
        if("/express/service/ExportOrder".equals(requestURI)){
            id = req.getParameter("token");
        }
        //if(){}
        if (id != null) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "cookie");
        } else {
            id = this.getUriPathSegmentParamValue(request, "JSESSIONID");
            if (id == null) {
                // 获取请求头中的session
                id = WebUtils.toHttp(request).getHeader(this.authorization);

                if (id == null) {
                    String name = this.getSessionIdName();
                    id = request.getParameter(name);

                    if (id == null) {
                        id = request.getParameter(name.toLowerCase());
                    }
                }
            }
            if (id != null) {
                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "url");
            }
        }

        if (id != null) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
        }

        return id;
    }

    // copy super
    private String getSessionIdCookieValue(ServletRequest request, ServletResponse response) {
        if (!this.isSessionIdCookieEnabled()) {
            log.debug("Session ID cookie is disabled - session id will not be acquired from a request cookie.");
            return null;
        } else if (!(request instanceof HttpServletRequest)) {
            log.debug("Current request is not an HttpServletRequest - cannot get session ID cookie.  Returning null.");
            return null;
        } else {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            return this.getSessionIdCookie().readValue(httpRequest, WebUtils.toHttp(response));
        }
    }

    // copy super
    private String getUriPathSegmentParamValue(ServletRequest servletRequest, String paramName) {
        if (!(servletRequest instanceof HttpServletRequest)) {
            return null;
        } else {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String uri = request.getRequestURI();
            if (uri == null) {
                return null;
            } else {
                int queryStartIndex = uri.indexOf(63);
                if (queryStartIndex >= 0) {
                    uri = uri.substring(0, queryStartIndex);
                }

                int index = uri.indexOf(59);
                if (index < 0) {
                    return null;
                } else {
                    String TOKEN = paramName + "=";
                    uri = uri.substring(index + 1);
                    index = uri.lastIndexOf(TOKEN);
                    if (index < 0) {
                        return null;
                    } else {
                        uri = uri.substring(index + TOKEN.length());
                        index = uri.indexOf(59);
                        if (index >= 0) {
                            uri = uri.substring(0, index);
                        }

                        return uri;
                    }
                }
            }
        }
    }

    // copy super
    private String getSessionIdName() {
        String name = this.getSessionIdCookie() != null ? this.getSessionIdCookie().getName() : null;
        if (name == null) {
            name = "JSESSIONID";
        }

        return name;
    }
}



