package hr.algebra.ivanabilic.prcoessor

import java.lang.StringBuilder
import java.util.stream.Collectors
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement


private val IMPORT = "import "
val NEW_LINE = "\n\n"

fun addImport(vararg import: String): String {
    val sb = StringBuilder()
    import.asSequence().forEach {
        sb.append(IMPORT).append(it).append("\n")
    }
    return sb.toString()
}

fun addProperty(name: String, type: String) = "private $type $name;\n\n"

fun addMethod(name: String, job: String,returnType: String, vararg parametes: String): String {
    val sb = StringBuilder()
    sb.append("@Override\n  public $returnType $name(").append("\n")
    parametes.asSequence().forEach {
        sb.append(it).append(",")
    }
    sb.setLength(sb.length - 1)
    sb.append("){\n")
    sb.append("return ")
    sb.append(job+";\n}")
    return sb.toString()
}

fun addConstructor(name: String, body: String,vararg parameters: String): String {
    val sb = StringBuilder()
    sb.append("public $name(").append("\n")
    parameters.asSequence().forEach {
        sb.append(it).append(",")
    }
    sb.setLength(sb.length - 1)
    sb.append("){\n")
    sb.append(body+"\n}")
    return sb.toString()
}

fun addMethodWithOverride(name: String, job: String,returnType: String, vararg parametes: String): String {
    val sb = StringBuilder()
    sb.append("@Override\n  public $returnType $name(").append("\n")
    parametes.asSequence().forEach {
        sb.append(it).append(",")
    }
    sb.setLength(sb.length - 1)
    sb.append("){\n")
    sb.append(job+";\n}")
    return sb.toString()
}

fun addMethodWithBody(
    name: String,
    job: String,
    returnType: String,
    vararg parametes: String,
): String {
    val sb = StringBuilder()
    sb.append("public $returnType $name(").append("\n")
    parametes.asSequence().forEach {
        sb.append(it).append(",")
    }
    sb.setLength(sb.length - 1)
    sb.append("){\n")
    sb.append(job+"\n}")
    return sb.toString()
}

fun addPropertyVolitile(name: String, type: String) = " @Volatile private $type $name;\n\n"

fun makeItToStirng(value: String) = "\"" + value + "\""

fun getTypeProperties(it:Element) :List<String> {
    val packageElements=it as ExecutableElement

    return packageElements.parameters.asSequence().map { prop->
       prop.asType().toString() +" "+ prop.toString()
    }.toCollection(mutableListOf<String>())
}
fun getNameProperties(it:Element) :List<String> {
    val packageElements=it as ExecutableElement
    return packageElements.parameters.asSequence().map { prop->
        prop.toString()
    }.toCollection(mutableListOf<String>())
}

fun getReturnParameter(it: Element):String{
    val packageElements=it as ExecutableElement
    return packageElements.returnType.toString()

}

fun getNameOfElement(processingEnv:ProcessingEnvironment,it:Element,index:Int)
    =processingEnv.elementUtils.getPackageOf(it).enclosedElements[0].simpleName.toString()


