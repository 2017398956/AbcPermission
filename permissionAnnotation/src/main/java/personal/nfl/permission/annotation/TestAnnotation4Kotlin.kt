package personal.nfl.permission.annotation

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
public annotation class TestAnnotation4Kotlin(val permissions: Array<String>)