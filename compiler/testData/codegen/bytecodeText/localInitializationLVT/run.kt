// IGNORE_BACKEND: JVM_IR
// TODO KT-36648 Captured variables not optimized in JVM_IR
// TODO KT-36812 Generate proper lifetime intervals for local variables in JVM_IR

fun test(): Char {
    val c: Char
    run {
        c = ' '
        println(c)
    }
    return c
}

// The first on declaration, the other on initialization
// 2 ISTORE 0
// 1 LOCALVARIABLE c C L1 L.* 0