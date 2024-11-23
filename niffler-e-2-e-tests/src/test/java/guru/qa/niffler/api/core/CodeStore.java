package guru.qa.niffler.api.core;

public class CodeStore {
    public CodeStore INSTANCE;
    private static final ThreadLocal<String> codeStore = ThreadLocal.withInitial(String::new);

    private CodeStore(){}

    public static void setCode(String code){
        codeStore.set(code);
    }
    public static String getCode(){
        return codeStore.get();
    }

}
