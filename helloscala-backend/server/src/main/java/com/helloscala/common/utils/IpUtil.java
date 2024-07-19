package com.helloscala.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import org.lionsoul.ip2region.xdb.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Objects;

import static com.helloscala.common.Constants.UNKNOWN;

public class IpUtil {
    private static final Logger logger = LoggerFactory.getLogger(IpUtil.class);
    private final static String format_url = "https://apis.map.qq.com/ws/location/v1/ip?ip={}&key=XJIBZ-ZNUWU-ZHGVM-2Z3JG-VQKF2-HXFTB";
    private final static String localIp = "127.0.0.1";
    private final static String resource_name = "ip2region.xdb";
    private static Searcher searcher = null;

    static {
        byte[] cBuff = new byte[0];
        InputStream inputStream = IpUtil.class.getClassLoader().getResourceAsStream(resource_name);
        try {
            File file = new File("./ip2region.xdb");
            FileUtil.writeFromStream(inputStream, file);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            cBuff = Searcher.loadContent(randomAccessFile);
        } catch (Exception e) {
            System.out.printf("failed to load content from `%s`: %s\n", resource_name, e);
        }

        try {
            searcher = Searcher.newWithBuffer(cBuff);
        } catch (Exception e) {
            System.out.printf("failed to create content cached searcher: %s\n", e);
        }
    }


    public static HttpServletRequest getRequest() {
        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return requestAttributes.getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getIp() {
        HttpServletRequest request = getRequest();
        if (Objects.isNull(request)) {
            return "";
        }
        String ipAddress;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (localIp.equals(ipAddress)) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                        ipAddress = inet.getHostAddress();
                    } catch (UnknownHostException e) {
                        logger.error("Unknown host", e);
                    }
                }
            }
            if (ipAddress != null && ipAddress.length() > 15) {
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return "0:0:0:0:0:0:0:1".equals(ipAddress) ? localIp : ipAddress;
    }

    public static String getCityInfo(String ip) {
        if ("127.0.0.1".equals(ip) || "local".equals(ip)) {
            return "LOCAL";
        }
        String s = analyzeIp(ip);
        Map map = JSONUtil.toBean(s, Map.class);
        Integer status = (Integer) map.get("status");
        String address = UNKNOWN;
        if (status == 0) {
            Map result = (Map) map.get("result");
            Map addressInfo = (Map) result.get("ad_info");
            String nation = (String) addressInfo.get("nation");
            String province = (String) addressInfo.get("province");
            String city = (String) addressInfo.get("city");
            address = nation + "-" + province + "-" + city;
        }
        return address;
    }

    public static String getIp2region(String ip) {

        if (searcher == null) {
            logger.error("Error: DbSearcher is null");
            return null;
        }
        String ipInfo = UNKNOWN;
        try {
            ipInfo = searcher.search(ip);
            if (!StrUtil.isEmpty(ipInfo)) {
                ipInfo = ipInfo.replace("|0", "");
                ipInfo = ipInfo.replace("0|", "");
            }
            return ipInfo;
        } catch (Exception e) {
            return ipInfo;
        }
    }

    public static void main(String[] args) {
        System.out.println(getIp2region("58.20.50.137"));
    }

    public static UserAgent getUserAgent(HttpServletRequest request) {
        return UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
    }

    public static String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
        }
        return localIp;
    }

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
        }
        return "UNKNOWN";
    }

    public static String analyzeIp(String ip) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String url = format_url.replace("{}", ip);
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            logger.error("Failed to send request!", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e2) {
                    logger.error("Failed to colse stream!", e2);
                }
            }
        }
        return result.toString();
    }
}
