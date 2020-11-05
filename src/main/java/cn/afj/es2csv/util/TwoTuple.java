package cn.afj.es2csv.util;

public class TwoTuple<A,B> {
    public final A a;
    public final B b;

    public TwoTuple(A a, B b){
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }
}
