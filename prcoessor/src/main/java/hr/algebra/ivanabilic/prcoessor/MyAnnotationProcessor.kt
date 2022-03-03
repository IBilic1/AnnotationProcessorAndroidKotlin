package hr.algebra.ivanabilic.prcoessor

import hr.algebra.ivanabilic.annotations.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import javax.tools.FileObject
import javax.tools.StandardLocation

private const val PACKAGE = "hr.algebra.ivanabilic.annotations"

@SupportedAnnotationTypes("$PACKAGE.Dao", "$PACKAGE.Database",
    "$PACKAGE.Query", "$PACKAGE.Update",
    "$PACKAGE.Insert", "$PACKAGE.Delete")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class MyAnnotationProcessor : AbstractProcessor() {


    override fun process(
        set: MutableSet<out TypeElement>?,
        roundEnviroment: RoundEnvironment?,
    ): Boolean {

        var tableName: String = ""
        var interfaceImplementationName: String = ""

        if (!roundEnviroment?.processingOver()!!) {

            roundEnviroment?.getElementsAnnotatedWith(Dao::class.java).asSequence().forEach {
                val annotation = it.getAnnotation(Dao::class.java);
                tableName = annotation.tableName
                interfaceImplementationName = it.simpleName.toString()

                generateCode(it.simpleName.toString(), processingEnv.filer, tableName,
                    processingEnv.elementUtils.getPackageOf(it).toString(), it)
            }

            roundEnviroment?.getElementsAnnotatedWith(Database::class.java).asSequence().forEach {
                val annotation = it.getAnnotation(Database::class.java);
                val createStatment = annotation.createStatement



                generateCodeForDatabase(it.simpleName.toString(),
                    processingEnv.filer,
                    createStatment,
                    tableName,
                    interfaceImplementationName,
                    processingEnv.elementUtils.getPackageOf(it).toString())
            }

            return true
        }

        return false

    }

    private fun <T : Annotation> checkForExistence(
        clazz: Class<T>,
        roundEnvironment: RoundEnvironment,
    ) = roundEnvironment?.getElementsAnnotatedWith(clazz).size != 0


    private fun generateCodeForDatabase(
        qualifiedClassName: String,
        filer: Filer?,
        createStatment: String,
        tableName: String,
        interfaceName: String,
        package_: String,
    ) {

        val createStatement = makeItToStirng(createStatment)
        val dropStatement = makeItToStirng("drop table $tableName")
        val jfo =
            filer!!.createResource(StandardLocation.SOURCE_OUTPUT, "", "${qualifiedClassName}_impl.java")
        writeDatabaseInFile(jfo,
            qualifiedClassName,
            createStatement,
            dropStatement,
            interfaceName,
            package_)


    }

    private fun writeDatabaseInFile(
        jfo: FileObject,
        qualifiedClassName: String,
        createStatement: String,
        dropStatement: String,
        interfaceName: String,
        package_: String,
    ) {

        val instance = "INSTANCE"
        jfo.openWriter().use {
            it.write("package " + package_ +";"+ NEW_LINE)
            it.write(addImport("android.database.sqlite.SQLiteDatabase;",
                "android.content.Context;","kotlin.jvm.Volatile;") + NEW_LINE)


            val clazzBuilder = ClassBuilder()
            clazzBuilder.setClass(qualifiedClassName,"extends")
            clazzBuilder.setProperty("context", "Context")
            clazzBuilder.setPropertyVolitile(instance, interfaceName)
            clazzBuilder.setConstrucotr(qualifiedClassName + "_impl", "super(context); this.context=context;", "Context context")


            clazzBuilder.setMethod("onCreate",
                "db.execSQL($createStatement);",
                "void",
                "SQLiteDatabase db")
            clazzBuilder.setMethod("onUpgrade",
                "db.execSQL($dropStatement);\nonCreate(db);",
                "void",
                "SQLiteDatabase db",
                "int oldVersion",
                "int newVersion")
            clazzBuilder.setMethod("get$interfaceName", " if ($instance != null) {\n" +
                    "      return $instance;\n" +
                    "    } else {\n" +
                    "      synchronized(this) {\n" +
                    "        if($instance == null) {\n" +
                    "          $instance = new $package_.${interfaceName}_impl(this);\n" +
                    "        }\n" +
                    "        return $instance;\n" +
                    "      }\n" +
                    "    }", interfaceName)

            it.write(clazzBuilder.build())
        }

    }

    private val selection = "selection: String?"
    private val selectionArgs = "selectionArgs: Array<String>?"
    private val projection = "projection: Array<String>?"
    private val sortOrder = "sortOrder: String?"

    private fun generateCode(
        qualifiedClassName: String,
        filer: Filer?,
        tableName: String,
        package_: String,
        it: Element,
    ) {
        val stringTableName = "\"" + tableName + "\""
        val jfo =
            filer!!.createResource(StandardLocation.SOURCE_OUTPUT,
                "",
                "${qualifiedClassName}_impl.java")
        writeCodeInFile(jfo, qualifiedClassName, stringTableName, package_, it)
    }

    private fun writeCodeInFile(
        jfo: FileObject,
        qualifiedClassName: String,
        stringTableName: String,
        package_: String,
        it: Element,
    ) {
        val clazzBuilder = ClassBuilder()
        jfo.openWriter().use { writer ->
            writer.write("package " + package_ + ";" + NEW_LINE)

            writer.write(addImport("android.database.sqlite.SQLiteOpenHelper;",
                "android.content.ContentValues;","static hr.algebra.ivanabilic.nba.framework.ExtensionsKt.*;") + NEW_LINE)


            clazzBuilder.setClass(qualifiedClassName,"implements")
            clazzBuilder.setProperty("__db", "SQLiteOpenHelper")
            clazzBuilder.setConstrucotr(qualifiedClassName + "_impl",
                "this.__db=__db;",
                "SQLiteOpenHelper __db")
            for (method in processingEnv.elementUtils.getPackageOf(it).enclosedElements[0].enclosedElements) {
                if (method.simpleName.toString() == "insert") {
                    clazzBuilder.setMethodWithOverrride(method,
                        "ContentValues values = decompile(player); return __db.getWritableDatabase().insert($stringTableName, null, values)")
                }
                if (method.simpleName.toString() == "update") {
                    clazzBuilder.setMethodWithOverrride(method,
                        "ContentValues values = decompile(player); return __db.getWritableDatabase().update($stringTableName, values, getSelection(player.get_id()), getSelectionArgs(player.get_id()))")
                }
                if (method.simpleName.toString() == "delete") {
                    clazzBuilder.setMethodWithOverrride(method,
                        "return __db.getWritableDatabase().delete($stringTableName, getSelection(_id), getSelectionArgs(_id) )")
                }
                if (method.simpleName.toString() == "query") {
                    clazzBuilder.setMethodWithOverrride(method,
                        "return __db.getReadableDatabase().query($stringTableName, null,null ,null, null, null,null )")
                }
            }
            writer.write(
                clazzBuilder.build()
            )
        }
    }
}