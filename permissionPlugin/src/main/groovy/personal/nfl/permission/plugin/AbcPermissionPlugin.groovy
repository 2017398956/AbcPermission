package personal.nfl.permission.plugin

import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import personal.nfl.permission.plugin.utils.FileUtil

class AbcPermissionPlugin implements Plugin<Project> {

    String sourceJDK = "1.8"
    String targetJDK = "1.8"

    void apply(Project project) {

        for (String name : project.repositories.getNames()) {
            println(name)
        }
        project.repositories.maven {
            url 'https://jitpack.io'
        }
        // 默认使用 java 的注解方式
        String annotationMethod = "annotationProcessor"
        if (project.plugins.findPlugin("kotlin-kapt") != null) {
            // 支持 kotlin
            // 如果在 kotlin 代码中使用了注解的方式生成代码，那么需要在
            // apply plugin: 'kotlin-kapt' 之后添加 apply plugin: 'abcpermission.plugin'
            // 顺序颠倒后将无法正常运行
            annotationMethod = "kapt"
        } else {
            // 使用默认的 annotationProcessor
        }

//        project.dependencies{
//            api("com.github.2017398956:AbcPermission:1.6.8") {
//                exclude module: 'permissionAnnotation'
//                exclude module: 'permissionCompiler'
//            }
//            provided("com.github.2017398956:AbcPermission:1.6.8") {
//                exclude module: 'permissionSupport'
//                exclude module: 'permissionCompiler'
//            }
//            kapt("com.github.2017398956:AbcPermission:1.6.8") {
//                exclude module: 'permissionSupport'
//            }
//        }

        project.dependencies.add("implementation",
                "com.github.2017398956:AbcPermission:1.6.9", {
            "exclude module: 'permissionAnnotation'"
            "exclude module: 'permissionCompiler'"
        })
        project.dependencies.add("compileOnly",
                "com.github.2017398956:AbcPermission:1.6.9", {
            "exclude module: 'permissionSupport'"
            "exclude module: 'permissionCompiler'"
        })
        project.dependencies.add(annotationMethod,
                "com.github.2017398956:AbcPermission:1.6.9", {
            "exclude module: 'permissionSupport'"
            "exclude module: 'permissionCompiler'"
        })

        if (project.hasProperty('android') && project.android != null) {
            if (project.android.hasProperty('compileOptions') && project.android.compileOptions != null) {
                if (project.android.compileOptions.hasProperty('targetCompatibility') && project.android.compileOptions.targetCompatibility != null) {
                    // targetJDK = project.android.compileOptions.properties.get('targetCompatibility')
                }
                if (project.android.compileOptions.hasProperty('sourceCompatibility') && project.android.compileOptions.sourceCompatibility != null) {
                    // sourceJDK = project.android.compileOptions.properties.get('sourceCompatibility')
                }
            }

        }

        if (project.hasProperty('android') && project.android != null) {
            if (project.android.hasProperty('applicationVariants')
                    && project.android.applicationVariants != null) {
                project.android.applicationVariants.all { variant ->
                    doLast(variant.getJavaCompileProvider().get())
                }
            }
            if (project.android.hasProperty('libraryVariants')
                    && project.android.libraryVariants != null) {
                project.android.libraryVariants.all { variant ->
                    doLast(variant.getJavaCompileProvider().get())
                }
            }
        }
    }

    private void doLast(Task javaCompile) {
        javaCompile.doLast {
            MessageHandler handler = new MessageHandler(true)
            String aspectPath = javaCompile.classpath.asPath
            String inPath = javaCompile.destinationDir.toString()
            String dPath = javaCompile.destinationDir.toString();
            String classpath = javaCompile.classpath.asPath
            // 配置 kotlin 相关参数
            String kotlinInPath = ""
            if (dPath.contains("debug\\classes")) {
                kotlinInPath = javaCompile.temporaryDir.getParentFile().path + File.separator + "kotlin-classes" + File.separator + "debug"
            } else {
                kotlinInPath = javaCompile.temporaryDir.getParentFile().path + File.separator + "kotlin-classes" + File.separator + "release"
            }
            // java 的 class 文件实现 aop
            String[] javacArgs = ["-showWeaveInfo",
                                  "-source", sourceJDK,
                                  "-target", targetJDK,
                                  "-inpath", kotlinInPath + ";" + inPath,
                                  "-aspectpath", aspectPath,
                                  "-d", dPath,
                                  "-classpath", classpath,
                                  "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]
            println(javacArgs)
            new Main().run(javacArgs, handler)
            File[] kotlinClassFiles = FileUtil.listFiles(kotlinInPath, true)
            File javacKotlinFile
            for (File temp : kotlinClassFiles) {
                if (temp.isFile() && temp.getName().endsWith(".class")) {
                    javacKotlinFile = new File(inPath + File.separator + temp.absolutePath.replace(kotlinInPath, ""))
                    if (null != javacKotlinFile && javacKotlinFile.exists()) {
                        FileUtil.delete(temp)
                        FileUtil.copyFile(javacKotlinFile, temp)
                        FileUtil.delete(javacKotlinFile)
                    }
                }
            }

            def log = project.logger
            for (IMessage message : handler.getMessages(null, true)) {
                switch (message.getKind()) {
                    case IMessage.ABORT:
                    case IMessage.ERROR:
                    case IMessage.FAIL:
                        log.error message.message, message.thrown
                        break;
                    case IMessage.WARNING:
                    case IMessage.INFO:
                        log.info message.message, message.thrown
                        break;
                    case IMessage.DEBUG:
                        log.debug message.message, message.thrown
                        break;
                }
            }
        }
    }
}