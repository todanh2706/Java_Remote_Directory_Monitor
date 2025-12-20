/**
 * Lớp chính của Client
 *
 * @author Danh, To Huu
 */

package com.danh.client;

import com.danh.client.core.DirectoryWatcher;
import com.danh.client.core.IClientCallback;
import com.danh.client.core.SocketClient;
import com.danh.common.model.FileEvent;

import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) {
        // Tạo Callback để xử lý sự kiện từ Socket và Watcher trả về
        IClientCallback callback = new IClientCallback() {
            @Override
            public void onConnectionStatusChanged(boolean isConnected, String message) {
                System.out.println(isConnected ? "[SUCCESS]: " + message : "[ERROR]: " + message);
            }

            @Override
            public void onMonitorStarted(String folderPath) {
                System.out.println("[INFO]: Server requested to monitor: " + folderPath);

                // Kích hoạt luồng giám sát thư mục
                DirectoryWatcher watcher = new DirectoryWatcher(folderPath, this);
                new Thread(watcher).start();
                System.out.println("[INFO]: Watcher service started for " + folderPath);
            }

            @Override
            public void onFileEvent(FileEvent event) {
                // Khi file thay đổi, in ra màn hình và gửi ngược lại Server
                System.out.println("[FILE CHANGED]: " + event.toString());

                // TODO: Gọi socket để gửi event này về Server
                // client.sendFileEvent(event);
            }
        };

        // Khởi tạo SocketClient và kết nối
        System.out.println("Enter your client name (e.g., PC-1):");
        String name = new Scanner(System.in).nextLine();

        SocketClient client = new SocketClient(callback);
        client.connect(name);

        // Giữ chương trình chạy
        while (true) {
            // Vòng lặp vô tận để main thread không chết
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }
    }
}