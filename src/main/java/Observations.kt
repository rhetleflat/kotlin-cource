data class Observations (val itemsPerPage : Int,
    val data : List<Data>)


/**
 *
 * {
"@context": "https://frost.met.no/schema",
"@type": "ObservationResponse",
"apiVersion": "v0",
"license": "https://creativecommons.org/licenses/by/3.0/no/",
"createdAt": "2021-12-06T16:23:02Z",
"queryTime": 0.595,
"currentItemCount": 24,
"itemsPerPage": 24,
"offset": 0,
"totalItemCount": 24,
"currentLink": "https://frost.met.no/observations/v0.jsonld?sources=SN50540&referencetime=2021-11-01%2F2021-11-02&elements=air_temperature%20",
"data": [
{
"sourceId": "SN50540:0",
"referenceTime": "2021-11-01T00:00:00.000Z",
"observations": [
{
"elementId": "air_temperature",
"value": 11.4,
"unit": "degC",
"level": {
"levelType": "height_above_ground",
"unit": "m",
"value": 2
},
 */