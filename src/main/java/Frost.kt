import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

fun getJson() {
    val BASE_URL = "https://frost.met.no/observations/v0.jsonld"
    var client : OkHttpClient = OkHttpClient.Builder().build()
    val request: Request = Request.Builder().url(BASE_URL.toString()+"?sources=SN50540&referencetime=2021-11-01%2F2021-11-02&elements=air_temperature%20").build()

    val call: Call = client.newCall(request)
    val response: Response = call.execute()
    println(response)
}

fun main (args: Array<String>) {
    var isMeny=true;
    var kontoListe: ArrayList<Konto> = ArrayList()
    while (isMeny) {
        println("1. Hent for Florida")
        println("6. Avslutt")
        var input = readLine()?:""
        when (input) {
            "1" -> getJson()
            "6" -> isMeny=false
        }
    }
}