package com.t1works.groupware.ody.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Controller
public class AirOdyController {

	
	   // 어제 날씨
    @ResponseBody
	@RequestMapping(value="/t1/airstatus.tw")
	    public String restApiGetAir(HttpServletRequest request, HttpServletResponse response) throws Exception{

    	//http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?serviceKey=Kb4EM%2FntmrXOi76DygiRHcmeF5z2%2BVNzCueqSypkUJTAIDbkCluUPi8REeX9m65vJ%2FiGwliXM%2FnlDL5GjSS3Kg%3D%3D&returnType=xml&numOfRows=100&pageNo=1&sidoName=%EC%84%9C%EC%9A%B8&ver=1.0
	        String url = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty"
	            + "?serviceKey=Kb4EM%2FntmrXOi76DygiRHcmeF5z2%2BVNzCueqSypkUJTAIDbkCluUPi8REeX9m65vJ%2FiGwliXM%2FnlDL5GjSS3Kg%3D%3D"
	            + "&returnType=JSON"            // JSON, XML
	            + "&numOfRows=100"             // 페이지 ROWS
	            + "&pageNo=1"                 // 페이지 번호
	            + "&sidoName=서울"     // 발표일자
	            + "&ver=1.0";           // 발표시각
	        
	        HashMap<String, Object> resultMap = getDataFromJson2(url, "UTF-8", "get", "");
	        
	        System.out.println("# RESULT : " + resultMap);

	        JSONObject jsonObj = new JSONObject();
	        
	        jsonObj.put("result", resultMap);
	        
	        return jsonObj.toString();
	    }
	    
	    public HashMap<String, Object> getDataFromJson2(String url, String encoding, String type, String jsonStr) throws Exception
	    {
	        boolean isPost = false;

	        if ("post".equals(type))
	        {
	            isPost = true;
	        }
	        else
	        {
	            url = "".equals(jsonStr) ? url : url + "?request=" + jsonStr;
	        }

	        return getStringFromURL(url, encoding, isPost, jsonStr, "application/json");
	    }    
   
@SuppressWarnings("unchecked")
public HashMap<String, Object> getStringFromURL(String url, String encoding, boolean isPost, String parameter, String contentType) throws Exception
{
    URL apiURL = new URL(url);

    HttpURLConnection conn = null;
    BufferedReader br = null;
    BufferedWriter bw = null;

    HashMap<String, Object> resultMap = new HashMap<String, Object>();

    try
    {
        conn = (HttpURLConnection) apiURL.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.setDoOutput(true);

        if (isPost)
        {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestProperty("Accept", "*/*");
        }
        else
        {
            conn.setRequestMethod("GET");
        }

        conn.connect();

        if (isPost)
        {
            bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            bw.write(parameter);
            bw.flush();
            bw = null;
        }

        br = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));

        String line = null;

        StringBuffer result = new StringBuffer();

        while ((line=br.readLine()) != null) result.append(line);

        ObjectMapper mapper = new ObjectMapper();

        resultMap = mapper.readValue(result.toString(), HashMap.class);
    }
    catch (Exception e)
    {
        e.printStackTrace();
        throw new Exception(url + " interface failed" + e.toString());
    }
    finally
    {
        if (conn != null) conn.disconnect();
        if (br != null) br.close();
        if (bw != null) bw.close();
    }

    return resultMap;
}
}
