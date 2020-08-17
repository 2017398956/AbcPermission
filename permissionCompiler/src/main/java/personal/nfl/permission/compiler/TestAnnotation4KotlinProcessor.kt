package personal.nfl.permission.compiler

import com.google.auto.service.AutoService
import personal.nfl.permission.annotation.TestAnnotation4Kotlin
import personal.nfl.permission.util.CodeCreator
import java.io.IOException
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic

@AutoService(Process::class)
class TestAnnotation4KotlinProcessor : AbstractProcessor() {

    private var mFiler: Filer? = null//文件相关的辅助类
    private var mElementUtils: Elements? = null//元素相关的辅助类
    private var mMessager: Messager? = null//日志相关的辅助类
    private var mLocale: Locale? = null
    private var mOptions: Map<String, String>? = null
    private var mTypeUtils: Types? = null

    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        note("====kotlin log:TestAnnotation4KotlinProcessor init")
        mFiler = processingEnv.filer
        mElementUtils = processingEnv.elementUtils
        mMessager = processingEnv.messager
        mLocale = processingEnv.locale
        mOptions = processingEnv.options
        mTypeUtils = processingEnv.typeUtils
    }

    /**
     * @return 指定哪些注解应该被注解处理器注册
     */
    override fun getSupportedAnnotationTypes(): Set<String>? {
        val types: MutableSet<String> = LinkedHashSet()
        types.add(TestAnnotation4Kotlin::class.java.canonicalName)
        note("====kotlin log:annotation class is "
                + TestAnnotation4Kotlin::class.java.canonicalName + "++++++++++")
        return types
    }

    override fun process(set: Set<TypeElement?>?, roundEnvironment: RoundEnvironment): Boolean {
        note("====kotlin log:=============== start ===============")
        val getPermissionsElement = roundEnvironment.getElementsAnnotatedWith(TestAnnotation4Kotlin::class.java)
        if (null != getPermissionsElement) {
            note("====kotlin log:" + getPermissionsElement.size + "++++++++++")
        } else {
            note("====kotlin log:" + "getPermissionsElement.size()" + "++++++++++")
        }
        for (element in getPermissionsElement!!) {
            // 1. get package name(You'll get different name if you uses BindView in different class .)
            val packageElement = mElementUtils!!.getPackageOf(element)
            val packageName = packageElement.qualifiedName.toString()
            //Integer.parseInt(packageName) ;
            // com.a2017398956.nodesignmodeframework.activity
            note(String.format("package = %s", packageName))
            // 2.get enclosing class name
            val enclosingElement = element.enclosingElement as TypeElement
            val enclosingName = enclosingElement.qualifiedName.toString()
            // com.a2017398956.nodesignmodeframework.activity.MainActivity
            note(String.format("enclosindClass = %s", enclosingName))
            val executableElement = element as ExecutableElement
            val permissions: Array<String> = executableElement.getAnnotation(TestAnnotation4Kotlin::class.java).permissions
            val methodName = executableElement.simpleName.toString()
            val returnType = executableElement.returnType.toString()
            note(String.format("%s %s", returnType, methodName))
            // create the real file to be compiled
            createFile(packageName + "TestKotlin", enclosingName, permissions, methodName, returnType)
        }
        return false
    }


    /**
     * create the real file to be compiled
     */
    private fun createFile(packageName: String, enclosingName: String, permissions: Array<String>, methodName: String, returnType: String) {
        try {
            val calendar = Calendar.getInstance()
            val classPostfix = "" + (calendar[Calendar.MINUTE] + 100) + (calendar[Calendar.SECOND] + 100) + (calendar[Calendar.MILLISECOND] + 1000)
            val jfo = mFiler!!.createSourceFile("$packageName.AutoCreate$classPostfix", *arrayOf())
            val writer = jfo.openWriter()
            writer.write(CodeCreator.brewCodeNoCallback(packageName, enclosingName, permissions, classPostfix, methodName, returnType))
            writer.flush()
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun note(msg: String) {
        mMessager!!.printMessage(Diagnostic.Kind.NOTE, msg)
    }

    private fun note(format: String, vararg args: Any) {
        mMessager!!.printMessage(Diagnostic.Kind.NOTE, String.format(format, *args))
    }
}