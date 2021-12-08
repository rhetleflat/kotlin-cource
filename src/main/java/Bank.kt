import com.google.gson.Gson
import java.io.File

fun lagre(kontoliste : ArrayList<Konto>) {
 var gson : Gson = Gson()
 var json = gson.toJson(kontoliste)
    File("c:/tmp/kontoer.json").writeText(json)
}

fun renteJustering(kontoListe: ArrayList<Konto>) {
    println("Oppgi prosent")
    var percent = readLine()?:""
    kontoListe.forEach{it.adjust(percent.toDouble())
    it.printSaldo()}
}

fun taUtPenger(kontoListe: ArrayList<Konto>) {
    println("Oppgi kontonr")
    var kontonr = readLine()?:""
    var nyKontoListe = kontoListe.filter {konto -> konto.kontonr.equals(kontonr) }
    if (nyKontoListe.isNotEmpty()) {
        println("Oppgi beløp du vil ta ut ")
        var belop = readLine()?:""
        nyKontoListe.first().substract(belop.toDouble())
        nyKontoListe.first().printSaldo()
    }
}

fun settInnPenger(kontoListe: ArrayList<Konto>) {
    println("Oppgi kontonr")
    var kontonr = readLine()?:""
    var nyKontoListe = kontoListe.filter {konto -> konto.kontonr.equals(kontonr) }
    if (nyKontoListe.isNotEmpty()) {
        println("Oppgi innskuddsbeløp ")
        var belop = readLine()?:""
        var inskudd = belop.toDouble()
        nyKontoListe.first().add(inskudd)
        nyKontoListe.first().printSaldo()
    }
}

fun opprettKonto(kontoListe: ArrayList<Konto>) {
   println("Oppgi navn")
    var navn = readLine()?:""
    println("Oppgi adresse")
    var adresse = readLine()?:""
    println("Oppgi postnr")
    var postnr = readLine()?:""
    println("Oppgi poststed")
    var poststed = readLine()?:""
    println("Oppgi kontonr")
    var kontonr = readLine()?:""
    var konto = Konto(navn,adresse,postnr,poststed,kontonr,0.0)
    kontoListe.add(konto)
}

fun main (args: Array<String>) {
    var isMeny=true;
    var kontoListe: ArrayList<Konto> = ArrayList()
    while (isMeny) {
        println("1. Opprett bankkonto")
        println("2. Sett inn penger")
        println("3. Ta ut penger")
        println("4. Gjør rentejustering")
        println("5. Lagre")
        println("6. Avslutt")
        var input = readLine()?:""
        when (input) {
            "1" -> opprettKonto(kontoListe)
            "2" -> settInnPenger(kontoListe)
            "3" -> taUtPenger(kontoListe)
            "4" -> renteJustering(kontoListe)
            "5" -> lagre(kontoListe)
            "6" -> isMeny=false
        }
    }
}