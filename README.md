# AbcPermission

[![Release Version](https://img.shields.io/badge/release-1.6.4-green.svg)](https://github.com/2017398956/AbcPermission/releases)

**更新日志：**
- **1.6.4** 支持 AndroidX ；使用 support 的请使用 com.github.2017398956:abcpermission-plugin:1.3.5 。

- **1.6.3** 解决在 gradle4.4中 appcompat-v7 版本冲突问题：考虑到 appcompat-v7 现在是必安装的包，因此在项目中不再传递该依赖，以免造成版本冲突，使用时请自行添加 appcompat-v7 依赖。

## 接入方式优化

由于部分读者反映 [AbcPermission](https://github.com/2017398956/AbcPermission "AbcPermission") 的接入方式太麻烦，利用周末时间我写了个 gradle 插件用于简化接入流程。这里提供了 2 套接入方案：一为最简便的；另一个支持版本自定义（如果你一直用最新版，这个可以忽略）。[AbcPermission](https://github.com/2017398956/AbcPermission "AbcPermission") 的概念 [这里](http://blog.csdn.net/niuzhucedenglu/article/details/78707302 "AbcPermission") 有详细的讲解。[这里是一次重要的升级](http://blog.csdn.net/niuzhucedenglu/article/details/78842723)，也可以直接看 [AbcPermission](https://github.com/2017398956/AbcPermission "AbcPermission") 的 readme 文件。

### 方案一

#### 1.在根目录下的 build 文件中添加如下代码

	buildscript {
	    repositories {
	       ...
	       maven { url 'https://jitpack.io' }
	    }
	    dependencies {
	        ...
	        // 如果你使用的时 androidx 请接入 1.3.5 以后的版本
	        classpath 'com.github.2017398956:abcpermission-plugin:1.3.5'
	    }
	}

	allprojects {
	    repositories {
	        ...
	        maven { url 'https://jitpack.io' }
	    }
	}

#### 2.在需要申请权限的 module 中添加如下代码

	apply plugin: 'abcpermission.plugin'

### 方案二

#### 1.在根目录下的 build 文件中添加如下代码

	buildscript {
	    repositories {
	       ...
	       maven { url 'https://jitpack.io' }
	    }
	    dependencies {
	        ...
	        classpath 'com.github.2017398956:AspectPlugin:1.2'
	    }
	}

	allprojects {
	    repositories {
	        ...
	        maven { url 'https://jitpack.io' }
	    }
	}

#### 2.在需要申请权限的 module 中添加如下代码

	apply plugin: 'AspectPlugin'

	dependencies {
	    ...
	    api("com.github.2017398956:AbcPermission:1.6.3") {
	        exclude module: 'permissionAnnotation'
	        exclude module: 'permissionCompiler'
	    }
	    provided("com.github.2017398956:AbcPermission:1.6.3") {
	        exclude module: 'permissionSupport'
	        exclude module: 'permissionCompiler'
	    }
	    annotationProcessor("com.github.2017398956:AbcPermission:1.6.3") {
	        exclude module: 'permissionSupport'
	    }
	}

## V1.6 重要更新

关于 [AbcPermission](https://github.com/2017398956/AbcPermission "AbcPermission") 的概念 [这里](http://blog.csdn.net/niuzhucedenglu/article/details/78707302 "AbcPermission") 有详细的讲解。但是在以前的版本中我们都不能对用户授权后进行回调，虽然用户只在初次授权的时候会多一次操作，但对用户体验影响不大。为了更加优化用户体检，从 V1.6 以后 [AbcPermission](https://github.com/2017398956/AbcPermission "AbcPermission") 支持对 **无返回值的方法** 进行回调，并且其回调流程已统一整合在  AbcPerpermission.permissionListener 。请放心，这次升级完全兼容 V1.6 之前的版本。

### AbcPermission V1.6 新特性的使用方法

1. 首先按照 [这里](http://blog.csdn.net/niuzhucedenglu/article/details/78707302 "AbcPermission") 或者下面 [1.6之前的方法](https://github.com/2017398956/AbcPermission#以下是-v16-之前的说明)  配置好所需的环境；

2. V1.6 之前的版本获取权限时使用的是 @GetPermissions({...}) ,现在你可以放弃这种用法了，将其改为  @GetPermissionsAuto({...}) 这样就可以对 **无返回值的方法** 在进行申请权限时进行回调；对于 **有返回值的方法** ，会走以前的逻辑，请放心升级。
3. 回调分为以下三类：1.用户点击 **同意授权** ，这种不需要你处理；      2.用户点击 **拒绝授权** ， 用户点击后会触发 AbcPerpermission.permissionListener.showRequestPermissionRationale(final Permission23Fragment permission23Fragment, final String[] permissions) 你可以在这里根据权限信息弹出相应的提示，当用户点击确定后 通过 **permission23Fragment.requestPermissions(permissions);** 再次申请权限，注意，这里**不要**使用 Fragment#requestPermissions(String[], int) 申请权限；      3.用户点击 **拒绝授权且不再提示** ，*如果你已经接入了 V1.6 之前的版本，那么，不需要做任何处理，就是这么省心 ^_^，*如果是第一次接入你可以在 ABCPermission.permissionListener.cannotRequestAgain(...) 中进行弹窗提示和打开设置界面，详细内容请移步 [这里](http://blog.csdn.net/niuzhucedenglu/article/details/78707302 "AbcPermission") 。

### 使用示例
1.申请权限只需要一行代码
	
	@GetPermissionsAuto({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private void readFile() {
        ...
    }

2.回调的处理，这里只以 用户点击 **拒绝授权** 为例

	@RequiresApi(api = Build.VERSION_CODES.M)
        public void showRequestPermissionRationale(final Permission23Fragment permission23Fragment, final String[] permissions) {
            StringBuffer stringBuffer = new StringBuffer();
            for (String permission : permissions) {
                stringBuffer.append(permission);
                stringBuffer.append("\n");
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(permission23Fragment.getActivity()).setTitle("权限申请")
                    .setMessage(stringBuffer.toString())
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
							// 用户看到需要权限的理由，同意后再次申请
                            permission23Fragment.requestPermissions(permissions);
                        }
                    })
                    .setNegativeButton("取消", null);
            builder.create().show();

        }



## 以下是 V1.6 之前的说明

一个在 Android M+ 上很方便的权限申请库，只需要在需授权的方法上加上注解即可，不会侵入业务逻辑。当 app 没有该权限时不会执行该方法，并弹出申请权限框，如果用户选择了“不再提示”则会打开设置界面（这里你可以定制）；如果有相应权限则会执行并且有异常时在 GetPermissionListener 中抛出，可自行 try catch 或根据异常信息做相应的操作。这里是这个库的思路 [http://blog.csdn.net/niuzhucedenglu/article/details/78707302](http://blog.csdn.net/niuzhucedenglu/article/details/78707302) 

### 原理图

![](https://i.imgur.com/vQ0PA3U.png)

### 使用方法（以下操作在 gradle:3.0.1 下测试通过）
#### 1.在项目目录下的 build.gradle 文件中加入
    
	allprojects {
    	repositories {
        	...
        	maven { url 'https://jitpack.io' }
    	}
	}

#### 2.在需要权限的 module 的 build.gradle 中加入

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
	    api("com.github.2017398956:AbcPermission:1.4") {
	        exclude module: 'permissionAnnotation'
	        exclude module: 'permissionCompiler'
	    }
	    provided("com.github.2017398956:AbcPermission:1.4") {
	        exclude module: 'permissionSupport'
	        exclude module: 'permissionCompiler'
	    }
	    annotationProcessor("com.github.2017398956:AbcPermission:1.4") {
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

#### 3.在你的 application 类中加入

	// 在申请权前配置
	@Override
    public void onCreate() {
        super.onCreate();
		AbcPermission.install(this);

#### 4.配置 nowActivity 

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

#### 5.配置 GetPermissionListener （同样需要在权限申请之前）

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

#### 6.使用注解申请权限

    public void onClick(View view) {
        readContacts();
    }

    @GetPermissions(Manifest.permission.READ_CONTACTS)
    private void readContacts() {
        Toast.makeText(this, "测试成功！！", Toast.LENGTH_SHORT).show();
    }

#### 7.兼容性
关于兼用性由于没有相应手机可供测试，这里以 [AndPermission](https://github.com/yanzhenjie/AndPermission/blob/master/README-CN.md#%E5%9B%BD%E4%BA%A7%E6%89%8B%E6%9C%BA%E9%80%82%E9%85%8D%E6%96%B9%E6%A1%88) 提到的几种情况作为适配依据，以下粗体字为我的适配方法。

1. 部分中国厂商生产手机（例如小米某型号）的Rationale功能，在第一次拒绝后，第二次申请时不会返回true，并且会回调申请失败，也就是说在第一次决绝后默认勾选了不再提示。**AbcPermission 有自己的判断逻辑，所以这一点已经和正常的逻辑统一了，只需要在permissionListener.cannotRequestAgain(...); 中处理即可。**

2. 部分中国厂商生产手机（例如小米、华为某型号）在申请权限时，用户点击确定授权后，还是回调我们申请失败，这个时候其实我们是拥有权限的。**由于 AbcPermission 没有使用回调机制，所以这一点不会造成影响。**

3. 部分中国厂商生产手机在系统Setting中设置**[禁用/询问]**某权限，但是在申请此权限时却直接提示有权限，这可能是厂商故意这样设计的，当我们真正执行需要这个权限代码的时候系统会自动申请权限。**既然需要权限的代码可以自动申请权限，而 AbcPermission 在得知有权限时会直接执行相应代码，所以对我们没影响**

4. 部分中国厂商生产手机（例如vivo、pppo某型号）在用户允许权限，并且回调了权限授权成功的方法，但是实际执行代码时并没有这个权限。**由于没权限，当用户执行到这块代码的时候 app 会在 permissionListener.exeException(...) 抛出异常；此时我们在这里判断异常信息为缺权限时，提示用户打开设置界面即可，并且 permissionListener 是全局的，只需要维护一份代码即可。**

5. 部分开发者反馈，在某些手机的Setting中授权后，检查时还是没有权限，执行响应的代码时应用崩溃（错误提示是没有权限），这种手机真的兼容不到了，我也觉得没必要兼容了，建议直接放弃这种平台。**在 Nexus6P Android 8.0.0 版本发现在使用 "android.permission.SYSTEM_ALERT_WINDOW" 和或"android.permission.WRITE_SETTINGS" 时出现了该问题，估计是新系统 bug （2017-12-08），执行到需要这些权限的方法，AbcPermission 会一直执行你在 AbcPermission.permissionListener.cannotRequestAgain(...) 设定的逻辑（如弹框让用户在设置界面授予权限），考虑到 8.0.0 在国内占有量不大，用户体验还可以接受**

#### 8.AbcPermission 相较于其它权限库的优势

1.  **权限和业务分离**
1.  **23 以下的版本适配到 23+ 以上非常方便：** 当你的项目时间比较长，现在要从 Android 23 以下的版本适配到 Android 23 以上时，为了防止因为权限的问题而 crash 往往在 app 启动后立刻申请一系列的权限，如果用户不授予权限则不让用户使用（很多 app 都这么做的）；这样处理虽然简便，但十分粗暴，严重影响用户体验。如果对使用到特殊权限的代码进行逐一修改来适配 Android 23+ 工作量大不说，还要小心处理业务逻辑，代码改动越大越容易出错，升级成本过大。如果使用 AbcPermission 则可以直接在这些方法上加一句权限申请的注解即可，不用动以前的逻辑；如果原业务逻辑不在你自己写的方法中，可以对其抽离成一个方法，也十分方便。

### 注：不管用户同意授权与否，都需要用户再次操作一次才能实现用户的意图（因为没有使用回调方法）。如：用户点击按钮想要打开摄像头，则先弹出请求摄像头的权限，用户同意后，需要再次点击按钮。只所以这么做是因为权限只需要获取一次，没有回调不会影响用户体验，但却可以解耦业务逻辑（优势很明显^_^）。另外，GetPermission 只能用在你自己写的方法上。
