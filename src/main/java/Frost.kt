import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.internal.filterList


fun getToken(client: OkHttpClient): TokenObj {

    val mediaType: MediaType? = "application/x-www-form-urlencoded".toMediaTypeOrNull()
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

    val resp: String? = response.body?.string()
    var gson: Gson = Gson()
    val tokenObj: TokenObj = gson.fromJson<TokenObj>(resp, TokenObj::class.java)
    println(tokenObj.access_token);
    return tokenObj
}

fun getAverage(token: TokenObj, client: OkHttpClient) {
    val request2: Request = Request.Builder()
        .url("https://frost.met.no/observations/v0.jsonld?sources=SN50540,SN50500&referencetime=2021-01-01/2021-12-31&elements=air_temperature")
        .method("GET", null)
        .addHeader("Authorization", "Bearer " + token.access_token)
        .build()
    val response2 = client.newCall(request2).execute()
    var gson: Gson = Gson()
    val obs = gson.fromJson<ObservationResponse>(response2.body?.string(), ObservationResponse::class.java)
    var obslist = obs.data.filter { it.sourceId == "SN50540:0" }
    var averageTemp: Double = 0.0
    for (obsPerMonth in obslist.groupBy({ it.referenceTime.month })) {
        print("Måned er ${obsPerMonth.key + 1}: ")
        averageTemp = 0.01
        for (data in obsPerMonth.value) {
            for (o in data.observations) {
                averageTemp += o.value
            }
        }
        averageTemp = averageTemp / obsPerMonth.value.size
        println(averageTemp)
    }
}

fun getHighestTemp(token: TokenObj, client: OkHttpClient) {
    val request2: Request = Request.Builder()
        .url("https://frost.met.no/observations/v0.jsonld?sources=SN50540,SN50500&referencetime=2021-01-01/2021-12-31&elements=air_temperature")
        .method("GET", null)
        .addHeader("Authorization", "Bearer " + token.access_token)
        .build()
    val response2 = client.newCall(request2).execute()
    var gson: Gson = Gson()
    val obs = gson.fromJson<ObservationResponse>(response2.body?.string(), ObservationResponse::class.java)
    var obslist = obs.data.filter { it.sourceId == "SN50540:0" }
    var hightemp: Double = 0.0
    for (obsPerMonth in obslist.groupBy({ it.referenceTime.month })) {
        print("Måned er ${obsPerMonth.key + 1}: ")
        hightemp = -200.00
        for (data in obsPerMonth.value) {
            for (o in data.observations) {
                if (o.value > hightemp) {
                    hightemp = o.value
                }
            }
        }
        println(hightemp)
    }
}

fun getTotalPrecipitation(token: TokenObj, client: OkHttpClient) {
    val request2: Request = Request.Builder()
        .url("https://frost.met.no/observations/v0.jsonld?sources=SN50540,SN50500&referencetime=2020-01-01/2020-12-31&elements=sum(precipitation_amount P1D)")
        .method("GET", null)
        .addHeader("Authorization", "Bearer " + token.access_token)
        .build()
    val response2 = client.newCall(request2).execute()
    var gson: Gson = Gson()
    val obs = gson.fromJson<ObservationResponse>(response2.body?.string(), ObservationResponse::class.java)
    var hightemp: Double = 0.0
    for (obsPerSource in obs.data.groupBy { it.sourceId }) {
        print("Sted er ${obsPerSource.key}: ")
        var totalPrecipitation = 0.00
        for (data in obsPerSource.value) {
            for (o in data.observations.filter { it.timeOffset == "PT18H" }) {
                totalPrecipitation += o.value
            }
        }
        println(totalPrecipitation)
    }
}

fun getTotalPrecipitationMonth(token: TokenObj, client: OkHttpClient) {
    val request2: Request = Request.Builder()
        .url("https://frost.met.no/observations/v0.jsonld?sources=SN50540,SN50500&referencetime=2020-01-01/2020-12-31&elements=sum(precipitation_amount P1D)")
        .method("GET", null)
        .addHeader("Authorization", "Bearer " + token.access_token)
        .build()
    val response2 = client.newCall(request2).execute()
    var gson: Gson = Gson()
    val obs = gson.fromJson<ObservationResponse>(response2.body?.string(), ObservationResponse::class.java)
    var hightemp: Double = 0.0
    for (obsPerMonth in obs.data.filter { it.sourceId == "SN50500:0" }.groupBy { it.referenceTime.month }) {
        print("Måned er ${obsPerMonth.key + 1}: ")
        var totalPrecipitation = 0.00
        for (obsSource in obsPerMonth.value) {
            for (o in obsSource.observations.filter { it.timeOffset == "PT18H" }) {
                totalPrecipitation += o.value
            }
        }
        println(totalPrecipitation)
    }
}

