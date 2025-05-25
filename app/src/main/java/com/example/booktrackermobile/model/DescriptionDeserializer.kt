package com.example.booktrackermobile.model

import com.google.gson.*
import java.lang.reflect.Type

class DescriptionDeserializer : JsonDeserializer<String?> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): String? {
        return when {
            json == null -> null
            json.isJsonPrimitive -> json.asString
            json.isJsonObject -> json.asJsonObject["value"]?.asString
            else -> null
        }
    }
}
