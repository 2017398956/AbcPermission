package personal.nfl.permission.compiler;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import personal.nfl.permission.annotation.GetPermissions4AndroidX;
import personal.nfl.permission.util.CodeCreator;

/**
 * Created by nfl on 2017/11/30.
 */

@AutoService(Processor.class)
public class GetPermissions4AndroidXProcessor extends AbstractProcessor {

    private Filer mFiler; //文件相关的辅助类
    private Elements mElementUtils; //元素相关的辅助类
    private Messager mMessager; //日志相关的辅助类
    private Locale mLocale;
    private Map<String, String> mOptions;
    private Types mTypeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mLocale = processingEnv.getLocale();
        mOptions = processingEnv.getOptions();
        mTypeUtils = processingEnv.getTypeUtils();
    }

    /**
     * @return 指定哪些注解应该被注解处理器注册
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(GetPermissions4AndroidX.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> getPermissionsElement = roundEnvironment.getElementsAnnotatedWith(GetPermissions4AndroidX.class);
        for (Element element : getPermissionsElement) {
            // 1. get package name(You'll get different name if you uses BindView in different class .)
            PackageElement packageElement = mElementUtils.getPackageOf(element);
            String packageName = packageElement.getQualifiedName().toString();
            // com.a2017398956.nodesignmodeframework.activity
            note(String.format("package = %s", packageName));
            // 2.get enclosing class name
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            String enclosingName = enclosingElement.getQualifiedName().toString();
            // com.a2017398956.nodesignmodeframework.activity.MainActivity
            note(String.format("enclosindClass = %s", enclosingName));

            ExecutableElement executableElement = (ExecutableElement) element;
            String[] permissions = executableElement.getAnnotation(GetPermissions4AndroidX.class).value();
            String methodName = executableElement.getSimpleName().toString();
            String returnType = executableElement.getReturnType().toString();

            note(String.format("%s %s", returnType, methodName));
            // create the real file to be compiled
            createFile(packageName, enclosingName, permissions, methodName, returnType);
        }
        return false;
    }


    /**
     * create the real file to be compiled
     */
    private void createFile(String packageName, String enclosingName, String[] permissions, String methodName, String returnType) {
        try {
            Calendar calendar = Calendar.getInstance();
            String classPostfix = "" + (calendar.get(Calendar.MINUTE) + 100) + (calendar.get(Calendar.SECOND) + 100) + (calendar.get(Calendar.MILLISECOND) + 1000);
            JavaFileObject jfo = mFiler.createSourceFile(packageName + ".AutoCreate" + classPostfix, new Element[]{});
            Writer writer = jfo.openWriter();
            if ("void".equals(returnType)) {
                writer.write(CodeCreator.brewCode(packageName, enclosingName, permissions, classPostfix, methodName, returnType,true));
            } else {
                writer.write(CodeCreator.brewCodeNoCallback(packageName, enclosingName, permissions, classPostfix, methodName, returnType,true));
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void note(String msg) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

    private void note(String format, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(format, args));
    }
}
