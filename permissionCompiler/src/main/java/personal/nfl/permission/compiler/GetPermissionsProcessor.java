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

import personal.nfl.permission.annotation.GetPermissions;

/**
 * Created by nfl on 2017/11/30.
 */

@AutoService(Processor.class)
public class GetPermissionsProcessor extends AbstractProcessor {

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
        types.add(GetPermissions.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> getPermissionsElement = roundEnvironment.getElementsAnnotatedWith(GetPermissions.class);
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
            String[] permissions = executableElement.getAnnotation(GetPermissions.class).value();
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
            writer.write(brewCode(packageName, enclosingName, permissions, classPostfix, methodName, returnType));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String brewCode(String packageName, String enclosingName, String[] permissions, String classPostfix, String methodName, String returnType) {

        StringBuilder builder = new StringBuilder();
        builder.append("package " + packageName + ";\n\n");
        builder.append("import android.Manifest;\n");
        builder.append("import android.app.Activity;\n");
        builder.append("import android.content.Context;\n");
        builder.append("import android.content.Intent;\n");
        builder.append("import android.content.pm.PackageManager;\n");
        builder.append("import android.net.Uri ;\n");
        builder.append("import android.support.v4.content.ContextCompat;\n");
        builder.append("import android.support.v4.app.ActivityCompat;\n");
        builder.append("import android.util.Log;\n");
        builder.append("import org.aspectj.lang.JoinPoint;\n");
        builder.append("import org.aspectj.lang.ProceedingJoinPoint;\n");

        builder.append("import personal.nfl.permission.support.constant.ApplicationConstant;\n");
        builder.append("import personal.nfl.permission.support.util.AbcPermission;\n");
        builder.append("import personal.nfl.permission.support.util.SharePreferenceTool;\n");
        builder.append("import org.aspectj.lang.annotation.After;\n");
        builder.append("import org.aspectj.lang.annotation.Around;\n");
        builder.append("import org.aspectj.lang.annotation.Aspect;\n");
        builder.append("import org.aspectj.lang.annotation.Before;\n");
        builder.append("import org.aspectj.lang.annotation.Pointcut;\n");
        builder.append("import java.util.ArrayList;\n");
        builder.append("import java.util.List;\n");
        builder.append("//Auto generated by apt,do not modify!!\n\n");
        builder.append("@Aspect\n");
        builder.append("public class AutoCreate");
        builder.append(classPostfix);
        builder.append(" { \n\n");

        builder.append("private Activity activity = ApplicationConstant.nowActivity ;\n");
        builder.append("private String[] permissions = {");
        for (int i = 0; i < permissions.length; i++) {
            builder.append("\"");
            builder.append(permissions[i]);
            builder.append("\"");
            if (i < permissions.length - 1) {
                builder.append(',');
            }
        }
        builder.append("};\n");
        builder.append("public static final String METHOD_CALL = \"call(* ");
        builder.append(enclosingName);
        builder.append(".");
        builder.append(methodName);
        builder.append("(..))\";\n");

        builder.append("public static final String METHOD_EXE = \"execution(* ");
        builder.append(enclosingName);
        builder.append(".");
        builder.append(methodName);
        builder.append("(..))\";\n");

        builder.append("@Pointcut(METHOD_CALL)\n");
        builder.append("public void methodCall() {}\n");

        builder.append("@Pointcut(METHOD_EXE)\n");
        builder.append("public void methodExe() {}\n");
        // create method beforeCall
        builder.append("@Before(\"methodCall()\")\n");
        builder.append("public void beforeCall(JoinPoint joinPoint) {\n");
        builder.append(" Log.i(\"NFL\", \"before GetPermissions exe\");}\n");
        // create method aroundExe
        builder.append("@Around(\"methodExe()\")\n");
        builder.append("public ");
        if ("void".equals(returnType)) {
            builder.append("Object");
        } else {
            builder.append(returnType);
        }
        builder.append(" aroundExe(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {\n");
        builder.append(" Log.i(\"NFL\", \"in GetPermissions exe\");\n");
        ///////////////////////////////////////////////////////////////////////////////////////////
        builder.append("List<String> permissionList = new ArrayList<>();\n");
        builder.append("for (String permission : permissions) {\n");
        builder.append("if (ContextCompat.checkSelfPermission(activity , permission) != PackageManager.PERMISSION_GRANTED) {\n");
        builder.append("permissionList.add(permission);}}\n");
        builder.append("if (permissionList.size() == 0) {\n");
        // exe annotation method
        if ("void".equals(returnType)) {
            builder.append("proceedingJoinPoint.proceed();\n");
            builder.append("return null ;\n");
        } else {
            builder.append(returnType);
            builder.append(" result = (" + returnType +
                    ") proceedingJoinPoint.proceed();\n");
            builder.append("return result;\n");
        }
        // exe annotation method
        builder.append("} else {\n");
        builder.append("String[] permissionListTemp = new String[permissionList.size()] ;\n");
        builder.append("for(int i = 0 ; i < permissionList.size() ; i++){\n");
        builder.append("permissionListTemp[i] = permissionList.get(i) ;\n");
        builder.append("}\n");

        builder.append("for (int i = 0; i < permissionList.size(); i++) {\n");
        builder.append("if (ContextCompat.checkSelfPermission(activity , permissionList.get(i)) != PackageManager.PERMISSION_GRANTED) {\n");
        builder.append(" Log.i(\"NFL\", \"PERMISSION_DENY\");\n");
        builder.append("if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionList.get(i))) {\n");
        builder.append(" Log.i(\"NFL\", \"shouldShowRequestPermissionRationale\");\n");
        builder.append("ActivityCompat.requestPermissions(activity, permissionListTemp , 0);\n");
        builder.append("break;\n");
        builder.append("} else {\n");
        builder.append("if (SharePreferenceTool.readObject(activity , permissionList.get(i)) != null) {\n");
        builder.append(" Log.i(\"NFL\", \"SharePreferenceTool not null \");\n");
        builder.append("if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionList.get(i))) {\n");
        builder.append(" Log.i(\"NFL\", \"!shouldShowRequestPermissionRationale not null \");\n");
        builder.append("AbcPermission.permissionListener.cannotRequestAgain(activity , permissionListTemp);");
        builder.append("break;\n");
        builder.append("}\n");
        builder.append("} else\n");
        builder.append("{\n");
        builder.append("if (permissionList.size() - 1 == i) {\n");
        builder.append("ActivityCompat.requestPermissions(activity, permissionListTemp , 0);\n");
        builder.append("}\n");
        builder.append("SharePreferenceTool.saveObject(\"rejected\", permissionList.get(i));\n");
        builder.append("}\n");
        builder.append("}\n");
        builder.append("}\n");
        builder.append("}\n");
        builder.append("}\n");
        builder.append("return null ;\n");
        builder.append("}\n");
        ///////////////////////////////////////////////////////////////////////////////////////////
        // create method afterCall
        builder.append("@After(\"methodCall()\")\n");
        builder.append("public void afterCall(JoinPoint joinPoint) {\n");
        builder.append(" Log.i(\"NFL\", \"after GetPermissions exe\");}\n");
        // create file finish
        builder.append("}");
        return builder.toString();
    }

    private void note(String msg) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

    private void note(String format, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(format, args));
    }
}
