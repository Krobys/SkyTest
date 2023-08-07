package com.krobys.skytest.retrofit.resultCallAdapter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
class ResultTypeAdapter<T>(private val adapter: TypeAdapter<T>) : TypeAdapter<Result<T>>() {

    companion object {

        fun getFactory() = object : TypeAdapterFactory {
            override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
                val rawType = type.rawType as Class<*>
                if (rawType != Result::class.java) {
                    return null
                }
                val parameterizedType = type.type as ParameterizedType
                val actualType = parameterizedType.actualTypeArguments[0]
                val adapter = gson.getAdapter(TypeToken.get(actualType))
                return ResultTypeAdapter(adapter) as TypeAdapter<T>
            }
        }
    }

    override fun write(out: com.google.gson.stream.JsonWriter?, value: Result<T>?) {
        if (value?.isSuccess == true){
            value.fold(onFailure = {
                out?.nullValue()
            }, onSuccess = {
                adapter.write(out, it)
            })

        }

    }

    override fun read(input: com.google.gson.stream.JsonReader?): Result<T> {
        val peek = input?.peek()
        return if (peek != JsonToken.NULL) {
            Result.runCatching { adapter.read(input) }
        } else {
            input.nextNull()
            Result.runCatching { adapter.read(input) }
        }
    }

}