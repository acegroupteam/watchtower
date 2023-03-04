package org.acegroup.watchtower.easyprofiler.web;

import org.acegroup.watchtower.easyprofiler.ProfilerViewRender;
import org.acegroup.watchtower.easyprofiler.annotation.Profiler;
import org.acegroup.watchtower.easyprofiler.autoconfigure.EasyProfilerConfigurationProperties;
import org.acegroup.watchtower.easyprofiler.model.MethodAccessInfo;
import org.acegroup.watchtower.easyprofiler.util.ProfileInfoHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;


@Controller
@Profiler(false)
public class ProfilerController {

    private static final Logger logger = LoggerFactory.getLogger(ProfilerController.class);

    public static final String DEFAULT_URL = "/profiler";


    private EasyProfilerConfigurationProperties properties;


    public ProfilerController(EasyProfilerConfigurationProperties properties) {
        this.properties = properties;
    }

    @RequestMapping(value = DEFAULT_URL, produces = "text/html")
    public void profile(HttpServletRequest request, HttpServletResponse response) {
        boolean enableBasic = properties.isEnableBasic();
        if (!enableBasic) {
            renderView(response, ProfileInfoHolder.getAllAccessInfo());
            return;
        }
        String auth = request.getHeader("Authorization");
        if (auth == null || auth.length() <= 6) {
            ret401(request, response);
            return;
        }

        auth = auth.substring(6);
        auth = new String(Base64.getDecoder().decode(auth));
        String authServer = properties.getUsername() + ":" + properties.getPassword();
        if (auth.equals(authServer)) {
            renderView(response, ProfileInfoHolder.getAllAccessInfo());
            return;
        }
        ret401(request, response);
    }


    private static void ret401(HttpServletRequest request, HttpServletResponse response) {
        String serverName = request.getServerName();
        response.setStatus(401);
        response.setHeader("Cache-Control", "no-store");
        response.setDateHeader("Expires", 0);
        response.setHeader("WWW-authenticate", "Basic Realm=\"" + serverName + "\"");
    }

    private void renderView(HttpServletResponse response, Map<String, List<MethodAccessInfo>> controllerAccessInfo) {
        StringBuilder sb = new StringBuilder(2000);
        sb.append("<html><head> <meta http-equiv=\"refresh\" content=\"15\">\r\n<style type='text/css'>table tr:nth-child(odd) {background-color:#F5F5F5;}\r\ntable tr:nth-child(even) {background-color:#fff;}</style></head><body style='font-size: 12px;font-family:Microsoft YaHei;color:rgb(33, 48, 93);background:#FFF'>");
        sb.append("<div style='padding:17px 4px;;font-size:16px;border-bottom:1px solid #999;'>").append(ProfilerViewRender.statsRequest()).append(ProfilerViewRender.statsThread()).append("</div>");
        sb.append("<table style='margin-top:20px;text-align: left;width:100%;margin:5px;' border='0' cellspacing='0' cellpadding='0'>");
        sb.append("<tr style='height:40px;'>").append(ProfilerViewRender.headerColumns).append("</tr>");
        List<MethodAccessInfo> rows = new ArrayList<>(100);
        controllerAccessInfo.values().forEach(rows::addAll);
        for (int i = 0; i < rows.size(); i++) {
            sb.append(ProfilerViewRender.createTr(rows.get(i)));
        }
        sb.append("</body></html");
        response.setContentType("text/html;charset=UTF-8");
        try {
            response.getWriter().write(sb.toString());
        } catch (IOException ex) {
            logger.warn("renderView failed",ex);
        }

    }


}
