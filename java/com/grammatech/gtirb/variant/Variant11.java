package com.grammatech.gtirb.variant;

import java.util.Optional;

/**
 * A 11-valued variant. The variant can store a single object that is
 * of one of the generic parameter types provided.
 *
 * Note that this class is abstract. Intended use is to extend this
 * class with a simple wrapper that uses named getters/setters.
 */
public abstract class Variant11<A, B, C, D, E, F, G, H, I, J, K> {
    private Object o;
    private int index;

    /**
     * Initialize the variant with an object of the first type.
     *
     * @param tok This is a marker parameter indicating which
     * field of the variant is to be populated by the constructor.
     * @param o The object to populate the variant with.
     */
    protected Variant11(Token.T0 tok, A o) {
        this.index = 0;
        this.o = o;
    }

    /**
     * Initialize the variant with an object of the second type.
     *
     * @param tok This is a marker parameter indicating which
     * field of the variant is to be populated by the constructor.
     * @param o The object to populate the variant with.
     */
    protected Variant11(Token.T1 tok, B o) {
        this.index = 1;
        this.o = o;
    }

    /**
     * Initialize the variant with an object of the third type.
     *
     * @param tok This is a marker parameter indicating which
     * field of the variant is to be populated by the constructor.
     * @param o The object to populate the variant with.
     */
    protected Variant11(Token.T2 tok, C o) {
        this.index = 2;
        this.o = o;
    }

    /**
     * Initialize the variant with an object of the fourth type.
     *
     * @param tok This is a marker parameter indicating which
     * field of the variant is to be populated by the constructor.
     * @param o The object to populate the variant with.
     */
    protected Variant11(Token.T3 tok, D o) {
        this.index = 3;
        this.o = o;
    }

    /**
     * Initialize the variant with an object of the fifth type.
     *
     * @param tok This is a marker parameter indicating which
     * field of the variant is to be populated by the constructor.
     * @param o The object to populate the variant with.
     */
    protected Variant11(Token.T4 tok, E o) {
        this.index = 4;
        this.o = o;
    }

    /**
     * Initialize the variant with an object of the sixth type.
     *
     * @param tok This is a marker parameter indicating which
     * field of the variant is to be populated by the constructor.
     * @param o The object to populate the variant with.
     */
    protected Variant11(Token.T5 tok, F o) {
        this.index = 5;
        this.o = o;
    }

    /**
     * Initialize the variant with an object of the seventh type.
     *
     * @param tok This is a marker parameter indicating which
     * field of the variant is to be populated by the constructor.
     * @param o The object to populate the variant with.
     */
    protected Variant11(Token.T6 tok, G o) {
        this.index = 6;
        this.o = o;
    }

    /**
     * Initialize the variant with an object of the eighth type.
     *
     * @param tok This is a marker parameter indicating which
     * field of the variant is to be populated by the constructor.
     * @param o The object to populate the variant with.
     */
    protected Variant11(Token.T7 tok, H o) {
        this.index = 7;
        this.o = o;
    }

    /**
     * Initialize the variant with an object of the ninth type.
     *
     * @param tok This is a marker parameter indicating which
     * field of the variant is to be populated by the constructor.
     * @param o The object to populate the variant with.
     */
    protected Variant11(Token.T8 tok, I o) {
        this.index = 8;
        this.o = o;
    }

    /**
     * Initialize the variant with an object of the tenth type.
     *
     * @param tok This is a marker parameter indicating which
     * field of the variant is to be populated by the constructor.
     * @param o The object to populate the variant with.
     */
    protected Variant11(Token.T9 tok, J o) {
        this.index = 9;
        this.o = o;
    }

    /**
     * Initialize the variant with an object of the eleventh type.
     *
     * @param tok This is a marker parameter indicating which
     * field of the variant is to be populated by the constructor.
     * @param o The object to populate the variant with.
     */
    protected Variant11(Token.T10 tok, K o) {
        this.index = 10;
        this.o = o;
    }

    /**
     * Get the field index that is populated in this variant.
     *
     * @return The index (0-based) of the field of the variant.
     */
    public int getIndex() { return this.index; }

