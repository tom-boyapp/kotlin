// FILE: Bar.java
class Bar {
    static void foo(String x) {

    }
}

// FILE: main.kt
class A {
    var x: String? = null
}

fun main() {
    val y = A()
    y.x = ""
    Bar.foo(y.x)
}