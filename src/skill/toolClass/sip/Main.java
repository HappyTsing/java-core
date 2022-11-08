package skill.toolClass.sip;

/**
 * 需要将实现了Logger接口的实现的Jar包放入到META-INF/services文件夹下；
 */
public class Main {
    public static void main(String[] args) {
        LoggerService service = LoggerService.getService();
        service.info("Hello SPI");
        service.debug("Hello SPI");
    }
}