    /**
     * Get the object in the first field of the variant.
     *
     * @return If the field in the variant is populated, the return
     * value is the populated object wrapped in Optional. Otherwise
     * returns Optional.empty().
     */
    public Optional<A> get0() {
        if (this.index == 0) {
            @SuppressWarnings("unchecked") A a = (A)o;
            return Optional.of(a);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Get the object in the second field of the variant.
     *
     * @return If the field in the variant is populated, the return
     * value is the populated object wrapped in Optional. Otherwise
     * returns Optional.empty().
     */
    public Optional<B> get1() {
        if (this.index == 1) {
            @SuppressWarnings("unchecked") B b = (B)o;
            return Optional.of(b);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Get the object in the third field of the variant.
     *
     * @return If the field in the variant is populated, the return
     * value is the populated object wrapped in Optional. Otherwise
     * returns Optional.empty().
     */
    public Optional<C> get2() {
        if (this.index == 2) {
            @SuppressWarnings("unchecked") C c = (C)o;
            return Optional.of(c);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Get the object in the fourth field of the variant.
     *
     * @return If the field in the variant is populated, the return
     * value is the populated object wrapped in Optional. Otherwise
     * returns Optional.empty().
     */
    public Optional<D> get3() {
        if (this.index == 3) {
            @SuppressWarnings("unchecked") D d = (D)o;
            return Optional.of(d);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Get the object in the fifth field of the variant.
     *
     * @return If the field in the variant is populated, the return
     * value is the populated object wrapped in Optional. Otherwise
     * returns Optional.empty().
     */

    public Optional<E> get4() {
        if (this.index == 4) {
            @SuppressWarnings("unchecked") E e = (E)o;
            return Optional.of(e);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Get the object in the sixth field of the variant.
     *
     * @return If the field in the variant is populated, the return
     * value is the populated object wrapped in Optional. Otherwise
     * returns Optional.empty().
     */
    public Optional<F> get5() {
        if (this.index == 5) {
            @SuppressWarnings("unchecked") F f = (F)o;
            return Optional.of(f);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Get the object in the seventh field of the variant.
     *
     * @return If the field in the variant is populated, the return
     * value is the populated object wrapped in Optional. Otherwise
     * returns Optional.empty().
     */
    public Optional<G> get6() {
        if (this.index == 6) {
            @SuppressWarnings("unchecked") G g = (G)o;
            return Optional.of(g);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Get the object in the eighth field of the variant.
     *
     * @return If the field in the variant is populated, the return
     * value is the populated object wrapped in Optional. Otherwise
     * returns Optional.empty().
     */
    public Optional<H> get7() {
        if (this.index == 7) {
            @SuppressWarnings("unchecked") H h = (H)o;
            return Optional.of(h);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Get the object in the ninth field of the variant.
     *
     * @return If the field in the variant is populated, the return
     * value is the populated object wrapped in Optional. Otherwise
     * returns Optional.empty().
     */
    public Optional<I> get8() {
        if (this.index == 8) {
            @SuppressWarnings("unchecked") I i = (I)o;
            return Optional.of(i);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Get the object in the tenth field of the variant.
     *
     * @return If the field in the variant is populated, the return
     * value is the populated object wrapped in Optional. Otherwise
     * returns Optional.empty().
     */
    public Optional<J> get9() {
        if (this.index == 9) {
            @SuppressWarnings("unchecked") J j = (J)o;
            return Optional.of(j);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Get the object in the eleventh field of the variant.
     *
     * @return If the field in the variant is populated, the return
     * value is the populated object wrapped in Optional. Otherwise
     * returns Optional.empty().
     */
    public Optional<K> get10() {
        if (this.index == 10) {
            @SuppressWarnings("unchecked") K k = (K)o;
            return Optional.of(k);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Set the first field of the variant. If any other field
     * is populated, that field is dropped.
     *
     * @param o The object to populate the field with.
     */
    public void set0(A o) {
        this.index = 0;
        this.o = o;
    }

    /**
     * Set the second field of the variant. If any other field
     * is populated, that field is dropped.
     *
     * @param o The object to populate the field with.
     */
    public void set1(B o) {
        this.index = 1;
        this.o = o;
    }

    /**
     * Set the third field of the variant. If any other field
     * is populated, that field is dropped.
     *
     * @param o The object to populate the field with.
     */
    public void set2(C o) {
        this.index = 2;
        this.o = o;
    }

    /**
     * Set the fourth field of the variant. If any other field
     * is populated, that field is dropped.
     *
     * @param o The object to populate the field with.
     */
    public void set3(D o) {
        this.index = 3;
        this.o = o;
    }

    /**
     * Set the fifth field of the variant. If any other field
     * is populated, that field is dropped.
     *
     * @param o The object to populate the field with.
     */
    public void set4(E o) {
        this.index = 4;
        this.o = o;
    }

    /**
     * Set the sixth field of the variant. If any other field
     * is populated, that field is dropped.
     *
     * @param o The object to populate the field with.
     */
    public void set5(F o) {
        this.index = 5;
        this.o = o;
    }

    /**
     * Set the seventh field of the variant. If any other field
     * is populated, that field is dropped.
     *
     * @param o The object to populate the field with.
     */
    public void set6(G o) {
        this.index = 6;
        this.o = o;
    }

    /**
     * Set the eighth field of the variant. If any other field
     * is populated, that field is dropped.
     *
     * @param o The object to populate the field with.
     */
    public void set7(H o) {
        this.index = 7;
        this.o = o;
    }

    /**
     * Set the ninth field of the variant. If any other field
     * is populated, that field is dropped.
     *
     * @param o The object to populate the field with.
     */
    public void set8(I o) {
        this.index = 8;
        this.o = o;
    }

    /**
     * Set the tenth field of the variant. If any other field
     * is populated, that field is dropped.
     *
     * @param o The object to populate the field with.
     */
    public void set9(J o) {
        this.index = 9;
        this.o = o;
    }

    /**
     * Set the eleventh field of the variant. If any other field
     * is populated, that field is dropped.
     *
     * @param o The object to populate the field with.
     */
    public void set10(K o) {
        this.index = 10;
        this.o = o;
    }

    /**
     * Implementation of deep equality.
     * @param other The other object to compare equality against
     * @return True of other is equal to this object.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Variant11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>)) {
            return false;
        }

        Variant11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> asVariant =
            (Variant11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>)other;
        return this.index == asVariant.index && this.o.equals(asVariant.o);
    }
}
