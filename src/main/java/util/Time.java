package util;

public class Time {
    // Biến lưu trữ thời điểm bắt đầu đo thời gian
    public static float timeStarted = System.nanoTime();

    // Phương thức trả về thời gian đã trôi qua từ thời điểm bắt đầu (đơn vị: giây)
    public static float getTime() {
        // Tính thời gian bằng cách lấy thời điểm hiện tại và trừ đi thời điểm bắt đầu,
        // sau đó chia cho 1E9 để đổi sang giây
        return (float) ((System.nanoTime() - timeStarted) * 1E-9);
    }
}
