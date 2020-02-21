// !DIAGNOSTICS: -UNUSED_PARAMETER, -SENSELESS_COMPARISON, -DEBUG_INFO_SMARTCAST

fun <T: Any?> test1(t: Any?): Any {
    return t <!UNCHECKED_CAST!>as T<!> ?: ""
}