fun getDaysWithWind(token: TokenObj, client: OkHttpClient) {
    val request2: Request = Request.Builder()
        .url("https://frost.met.no/observations/v0.jsonld?sources=SN50540,SN50500&referencetime=2020-01-01/2020-12-31&elements=max(wind_speed P1D)")
        .method("GET", null)
        .addHeader("Authorization", "Bearer " + token.access_token)
        .build()
    val response2 = client.newCall(request2).execute()
    var gson: Gson = Gson()
    val obs = gson.fromJson<ObservationResponse>(response2.body?.string(), ObservationResponse::class.java)
    var hightemp: Double = 0.0
    for (observation in obs.data.sortedByDescending { it.observations.maxOf { it.value } }.take(10)) {
        println("${observation.sourceId} ${observation.referenceTime} ${observation.observations.maxOf { it.value }}")
    }
}

fun getDaysWithMostRain(token: TokenObj, client: OkHttpClient) {
    val request2: Request = Request.Builder()
        .url("https://frost.met.no/observations/v0.jsonld?sources=SN50540&referencetime=2020-01-01/2020-12-31&elements=sum(precipitation_amount P1D)")
        .method("GET", null)
        .addHeader("Authorization", "Bearer " + token.access_token)
        .build()
    val response2 = client.newCall(request2).execute()
    var gson = Gson()
    val obs = gson.fromJson(response2.body?.string(), ObservationResponse::class.java)
    for (data in obs.data) {
        data.observations = data.observations.filter { it.timeOffset == "PT6H" }
    }
    for (observation in obs.data.filter { it.sourceId == "SN50540:0" }
        .sortedByDescending { it.observations.maxOf { it.value } }.take(10)) {
        println("${observation.sourceId} ${observation.referenceTime} ${observation.observations.maxOf { it.value }}")
    }
}

fun rain2021vs2020(token: TokenObj, client: OkHttpClient) {
    val request2: Request = Request.Builder()
        .url("https://frost.met.no/observations/v0.jsonld?sources=SN50540&referencetime=2020-01-01/2021-11-30&elements=sum(precipitation_amount P1D)")
        .method("GET", null)
        .addHeader("Authorization", "Bearer " + token.access_token)
        .build()
    val response2 = client.newCall(request2).execute()
    var gson = Gson()
    val obs = gson.fromJson(response2.body?.string(), ObservationResponse::class.java)

    val test = obs.data.first().referenceTime
    println(test)
    val list2020 = obs.data.filter { it.referenceTime.year == (2020 - 1900) }.filter { it.referenceTime.month < 11 }
    var sum2020 = 0.0
    for (data in list2020) {
        sum2020 += data.observations.filter { it.timeOffset == "PT18H" }.sumOf { it.value }
    }
    val list2021 = obs.data.filter { it.referenceTime.year == (2021 - 1900) }.filter { it.referenceTime.month < 11 }
    var sum2021 = 0.0
    for (data in list2021) {
        sum2021 += data.observations.filter { it.timeOffset == "PT18H" }.sumOf { it.value }
    }
    println("Total nedbør første 11 måneder 2020: $sum2020")
    println("Total nedbør første 11 måneder 2021: $sum2021")
}

fun averageAndMedianFlorida2020(token: TokenObj, client: OkHttpClient) {
    val request2: Request = Request.Builder()
        .url("https://frost.met.no/observations/v0.jsonld?sources=SN50540&referencetime=2021-01-01/2021-12-31&elements=air_temperature")
        .method("GET", null)
        .addHeader("Authorization", "Bearer " + token.access_token)
        .build()
    val response2 = client.newCall(request2).execute()
    var gson: Gson = Gson()
    val obs = gson.fromJson(response2.body?.string(), ObservationResponse::class.java)
    for (obsPerMonth in obs.data.filter { it.sourceId == "SN50540:0" }.groupBy { it.referenceTime.month }) {
        val tempAverage = obsPerMonth.value.map { it.observations.first().value }.average()
        val tempMedian = obsPerMonth.value.map { it.observations.first().value }.sorted()[obsPerMonth.value.size / 2]
        println("Måned ${obsPerMonth.key + 1}: gjennomsnitt: $tempAverage, median: $tempMedian")
    }
}

