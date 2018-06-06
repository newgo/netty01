import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {

        BigDecimal b = new BigDecimal(555.154).multiply(new BigDecimal(-1));
        System.out.println(b);
    }
}
