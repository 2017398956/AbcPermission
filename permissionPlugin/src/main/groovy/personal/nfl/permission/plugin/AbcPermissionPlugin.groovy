package personal.nfl.permission.plugin

import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class AbcPermissionPlugin implements Plugin<Project> {

    void apply(Project project) {

        for (String name : project.repositories.getNames()) {
            println(name)
        }
        project.repositories.maven {
            url 'https://jitpack.io'
        }

        project.dependencies{
            api("com.github.2017398956:AbcPermission:1.6.4") {
                exclude module: 'permissionAnnotation'
                exclude module: 'permissionCompiler'
            }
            provided("com.github.2017398956:AbcPermission:1.6.4") {
                exclude module: 'permissionSupport'
                exclude module: 'permissionCompiler'
            }
            annotationProcessor("com.github.2017398956:AbcPermission:1.6.4") {
                exclude module: 'permissionSupport'
            }
        }

//        project.dependencies.add("implementation",
//                "com.github.2017398956:AbcPermission:1.6", {
//            "exclude module: 'permissionAnnotation'"
//            "exclude module: 'permissionCompiler'"
//        })
//        project.dependencies.add("provided",
//                "com.github.2017398956:AbcPermission:1.6", {
//            "exclude module: 'permissionSupport'"
//            "exclude module: 'permissionCompiler'"
//        })
//        project.dependencies.add("annotationProcessor",
//                "com.github.2017398956:AbcPermission:1.6", {
//            "exclude module: 'permissionSupport'"
//            "exclude module: 'permissionCompiler'"
//        })


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
            String[] args = ["-showWeaveInfo",
                             "-1.5",
                             "-inpath", javaCompile.destinationDir.toString(),
                             "-aspectpath", javaCompile.classpath.asPath,
                             "-d", javaCompile.destinationDir.toString(),
                             "-classpath", javaCompile.classpath.asPath,
                             "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]
            MessageHandler handler = new MessageHandler(true)
            new Main().run(args, handler)

            def log = project.logger
            for (IMessage message : handler.getMessages(null, true)) {
                switch (message.getKind()) {
                    case IMessage.ABORT:
                    case IMessage.ERROR:
                    case IMessage.FAIL:
                        log.error message.message, message.thrown
                        break
                    case IMessage.WARNING:
                    case IMessage.INFO:
                        log.info message.message, message.thrown
                        break
                    case IMessage.DEBUG:
                        log.debug message.message, message.thrown
                        break
                }
            }
        }
    }
}