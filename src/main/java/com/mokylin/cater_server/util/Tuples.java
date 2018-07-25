package com.mokylin.cater_server.util;

import java.util.Objects;

public class Tuples {
    public static class Tuple2<T1, T2> {
        private final T1 t1;
        private final T2 t2;

        private Tuple2(T1 t1, T2 t2) {
            this.t1 = t1;
            this.t2 = t2;
        }

        public static <T1, T2> Tuple2<T1, T2> of(T1 t1, T2 t2) {
            return new Tuple2<>(t1, t2);
        }

        public T1 _1() {
            return t1;
        }

        public T2 _2() {
            return t2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;
            return Objects.equals(t1, tuple2.t1) && Objects.equals(t2, tuple2.t2);
        }

        @Override
        public int hashCode() {

            return Objects.hash(t1, t2);
        }
    }

    public static class Tuple3<T1, T2, T3> {
        private final T1 t1;
        private final T2 t2;
        private final T3 t3;

        private Tuple3(T1 t1, T2 t2, T3 t3) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
        }

        public static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 t1, T2 t2, T3 t3) {
            return new Tuple3<>(t1, t2, t3);
        }

        public T1 _1() {
            return t1;
        }

        public T2 _2() {
            return t2;
        }

        public T3 _3() {
            return t3;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tuple3<?, ?, ?> tuple3 = (Tuple3<?, ?, ?>) o;
            return Objects.equals(t1, tuple3.t1) && Objects.equals(t2, tuple3.t2) &&
                    Objects.equals(t3, tuple3.t3);
        }

        @Override
        public int hashCode() {

            return Objects.hash(t1, t2, t3);
        }
    }

    public static class Tuple4<T1, T2, T3, T4> {
        private final T1 t1;
        private final T2 t2;
        private final T3 t3;
        private final T4 t4;

        private Tuple4(T1 t1, T2 t2, T3 t3, T4 t4) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
        }

        public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(T1 t1, T2 t2, T3 t3, T4 t4) {
            return new Tuple4<>(t1, t2, t3, t4);
        }

        public T1 _1() {
            return t1;
        }

        public T2 _2() {
            return t2;
        }

        public T3 _3() {
            return t3;
        }

        public T4 _4() {
            return t4;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tuple4<?, ?, ?, ?> tuple4 = (Tuple4<?, ?, ?, ?>) o;
            return Objects.equals(t1, tuple4.t1) && Objects.equals(t2, tuple4.t2) &&
                    Objects.equals(t3, tuple4.t3) && Objects.equals(t4, tuple4.t4);
        }

        @Override
        public int hashCode() {

            return Objects.hash(t1, t2, t3, t4);
        }
    }

    public static class Tuple5<T1, T2, T3, T4, T5> {
        private final T1 t1;
        private final T2 t2;
        private final T3 t3;
        private final T4 t4;
        private final T5 t5;

        private Tuple5(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
        }

        public static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> of(T1 t1, T2 t2, T3 t3, T4 t4,
                T5 t5) {
            return new Tuple5<>(t1, t2, t3, t4, t5);
        }

        public T1 _1() {
            return t1;
        }

        public T2 _2() {
            return t2;
        }

        public T3 _3() {
            return t3;
        }

        public T4 _4() {
            return t4;
        }

        public T5 _5() {
            return t5;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tuple5<?, ?, ?, ?, ?> tuple5 = (Tuple5<?, ?, ?, ?, ?>) o;
            return Objects.equals(t1, tuple5.t1) && Objects.equals(t2, tuple5.t2) &&
                    Objects.equals(t3, tuple5.t3) && Objects.equals(t4, tuple5.t4) &&
                    Objects.equals(t5, tuple5.t5);
        }

        @Override
        public int hashCode() {

            return Objects.hash(t1, t2, t3, t4, t5);
        }
    }

    public static class Tuple6<T1, T2, T3, T4, T5, T6> {
        private final T1 t1;
        private final T2 t2;
        private final T3 t3;
        private final T4 t4;
        private final T5 t5;
        private final T6 t6;

        private Tuple6(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
        }

        public static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> of(T1 t1, T2 t2,
                T3 t3, T4 t4, T5 t5, T6 t6) {
            return new Tuple6<>(t1, t2, t3, t4, t5, t6);
        }

        public T1 _1() {
            return t1;
        }

        public T2 _2() {
            return t2;
        }

        public T3 _3() {
            return t3;
        }

        public T4 _4() {
            return t4;
        }

        public T5 _5() {
            return t5;
        }

        public T6 _6() {
            return t6;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tuple6<?, ?, ?, ?, ?, ?> tuple6 = (Tuple6<?, ?, ?, ?, ?, ?>) o;
            return Objects.equals(t1, tuple6.t1) && Objects.equals(t2, tuple6.t2) &&
                    Objects.equals(t3, tuple6.t3) && Objects.equals(t4, tuple6.t4) &&
                    Objects.equals(t5, tuple6.t5) && Objects.equals(t6, tuple6.t6);
        }

        @Override
        public int hashCode() {

            return Objects.hash(t1, t2, t3, t4, t5, t6);
        }
    }

    public static class Tuple7<T1, T2, T3, T4, T5, T6, T7> {
        private final T1 t1;
        private final T2 t2;
        private final T3 t3;
        private final T4 t4;
        private final T5 t5;
        private final T6 t6;
        private final T7 t7;

        private Tuple7(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
            this.t7 = t7;
        }

        public static <T1, T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> of(T1 t1,
                T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
            return new Tuple7<>(t1, t2, t3, t4, t5, t6, t7);
        }

        public T1 _1() {
            return t1;
        }

        public T2 _2() {
            return t2;
        }

        public T3 _3() {
            return t3;
        }

        public T4 _4() {
            return t4;
        }

        public T5 _5() {
            return t5;
        }

        public T6 _6() {
            return t6;
        }

        public T7 _7() {
            return t7;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tuple7<?, ?, ?, ?, ?, ?, ?> tuple7 = (Tuple7<?, ?, ?, ?, ?, ?, ?>) o;
            return Objects.equals(t1, tuple7.t1) && Objects.equals(t2, tuple7.t2) &&
                    Objects.equals(t3, tuple7.t3) && Objects.equals(t4, tuple7.t4) &&
                    Objects.equals(t5, tuple7.t5) && Objects.equals(t6, tuple7.t6) &&
                    Objects.equals(t7, tuple7.t7);
        }

        @Override
        public int hashCode() {

            return Objects.hash(t1, t2, t3, t4, t5, t6, t7);
        }
    }
}
