package com.rmnivnv.cryptomoon.utils

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.rmnivnv.cryptomoon.model.*

/**
 * Created by rmnivnv on 12/07/2017.
 */

fun getCoinDisplayBodyFromJson(jsonObject: JsonObject, map: Map<String, ArrayList<String>>): ArrayList<DisplayCoin> {
    val result: ArrayList<DisplayCoin> = ArrayList()
    val display: JsonElement
    val fromObjectsList: HashMap<String, JsonElement> = HashMap()
    if (jsonObject.has(DISPLAY)) {
        display = jsonObject[DISPLAY]
        for ((key, value) in map) {
            if (key == FSYMS) {
                value.forEach {
                    if (display.asJsonObject.has(it)) {
                        fromObjectsList.put(it, display.asJsonObject[it])
                    }
                }
            }

        }
        for ((key, value) in map) {
            if (key == TSYMS) {
                value.forEach { toSymbol ->
                    for ((keyFrom, valueFrom) in fromObjectsList) {
                        if (valueFrom.asJsonObject.has(toSymbol)) {
                            val body: DisplayCoin = Gson().fromJson(valueFrom.asJsonObject[toSymbol], DisplayCoin::class.java)
                            body.from = keyFrom
                            body.to = toSymbol
                            result.add(body)
                        }
                    }
                }
            }
        }
    }
    return result
}

fun getAllCoinsFromJson(response: AllCoinsResponse): ArrayList<InfoCoin> {
    val result: ArrayList<InfoCoin> = ArrayList()
    val jsonObject = response.data
    jsonObject.entrySet().forEach {
        val coin = Gson().fromJson(it.value, InfoCoin::class.java)
        coin.imageUrl = response.baseImageUrl + coin.imageUrl
        result.add(coin)
    }
    return result
}