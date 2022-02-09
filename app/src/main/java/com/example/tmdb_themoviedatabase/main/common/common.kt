package com.example.tmdb_themoviedatabase.main.common

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavDirections
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

/**
 * safely navigate
 * this prevents navigation errors when tapping a button that leads to an navigate action multiple times
 * @param direction the navDirection to navigate to
 */
fun NavController.doNavigate(direction: NavDirections): Boolean {
    Log.d("doNavigate", "-")
    currentDestination?.let { currDestination ->
        currDestination.getAction(direction.actionId)?.let { thisAction ->
            if(thisAction.destinationId != currDestination.id) {
                Log.d("doNavigate", "+")
                navigate(direction)
                return true
            }
        }
    }
    return false
}