import kotlin.random.Random

fun main (args: Array<String>) {

    var tall = Random.nextInt(0,100)
    var isCorrectNumber = false
    var antall = 0
    while (!isCorrectNumber) {
        try {
            println("Gjett et tall mellom 0 og 100")
            var input = readLine()?:""
            antall++
            var inputTall = input.toInt()
            if (inputTall < tall) {
                println("Tallet du gjettet er for lavt")
            } else if (inputTall > tall) {
                println("Tallet du gjettet er for høyt")
            } else {
                println("Tallet er rett på $antall forsøk")
                isCorrectNumber=true
            }
        } catch (e: Exception) {
            println("Dette var ikke et tall. Oppgi tall")

        }
    }


}