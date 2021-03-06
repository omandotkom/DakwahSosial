package mobile.omandotkom.dakwahsosial;



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationHost{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ds_activity_main);
        Intent intent = getIntent();
        if (intent.getStringExtra("command").equals("LOGIN_REQUIRED")){
            if (savedInstanceState == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, new LoginFragment())
                        //.add(R.id.container,new ComposePost() )
                        .commit();
            }}
            else{
                if (savedInstanceState == null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                           // .add(R.id.container, new LoginFragment())
                            .add(R.id.container,new ComposePost() )
                            .commit();
                }
         }
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment f : fragments) {
                if (f instanceof ComposePost) {
                    f.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }



    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

}
