package hello.core.singleton;

public class StatefulService {

    // 상태를 유지하지 않도록 변경
    // private int price;

    public int order(String name, int price) {
        System.out.println("name = " + name + ", price = " + price);

        return price; // 문제 발생
    }
}
