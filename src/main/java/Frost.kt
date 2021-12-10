import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull


fun getJson() {
    val client = OkHttpClient().newBuilder()
        .build()
    val mediaType: MediaType?
    mediaType = "application/x-www-form-urlencoded".toMediaTypeOrNull()
    val body: RequestBody = RequestBody.create(
        mediaType,
        "client_id=5435ddce-8016-48d0-b50f-a535f70be85d&client_secret=f6ee8cf8-f634-45ae-bcd9-48bdbc482e87&grant_type=client_credentials"
    )
    val request: Request = Request.Builder()
        .url("https://frost.met.no/auth/accessToken")
        .method("POST", body)
        .addHeader("Content-Type", "application/x-www-form-urlencoded")
        .build()
    val response = client.newCall(request).execute()

    val resp : String? = response.body?.string()
    println(resp);

    val request2: Request = Request.Builder()
        .url("https://frost.met.no/observations/v0.jsonld?sources=SN50540&referencetime=2021-11-01/2021-11-02&elements=air_temperature")
        .method("GET", null)
        .addHeader("Authorization", "Bearer fquy-hSPXiW3J2QxIMKantomBvF-uY5bj61NRfQ8yj0=|AAAAAAAAFpkAAAF9qmttlQAAAAI=")
        .build()
    val response2 = client.newCall(request2).execute()
    println(response2.body?.string())
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