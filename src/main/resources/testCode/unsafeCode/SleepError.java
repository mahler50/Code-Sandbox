/**
 * 无限睡眠（阻塞程序异常）
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Long ONE_HOUR = 60 * 60 * 1000L;
        Thread.sleep(ONE_HOUR);
        System.out.println("睡完了");
    }
}
