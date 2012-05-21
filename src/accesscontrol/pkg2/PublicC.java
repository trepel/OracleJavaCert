package accesscontrol.pkg2;

import accesscontrol.pkg1.PublicA;

public class PublicC {

    void method() {
//        new DefaultB(); // compile error
        new PublicA();
    }
}
