/**
 * Lớp Socket để liên lạc với Server
 * @author Danh, To Huu
 */

package com.danh.client.core;

import com.danh.common.constants.AppConfig;
import com.danh.common.enums.ActionType;
import com.danh.common.model.FileEvent;
import com.danh.common.model.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClient {
    private Socket socket; // Socket để kết nối với Server
    private ObjectOutputStream out; // Ouput Object
    private ObjectInputStream in; // Input Obecjt
    private IClientCallback callback; // Callback
    private boolean isRunning = false; // Trạng thái
    private String serverHost; // Host
    private int serverPort; // Port

    // Constructor
    public SocketClient (IClientCallback callback) {
        this.callback = callback;
        this.serverHost = AppConfig.DEFAULT_SERVER_HOST;
        this.serverPort = AppConfig.SERVER_PORT;
    }

    public void connect(String clientName) {
        new Thread(() -> {
            try {
                // Kết nối đến Server
                socket = new Socket(serverHost, serverPort);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                isRunning = true;
                callback.onConnectionStatusChanged(true, "Connected to " + serverHost + ":" + serverPort);

                // Gửi tin nhắn chào hỏi (Login)
                sendMessage(new Message(ActionType.LOGIN, clientName, clientName));

                // Vòng lặp lắng nghe lệnh từ Server
                while (isRunning) {
                    try {
                        Message msg = (Message) in.readObject();
                        processMessage(msg);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException ioe) {
                callback.onConnectionStatusChanged(false, "Connection Failed: " + ioe.getMessage());
            }
        }).start();
    }

    // Hàm xử lý message
    private void processMessage(Message msg) {
        switch (msg.getAction()) {
            case START_MONITOR:
                // Server yêu cầu giám sát thư mục này
                String folderPath = (String) msg.getContent();
                // Kích hoạt DirectoryWatcher
                callback.onMonitorStarted(folderPath);
                break;

            case LOGIN_SUCCESS:
                System.out.println("Login OK: " + msg.getContent());
                break;
        }
    }

    // Gửi sự kiện file
    public void sendFileEvent(FileEvent event) {
        sendMessage(new Message(ActionType.FILE_CHANGED, event, "Client"));
    }

    // Gửi tin nhắn
    private void sendMessage(Message msg) {
        try {
            if (out != null) {
                out.writeObject(msg);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}