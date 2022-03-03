package hr.algebra.ivanabilic.prcoessor

import java.lang.Exception

enum class KotlinTypeConverter(private val javaType:String,private val kotlinType:String) {

    ContentValues("android.content.ContentValues","ContentValues?"),ArrayString("java.lang.String[]","Array<String>?");

    companion object{
        fun getKotlinType(javaType: String):String{
            for (value in values()){
                if (value.javaType==javaType){
                    return value.kotlinType
                }
            }
            return javaType
        }
    }

}