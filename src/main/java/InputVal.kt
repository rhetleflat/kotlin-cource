fun main (args: Array<String>) {


    var isnumber = false
    while (!isnumber) {
        try {
            println("Skriv inn et tall")
            var input = readLine()?:""
            var tall = input.toInt()
            isnumber=true
            println("Takk for tallet $tall")
        } catch (e: Exception) {
            println("Dette var ikke et tall. Oppgi tall")

        }
    }


}