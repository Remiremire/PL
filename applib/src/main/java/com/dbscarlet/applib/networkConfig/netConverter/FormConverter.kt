package com.dbscarlet.applib.networkConfig.netConverter


import java.io.IOException
import java.util.*

/**
 * Created by Daibing Wang on 2019/1/3.
 */
interface FormConverter<T> {

    @Throws(IOException::class)
    fun objectToFormMap(obj: T): Map<String, String>

    companion object {

        val FIELD_FORM_CONVERTER: FormConverter<Any> = object : FormConverter<Any> {
            @Throws(IOException::class)
            override fun objectToFormMap(obj: Any): Map<String, String> {
                val map = HashMap<String, String>()
                val fields = obj.javaClass.declaredFields
                for (f in fields) {
                    try {
                        f.isAccessible = true
                        val fieldValue = f.get(obj)
                        if (fieldValue != null) {
                            map[f.name] = fieldValue.toString()
                        }
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    }

                }
                return map
            }
        }
    }
}
