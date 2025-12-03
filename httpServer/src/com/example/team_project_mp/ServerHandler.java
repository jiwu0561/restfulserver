package com.example.team_project_mp;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ServerHandler implements HttpHandler{
    private DBManager db;
        
    public ServerHandler() {
        try {
            db = new DBManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void handle(HttpExchange arg0) throws IOException{

        String method = arg0.getRequestMethod();
        // Path는 안 쓸듯
        String query = arg0.getRequestURI().getQuery();
        OutputStream resBody = arg0.getResponseBody();

        String resText = ""; // 응답 문자열
        int status = 200;

        try{

            if (method.equals("GET")) {
                if (query != null) {
                    HashMap<String, String> map = getParamsMap(query);
                    String id = map.get("id");
                    String pw = map.get("pw");

                    if (id == null || pw == null) {
                        resText = "id 또는 pw 없음";
                        status = 400;
                    }
                    else {
                    boolean ok = login(id, pw);
                    resText = ok?"로그인 성공":"로그인 실패";
                    }
                } 
            }
            else {
                resText = "오류";
                status = 404;
            }
            if (method.equals("POST")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(arg0.getRequestBody(), StandardCharsets.UTF_8));
                String body = reader.readLine();
                if (body!=null) body = URLDecoder.decode(body, "UTF-8");
                HashMap<String, String> map = getParamsMap(body);
                
                String id = map.get("id");
                String pw = map.get("pw");
                String name = map.get("name");
                
                if (id == null || pw == null || name == null) {
                    resText = "일부 입력 누락됨";
                    status = 400;
                }
                else {
                    boolean ok = signin(id, pw, name);
                    resText = ok?"회원가입 성공":"회원가입 실패";
                }
            }
           byte[] bytes = resText.getBytes(StandardCharsets.UTF_8);
            arg0.getResponseHeaders().add("Content-Type", "text/plain; charset=UTF-8");
            arg0.sendResponseHeaders(status, bytes.length);

            resBody.write(bytes);
            resBody.close();
        
        } catch (IOException e) {
            e.printStackTrace();
        }
        

    }
    
    // 파라미터 맵 객체를 얻는 메서드
    private HashMap<String, String> getParamsMap(String query) {
        HashMap<String, String> param_Map = new HashMap<>();
        String[] params_Array = query.split("&"); // 각 파라미더만 나눈 배열
        for (String param_Str : params_Array) {
            String[] param_Array = param_Str.split("=");
            if (param_Array.length >1)
                param_Map.put(param_Array[0], param_Array[1]); // 0에 키, 1에 밸류로 저장
            else
                param_Map.put(param_Array[0], ""); // 값이 없으면 밸류는 빈칸
        }
        return param_Map;
    }

    private boolean login(String id, String pw) {
        try {
            Statement st = db.getConnection().createStatement();
            String sql = "SELECT * FROM account WHERE user_id='"+id+"' AND user_pw='"+pw+"'";
            return st.executeQuery(sql).next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
    }
    private boolean signin(String id, String pw, String name) {
        try {
            Statement st = db.getConnection().createStatement();
            String sql = "INSERT INTO account (user_id, user_pw, nickname) VALUES ('"
                        +id+"', '"+pw+"', '"+name+"')";
            return st.executeUpdate(sql) == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
}