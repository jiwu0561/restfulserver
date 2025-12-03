package com.example.team_project_mp;
import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class Server {
    private int DEFAULT_BACKLOG = 0;
    private HttpServer server = null; // 서버 인스턴스를 담을 변수

    // 서버 생성
    public Server() throws IOException {
        // 서버 IP 주소 설정. 로컬 호스트:1111로 요청을 받을 거임.
        InetSocketAddress is_Address = new InetSocketAddress("localhost", 1111);
        // 서버 생성
        this.server = HttpServer.create(is_Address, DEFAULT_BACKLOG);
        // "localhost:1111/" 경로로 들어오는 요청에 대해 ServerHandler()가 처리하도록 넘김
        this.server.createContext("/", new ServerHandler());
    }

    // 서버 실행
    public void start() {
        this.server.start();
    }

    // 서버 중지. delay 후 중지됨
    public void stop(int delay) {
        this.server.stop(delay);
    }

    public static void main(String[] args) throws IOException{
        Server httpmanager = new Server();
        httpmanager.start(); // 시작~
        System.out.println("서버 실행 중... 1111 포트로 입력 받는 중");

    }
}
