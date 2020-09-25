package personal.nfl.abcpermission;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import personal.nfl.permission.support.R;
import personal.nfl.permission.support.constant.ApplicationConstant;
import personal.nfl.permission.support.util.AbcPermission;
import personal.nfl.permission.support.view.Permission23Fragment;
//Auto generated by apt,do not modify!!

@Aspect
public class AutoCreate12 {

    // private String[] permissions = {"android.permission.WRITE_CONTACTS"};
    private String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE"};

    private final String METHOD_CALL = "call(* personal.nfl.abcpermission.kotlin.KotlinTest.test(..))";
    private final String METHOD_EXE = "execution(* personal.nfl.abcpermission.kotlin.KotlinTest.test(..))";
    private ProceedingJoinPoint proceedingJoinPoint;

    @Pointcut(METHOD_CALL)
    public void methodCall() {
    }

    @Pointcut(METHOD_EXE)
    public void methodExe() {
    }

    @Before("methodCall()")
    public void beforeCall(JoinPoint joinPoint) {
        Log.i("NFL", "beforeCall Permission23Fragment");
        if (Build.VERSION.SDK_INT >= 14) {
            ViewGroup viewGroup = ApplicationConstant.nowActivity.getWindow().getDecorView().findViewById(android.R.id.content);
            FrameLayout frameLayout = ApplicationConstant.nowActivity.findViewById(R.id.permission23);
            if (null == frameLayout) {
                frameLayout = new FrameLayout(ApplicationConstant.nowActivity);
                frameLayout.setVisibility(View.GONE);
                frameLayout.setBackgroundColor(Color.GREEN);
                frameLayout.setId(R.id.permission23);
                viewGroup.addView(frameLayout);
            }
            final FragmentManager fragmentManager = ApplicationConstant.nowActivity.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            final Permission23Fragment permission23Fragment = new Permission23Fragment();
            permission23Fragment.setPermissionsHandler(permissionsHandler);
            fragmentTransaction.add(R.id.permission23, permission23Fragment);
            fragmentTransaction.commit();
            frameLayout.post(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void run() {
                    permission23Fragment.requestPermissions(permissions);
                }
            });
        }
    }

    @Around("methodExe()")
    public Object aroundExe(ProceedingJoinPoint proceedingJoinPoint) {
        Log.i("NFL", "in Permission23Fragment exe");
        if (Build.VERSION.SDK_INT < 14) {
            try {
                return proceedingJoinPoint.proceed();
            } catch (Throwable throwable) {
                AbcPermission.permissionListener.exeException(throwable);
                return null;
            }
        } else {
            this.proceedingJoinPoint = proceedingJoinPoint;
        }
        return null;
    }

    @After("methodCall()")
    public void afterCall(JoinPoint joinPoint) {
        Log.i("NFL", "after Permission23Fragment exe");
    }

    private Permission23Fragment.PermissionsHandler permissionsHandler = new Permission23Fragment.PermissionsHandler() {
        @Override
        public void success() {
            try {
                proceedingJoinPoint.proceed();
            } catch (Throwable throwable) {
                AbcPermission.permissionListener.exeException(throwable);
            }
        }
    };
}
