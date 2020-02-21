class Foo<T>(x: T)
class Bar<S>

fun <K> foo2(x: K?, y: Foo<Bar<K>>) {}

fun <L> main(x: L?) {
    val f = foo2(x, Foo(Bar()))
}