fun compareSummerMonths(token: TokenObj, client: OkHttpClient) {
    val request2: Request = Request.Builder()
        .url("https://frost.met.no/observations/v0.jsonld?sources=SN50540&referencetime=2020-01-01/2021-12-31&elements=air_temperature")
        .method("GET", null)
        .addHeader("Authorization", "Bearer " + token.access_token)
        .build()
    val response2 = client.newCall(request2).execute()
    var gson: Gson = Gson()

    val obs = gson.fromJson(response2.body?.string(), ObservationResponse::class.java)

    val list2020 = obs.data.filter { it.referenceTime.year == (2020 - 1900) }.filter { it.referenceTime.month in 5..7 }
    val list2021 = obs.data.filter { it.referenceTime.year == (2021 - 1900) }.filter { it.referenceTime.month in 5..7 }
    val printAverageAndMedian = { yearlySummerTemps: List<Data> ->
        for (obsPerMonth in yearlySummerTemps.filter { it.sourceId == "SN50540:0" }
            .groupBy { it.referenceTime.month }) {
            val tempAverage = obsPerMonth.value.map { it.observations.first().value }.average()
            val tempMedian =
                obsPerMonth.value.map { it.observations.first().value }.sorted()[obsPerMonth.value.size / 2]
            println("Måned ${obsPerMonth.key + 1}: gjennomsnitt: $tempAverage, median: $tempMedian")
        }
    }

    printAverageAndMedian(list2020)
    printAverageAndMedian(list2021)
}

fun findTypeNumber(token: TokenObj, client: OkHttpClient) {
    val request2: Request = Request.Builder()
        .url("https://frost.met.no/observations/v0.jsonld?sources=SN50540&referencetime=2021-01-01/2021-12-31&elements=air_temperature")
        .method("GET", null)
        .addHeader("Authorization", "Bearer " + token.access_token)
        .build()
    val response2 = client.newCall(request2).execute()
    val gson = Gson()

    val obs = gson.fromJson(response2.body?.string(), ObservationResponse::class.java)
    val getTypeNumber = { data: List<Data> ->
        val obsList = data.map { it.observations.first() }
        val mapByTemperature = obsList.groupBy { it.value }
        val typeNumberEntry = mapByTemperature.entries.sortedByDescending { it.value.size }
        println(typeNumberEntry.first().key)
    }
    getTypeNumber(obs.data)
}

fun main(args: Array<String>) {
    val client = OkHttpClient().newBuilder()
        .build()
    var isMeny = true;
    val token = getToken(client)
    var kontoListe: ArrayList<Konto> = ArrayList()
    while (isMeny) {
        println("1. Hent for Florida")
        println("2. Høyeste temperatur for hver måned Florida")
        println("3. Total nedbør Florida og Flesland 2020")
        println("4. Total nedbør for Flesland 2020 per måned")
        println("5. 10 dager med mest vind på flesland og Florida")
        println("6. 10 dager med mest regn på flesland og Florida")
        println("7. Regn første 11 måneder 2021 vs 2020")
        println("8. Snitt og median per måned for Florida 2020")
        println("9. Sammenlign sommermåned temperatur for Flesland i 2020 og 2021")
        println("10. Finn typetall for temperatur for Florida i 2021")
        println("11. Avslutt")
        var input = readLine() ?: ""
        when (input) {
            "1" -> {
                getAverage(token, client)
            }
            "2" -> {
                getHighestTemp(token, client)
            }
            "3" -> getTotalPrecipitation(token, client)
            "4" -> getTotalPrecipitationMonth(token, client)
            "5" -> getDaysWithWind(token, client)
            "6" -> getDaysWithMostRain(token, client)
            "7" -> rain2021vs2020(token, client)
            "8" -> averageAndMedianFlorida2020(token, client)
            "9" -> compareSummerMonths(token, client)
            "10" -> findTypeNumber(token, client)
            "11" -> isMeny = false
        }
    }
}









