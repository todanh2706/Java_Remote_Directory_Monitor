/**
 * Callback cho Client
 * @author Danh, To Huu
 */

package com.danh.client.core;

import com.danh.common.model.FileEvent;

public interface IClientCallback {
    void onConnectionStatusChanged(boolean isConnected, String message); // Gọi khi kết nối thành công, thất bại để GUI cập nhật
    void onMonitorStarted(String folderPath); // Gọi khi Server yêu cầu giám sát folder
    void onFileEvent(FileEvent event); // Gọi khi phát hiện file thay đổi
}