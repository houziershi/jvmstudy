import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;

import java.util.HashMap;

public class JOL {

    public static void main(String[] args) {

        System.out.println(VM.current().details());
        HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
        hashMap.put(15, "this is " );
//        for (int i = 0; i < 65; i++) {
//            hashMap.put(i, "this is " + i);
//        }
        System.out.println(GraphLayout.parseInstance(hashMap).toPrintable());
    }
}
