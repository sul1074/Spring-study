package hello.core.singleton;

public class SingletonService {

    private static final SingletonService instance = new SingletonService();

    // 외부 생성 막음
    private SingletonService() {}

    public static SingletonService getInstance() {
        return instance;
    }
    
    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }
}