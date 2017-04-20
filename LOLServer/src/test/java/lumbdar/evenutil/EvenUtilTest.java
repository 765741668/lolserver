package lumbdar.evenutil;/**
 * Description : 
 * Created by YangZH on 2017/4/16
 *  19:35
 */

import com.lol.buffer.GameUpBuffer;
import com.lol.tool.EventUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Description :
 * Created by YangZH on 2017/4/16
 * 19:35
 */

public class EvenUtilTest {

    public static void main(String[] args) {
        new ClassSelectRoom();
        new ClassSelect().init();
    }
}

class ClassSelect {
    public void init() {
        EventUtil.initSelectRoom.init(Arrays.asList(1, 2), Arrays.asList(3, 4), null);
    }
}

class ClassSelectRoom {
    ClassSelectRoom() {
        EventUtil.initSelectRoom = this::init;
    }

    public void init(List<Integer> teamOne, List<Integer> teamTwo, GameUpBuffer data) {
        teamOne.stream().forEach(System.out::println);
        teamTwo.stream().forEach(System.out::println);
        System.out.println(data);
    }
}
