package cn.edu.sdu.orz.service;

import javax.servlet.http.HttpServletRequest;

public interface GetIPService {
    public String getRemoteIP(HttpServletRequest request);
}
