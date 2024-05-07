package com.linji.mylibrary.serial

import android.util.Log


fun bytesToHexString(src: ByteArray?, size: Int): String? {
    val ret = StringBuilder()
    if (src == null || size <= 0) {
        return null
    }
    for (i in 0 until size) {
        ret.append(String.format("%02x", src[i]))
    }
    return ret.toString().toUpperCase()
}

fun getXORCheck(src: String): String {
    Log.e("TAG,src", src)
    val ret = spitString(src)
    Log.e("TAG,ret", ret.joinToString(transform = { String.format("%02x", it).toUpperCase() }))
    var x = 0
    for (i in ret.indices)
        x = x xor ret[i]
    return String.format("%02x", x).toUpperCase()
}

fun getXORCheck(src: String, split: Int): String {
    val ret = spitString(src)
    Log.e("TAG,ret", ret.joinToString(transform = { String.format("%02x", it).toUpperCase() }))
    var x = 0
    for (i in split until ret.size) {
        x += ret[i]
    }
    return String.format("%04x", x).toUpperCase()
}

fun spitString(src: String): IntArray {
    val ret = IntArray(src.length / 2)
    val tmp = src.toCharArray()
    for (i in 0 until tmp.size / 2) {
        ret[i] = Integer.parseInt(tmp[i * 2] + "" + tmp[i * 2 + 1], 16)
    }
    return ret
}
