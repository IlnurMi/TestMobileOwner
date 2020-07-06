package com.ilnur.mobileowner.presentation.ui.login

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.ilnur.mobileowner.R
import kotlinx.android.synthetic.main.activity_login.*
import com.ilnur.mobileowner.presentation.ui.main.MainActivity
import com.ilnur.mobileowner.data.network.ApiClient
import com.ilnur.mobileowner.data.utils.NavigationUtil
import com.ilnur.mobileowner.data.preferences.PreferenceRepository
import com.ilnur.mobileowner.domain.models.response.Token
import com.ilnur.mobileowner.interfaces.views.login.LoginView
import com.ilnur.mobileowner.presentation.presenters.login.LoginPresenter
import com.ilnur.mobileowner.presentation.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity(), LoginView {

    private lateinit var presenter: LoginPresenter
    private lateinit var btnAnimation: Animation
    lateinit var backAnimation: Animation
    private var check: Boolean = false
    private val REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        presenter = LoginPresenter(
            ProfileInteractor(
                ProfileRepository(
                    ApiClient().getRestClient(this),
                    ApiClient().getTestRestClient(this),
                    ApiClient().getFirebaseRestClient(this)
                )
            )
        )
        presenter.setView(this)
        checkAuthorization()
        clickListeners()
        btnAnimation = AnimationUtils.loadAnimation(this, R.anim.move_btn)
        backAnimation = AnimationUtils.loadAnimation(this, R.anim.back_move_btn)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }

    // get Firebase token
    private fun getToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val token = task.result?.token
                if (token != PreferenceRepository.INSTANCE.getToken()) {
                    PreferenceRepository.INSTANCE.saveToken(token.toString())
                }
            })
    }

    override fun checkAuthorization() {
        if (PreferenceRepository.INSTANCE.isAuthorised()) {
            loginGetToken()
            NavigationUtil.instance.invokeActivityWithoutData(
                this@LoginActivity,
                MainActivity::class.java,
                true
            )
        }
    }

    override fun clickListeners() {
        btn_enter.setOnClickListener {
            presenter.clickAuthorizeButton(et_email.text.toString(), et_password.text.toString())
        }

        tv_forget_password.setOnClickListener {
            //TODO handle start recovery activity
        }

        btn_register.setOnClickListener {
            presenter.clickRegister(false)
        }

        physic_btn.setOnClickListener {
            presenter.clickRegister(false)
        }
        juridical_btn.setOnClickListener {
            presenter.clickRegister(true)
        }

        base_layout_login.setOnClickListener {
            if (check) {
                liner_layout.startAnimation(backAnimation)
                liner_layout.isEnabled = false
                btn_register.isEnabled = true
                check = false
            }
        }

    }


    override fun loginGetToken() {
        presenter.getToken()
    }

    override fun startBaseActivity() {
        NavigationUtil.instance.invokeActivityWithoutData(
            this@LoginActivity,
            MainActivity::class.java,
            true
        )
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun saveEmailPassword(email: String, password: String) {
        PreferenceRepository.INSTANCE.saveEmailPassword(email, password)
    }

    override fun showFieldsIsEmpty() {
        Toast.makeText(this, getString(R.string.toast_fields_empty), Toast.LENGTH_SHORT).show()
    }

    override fun showEmailNotValid() {
        Toast.makeText(this, getString(R.string.toast_email_not_valid), Toast.LENGTH_SHORT).show()
    }

    override fun showLoadingProgressBar(show: Boolean) {
        if (show)
            progressBar_login.visibility = View.VISIBLE
        else
            progressBar_login.visibility = View.GONE
    }

    override fun showLoginOrPasswordErrorOnServer() {
        Toast.makeText(
            this,
            getString(R.string.toast_login_or_password_not_valid),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun showUserNotRegisteredOnServer() {
        Toast.makeText(this, getString(R.string.toats_user_not_registered), Toast.LENGTH_SHORT)
            .show()
    }

    override fun errorNoInternetConnection() {
        Toast.makeText(this, getString(R.string.toast_no_internet_connection), Toast.LENGTH_SHORT)
            .show()
    }

    override fun loginSuccess(token: Token?) {
        PreferenceRepository.INSTANCE.saveToken(token?.accessToken!!)
        PreferenceRepository.INSTANCE.setAuthorized(true)
        NavigationUtil.instance.invokeActivityWithoutData(
            this@LoginActivity,
            MainActivity::class.java,
            true
        )
    }

    override fun loginSuccessSaveToken(token: Token?) {
        PreferenceRepository.INSTANCE.saveToken(token?.accessToken!!)
    }

    override fun goToRegister(email: String) {
        NavigationUtil.instance.invokeActivityWithData(
            this,
            RegisterActivity::class.java,
            email,
            false
        )
    }

    override fun register(juridic: Boolean) {
        NavigationUtil.instance.invokeActivityRegister(
            this,
            RegisterActivity::class.java, juridic, false
        )
    }

    override fun onRestart() {
        super.onRestart()
        liner_layout.visibility == View.GONE
    }

    override fun onStart() {
        super.onStart()
        liner_layout.visibility == View.GONE
    }

    override fun onResume() {
        super.onResume()
        liner_layout.visibility == View.GONE
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this@LoginActivity,
            arrayOf(android.Manifest.permission.READ_PHONE_STATE),
            REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG", "Permission has been denied by user")
                } else {
                    Log.d("TAG", "Permission has been granted by user")
                }
            }
        }
    }

    override fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }
}

