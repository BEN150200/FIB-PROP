package DomainLayer.temp;

public class Quartet<A, B, C, D> {
    public final A a;
    public final B b;
    public final C c;
    public final D d;
    public Quartet(A a, B b, C c, D d){
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override public String toString(){
        return String.format("[%s, %s, %s, %s]", a.toString(), b.toString(), c.toString(), d.toString());
    }
}