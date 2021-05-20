package com.xiaohu.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public interface ReportService {
    Map<String, Object> getBusinessReportData() throws Exception;
}
