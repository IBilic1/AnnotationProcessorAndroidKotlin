package hr.algebra.ivanabilic.prcoessor

import java.lang.StringBuilder
import javax.lang.model.element.Element

class ClassBuilder {

    private var clazz:StringBuilder = StringBuilder()

    fun build()=clazz.append("}").toString()

    fun setConstrucotr(name:String,body: String,vararg parameters: String){
        clazz.append(addConstructor(name,body,*parameters))
    }

    fun  setClass(name:String,asociation:String):ClassBuilder{
        clazz.append("class ${name + "_impl"} $asociation $name {"+NEW_LINE)
        return this
    }

    fun  setProperty(name:String,value:String):ClassBuilder{
        clazz.append(addProperty(name,value))
        return this
    }
    fun  setPropertyVolitile(name:String,value:String):ClassBuilder{
        clazz.append(addPropertyVolitile(name,value))
        return this
    }

    fun setMethodWithOverrride(method:Element, body:String):ClassBuilder{
        clazz.append(addMethodWithOverride(
            method.simpleName.toString(),
            body,
            getReturnParameter(method),
            *getTypeProperties(method).toTypedArray()))
            .append(NEW_LINE)
        return this
    }

    fun setMethod(name:String, body:String,returnType:String,vararg params:String):ClassBuilder{
        clazz.append(addMethodWithBody(
           name,
            body,
            returnType,
            *params))
            .append(NEW_LINE)
        return this
    }


    fun setProperty(property:String):ClassBuilder{
        clazz.append(property)
        return this
    }


}