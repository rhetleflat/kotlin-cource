data class Konto (val navn: String,
                  val adresse: String,
                  val postnr: String,
                  val poststed: String,
                  val kontonr: String,
                  var saldo: Double)
{
    fun add(belop:Double) {
        saldo+=belop
    }
    fun substract(belop:Double) {
        saldo-=belop
    }
    fun adjust(percent:Double) {
        saldo += saldo * (percent/100)
    }
    fun printSaldo() {
        println("kontonr $kontonr beløp $saldo")
    }
}