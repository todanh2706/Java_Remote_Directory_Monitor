1. Giai đoạn Thiết kế (Design Phase)

-   Thiết kế Giao thức (Protocol): Xác định định dạng dữ liệu gửi qua lại.
    -   Ví dụ: Object bao gồm type (CONNECT, MONITOR_REQ, CHANGE_NOTIFY) và payload (dữ liệu).
-   Thiết kế UI (Mockup):
    -   Server: Bảng danh sách Client, nút chọn thư mục, khung log hiển thị thay đổi.
    -   Client: Form nhập IP Server, nút kết nối, trạng thái đang chạy.

2. Giai đoạn Backend (Core Logic)

-   Module Common:
    -   Tạo các Class Message hoặc Request/Response (phải implements Serializable) để gửi object qua mạng.
-   Module Client:
    -   Viết hàm kết nối Socket tới Server.
    -   Quan trọng: Viết Class xử lý WatchService (Java NIO) để lắng nghe sự thay đổi của thư mục (Create, Modify, Delete).
    -   Gửi thông báo về Server ngay khi phát hiện thay đổi.
-   Module Server:
    -   Viết ServerSocket lắng nghe kết nối.
    -   Đa luồng: Mỗi khi có Client kết nối, tạo một Thread mới để quản lý Client đó (ClientHandler).
    -   Lưu danh sách các ClientHandler vào một List để quản lý.

3. Giai đoạn Frontend (Swing GUI)

-   Client GUI:
    -   Gắn logic kết nối vào nút "Connect".
    -   Hiển thị trạng thái kết nối.
-   Server GUI:
    -   Hiển thị danh sách Client đang online (cập nhật realtime).
    -   Tạo dialog hoặc input để Server gửi yêu cầu "Giám sát thư mục X" tới Client.
    -   TextArea hoặc Table để hiển thị log thay đổi file nhận từ Client.

4. Tích hợp & Kiểm thử

-   Xử lý ngắt kết nối đột ngột (Client tắt máy thì Server phải xóa khỏi danh sách).
-   Test với đường dẫn thư mục có dấu Tiếng Việt.
-   Test mô hình 1 Server - 3 Clients chạy song song.
