fun main (args: Array<String>) {
    var input = " Dette er test en. "
    input = input.trim()
    println(input)
    val input2 = input.substring(0,9)+input.substring(14,16)+input.substring(8,13)
    println(input2.uppercase())
}