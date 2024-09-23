//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.Random;

public class HW2crypto {
    public HW2crypto() {
    }

    public static String encrypt(String var0) {
        long var1 = 723637564L;
        Random var3 = new Random(var1);
        StringBuilder var4 = new StringBuilder(var0);
        int var5 = 20 - var4.length();
        int var6 = var4.charAt(var4.length() - 1);

        for(int var7 = 0; var7 < var5; ++var7) {
            var4.append((char)((var3.nextInt(2147483647) + var6) % 94 + 33));
            var6 += var3.nextInt(200);
        }

        StringBuilder var12 = new StringBuilder();
        var6 = var4.charAt(var4.length() - 1);
        char[] var8 = var4.toString().toCharArray();
        int var9 = var8.length;

        for(int var10 = 0; var10 < var9; ++var10) {
            char var11 = var8[var10];
            var6 += var11;
            var12.append((char)((var3.nextInt(2147483647) + var6) % 94 + 33));
            var12.append((char)((var3.nextInt(2147483647) + var6) % 94 + 33));
        }

        return var12.toString();
    }

    public static void main(String[] var0) {
        System.out.println(encrypt("cat"));
        System.out.println(encrypt("at"));
        System.out.println(encrypt("dog"));
        System.out.println(encrypt("and"));
        System.out.println(encrypt("tan"));
        System.out.println(encrypt("on"));
        System.out.println(encrypt("arr"));
        System.out.println(encrypt("ace"));
        System.out.println(encrypt("hex"));
    }
}
