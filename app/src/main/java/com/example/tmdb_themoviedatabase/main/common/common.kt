package com.example.tmdb_themoviedatabase.main.common

import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

object EnumAsOrdinalToStringConverter : Converter<Enum<*>, String> {
    override fun convert(value: Enum<*>): String =
            value.ordinal.toString()
}

class EnumAsOrdinalToStringConverterFactory : Converter.Factory() {
    override fun stringConverter(
            type: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit
    ): Converter<*, String>? = if (type is Class<*> && type.isEnum) {
        EnumAsOrdinalToStringConverter
    } else {
        null
    }
}