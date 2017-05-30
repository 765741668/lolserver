/**
 * Description : 
 * Created by YangZH on 2017/5/22
 *  13:32
 */

/**
 * Description :
 * Created by YangZH on 2017/5/22
 * 13:32
 */

public class TestIntString {
    public static void main(String[] args) {
        String a = "a";
        Integer b = 1;
        String a1 = new String("a");
        System.out.println(new String("a").equals(a1));
        System.out.println(a.equals(a1));
        System.out.println(a==a1);

        Tuser tu1 = new Tuser(1,"yzh");
        Tuser tu2 = new Tuser(1,"yzh");

        System.out.println(tu1.equals(tu2));
        System.out.println(tu1 == tu2);
    }
}

class Tuser {
    private int id;
    private String name;

    Tuser(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if(this == obj){
            result = true;
        }
        if(null == obj){
            result = false;
        }
        if(obj instanceof Tuser){
            if(this.getId() == ((Tuser) obj).getId()
                    && this.getName().equals(((Tuser) obj).getName())){
                result = true;
            }
        }

        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
