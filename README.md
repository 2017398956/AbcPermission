# AbcPermission
一个在 Android M+ 上很方便的权限申请库，只需要在需授权的方法上加上注解即可，不会侵入业务逻辑。当 app 没有该权限时不会执行该方法，并弹出申请权限框，如果用户选择了“不再提示”则会打开设置界面（这里你可以定制）；如果有相应权限则会执行并且有异常时在 GetPermissionListener 中抛出，可自行 try catch 或根据异常信息做相应的操作。这里是这个库的思路 [http://blog.csdn.net/niuzhucedenglu/article/details/78707302](http://blog.csdn.net/niuzhucedenglu/article/details/78707302) （关于原理以后补上）
## 使用方法（以下操作在 gradle:3.0.1 下测试通过）
### 1.在项目目录下的 build.gradle 文件中加入
    
	allprojects {
    	repositories {
        	...
        	maven { url 'https://jitpack.io' }
    	}
	}

### 2.在需要权限的 module 的 build.gradle 中加入

	buildscript {
	    repositories {
	        mavenCentral()
	    }
	    dependencies {
	        classpath 'org.aspectj:aspectjtools:1.8.13'
	        classpath 'org.aspectj:aspectjweaver:1.8.13'
	    }
	}

	dependencies {
	    ...
	    api("com.github.2017398956:AbcPermission:1.2") {
	        exclude module: 'permissionAnnotation'
	        exclude module: 'permissionCompiler'
	    }
	    provided("com.github.2017398956:AbcPermission:1.2") {
	        exclude module: 'permissionSupport'
	        exclude module: 'permissionCompiler'
	    }
	    annotationProcessor("com.github.2017398956:AbcPermission:1.2") {
	        exclude module: 'permissionSupport'
	    }
	}
	// 如果 module 是 library 则 将 applicationVariants 替换为 libraryVariants
	android.applicationVariants.all { variant ->
	    Task javaCompile = variant.getJavaCompiler()
	    javaCompile.doLast {
	        String[] args = ["-showWeaveInfo",
	                         "-1.5",
	                         "-inpath", javaCompile.destinationDir.toString(),
	                         "-aspectpath", javaCompile.classpath.asPath,
	                         "-d", javaCompile.destinationDir.toString(),
	                         "-classpath", javaCompile.classpath.asPath,
	                         "-bootclasspath", android.bootClasspath.join(File.pathSeparator)]
	        MessageHandler handler = new MessageHandler(true)
	        new Main().run(args, handler)
	
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

如果报出 找不到 MessageHandler 等类的错误，先将其注释掉，sync 后再打开加入即可。

### 3.在你的 application 类中加入

	// 在申请权前配置
	@Override
    public void onCreate() {
        super.onCreate();
		AbcPermission.install(this);

### 4.配置 nowActivity 

由于权限的申请需要 activity 这里我们维护一个全局的 nowActivity，如果你的目标用户在 4.0 以上你可以在 application 中添加（也必须在申请权前）

	registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
	            @Override
	            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
	                ApplicationConstant.nowActivity = activity;
	            }
	
	            @Override
	            public void onActivityStarted(Activity activity) {
	                ApplicationConstant.nowActivity = activity;
	            }
	
	            @Override
	            public void onActivityResumed(Activity activity) {
	                ApplicationConstant.nowActivity = activity;
	            }

如果你的目标用户包括 4.0 之前的版本，那么会直接执行被注解的方法。但不用担心会发生 crash 。所有被注解的方法发生异常时都可以在 GetPermissionListener 中 try catch。

### 5.配置 GetPermissionListener （同样需要在权限申请之前）

我已经提供了一个默认的 GetPermissionListener 仅供测试使用。

	AbcPermission.permissionListener = new AbcPermission.GetPermissionListener(){
	 		/**
	         * 当用户不给权限且选择了不再提示后，会执行这个方法，比如打开 设置 界面
	         *
	         * @param activity 
	         * @param permissions 用户拒绝授予的权限
	         */
	            @Override
	            public void cannotRequestAgain(Activity activity, String[] permissions) {
	                
	            }
	
			/**
	         * 为了程序不崩溃，被注解的方法在这里抛出异常,需要你自行处理
	         *
	         * @param throwable
	         */
	
	            @Override
	            public void exeException(Throwable throwable) {
	                
	            }
	        } ;

### 6.使用注解申请权限

    public void onClick(View view) {
        readContacts();
    }

    @GetPermissions(Manifest.permission.READ_CONTACTS)
    private void readContacts() {
        Toast.makeText(this, "测试成功！！", Toast.LENGTH_SHORT).show();
    }

### 注：不管用户同意授权与否，都需要用户再次操作一次才能实现用户的意图（因为没有使用回掉方法）。如：用户点击按钮想要打开摄像头，则先弹出请求摄像头的权限，用户同意后，需要再次点击按钮。只所以这么做是因为权限只需要获取一次，没有回掉不会影响用户体验，但却可以解耦业务逻辑（优势很明显^_^）。