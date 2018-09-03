package mobile.omandotkom.dakwahsosial;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mobile.omandotkom.dakwahsosial.data.User;
import mobile.omandotkom.dakwahsosial.network.RequestMaker;

/**
 * Fragment representing the login screen for Shrine.
 */
public class LoginFragment extends Fragment {
private final String TAG  = "LOGINFRAGMENT";
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ds_fragment_login, container, false);

        // Snippet from "Navigate to the next Fragment" section goes here.
        final TextInputLayout passwordTextInput = view.findViewById(R.id.password_text_input);
        final TextInputEditText passwordEditText = view.findViewById(R.id.passwordEditText);
        final TextInputLayout usernameTextInput = view.findViewById(R.id.username_text_input);
        final TextInputEditText usernameEditText = view.findViewById(R.id.usernameEditText);
        MaterialButton nextButton = view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPasswordValid(passwordEditText.getText())) {
                    passwordTextInput.setError(getString(R.string.shr_error_password));
                } else {
                    Log.d(TAG,usernameEditText.getText().toString() + passwordEditText.getText().toString());
                    passwordTextInput.setError(null); // Clear the error
                  //  ((NavigationHost) getActivity()).navigateTo(new ProductGridFragment(), false); // Navigate to the next Fragment
                    User user = new User(getContext());
                    user.setUsername(usernameEditText.getText().toString());
                    user.setPassword(passwordEditText.getText().toString());
                    RequestMaker requestMaker = new RequestMaker(user);
                    requestMaker.login();
                }
            }
        });

        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (isPasswordValid(passwordEditText.getText())) {
                    passwordTextInput.setError(null); //Clear the error
                }
                return false;
            }
        });
        return view;
    }
    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 8;
    }

    // "isPasswordValid" from "Navigate to the next Fragment" section method goes here
}
