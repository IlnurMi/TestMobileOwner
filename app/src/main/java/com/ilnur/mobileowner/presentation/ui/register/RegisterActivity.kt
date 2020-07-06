package com.ilnur.mobileowner.presentation.ui.register

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.ilnur.mobileowner.R
import kotlinx.android.synthetic.main.juridical_register_form.*
import kotlinx.android.synthetic.main.physics_register_form.*
import kotlinx.android.synthetic.main.register_activity.*
import com.ilnur.mobileowner.data.network.ApiClient
import com.ilnur.mobileowner.interfaces.views.register.RegisterView
import com.ilnur.mobileowner.presentation.presenters.register.RegisterPresenter

class RegisterActivity : AppCompatActivity(), RegisterView {

    private lateinit var presenter: RegisterPresenter
    private var juridical: Boolean = false
    private var flagsPhone: Boolean = false
    private var flagsBirthday: Boolean = false
    private var flagsPassport: Boolean = false
    private var flagsAccount: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)
        presenter = RegisterPresenter(
            ProfileInteractor(
                ProfileRepository(
                    ApiClient().getRestClient(this),
                    ApiClient().getTestRestClient(this),
                    ApiClient().getFirebaseRestClient(this)
                )
            )
        )
        presenter.setView(this)
        initToolbar()
        initViews()
        clickListener()
    }

    override fun initToolbar() {
        setSupportActionBar(toolbar_register as Toolbar?)
        var actionBar = supportActionBar
        actionBar!!.title = getString(R.string.toolbar_title_registration)
        actionBar.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

        (toolbar_register as Toolbar?)?.setNavigationOnClickListener { v -> onBackPressed() }
    }

    override fun initViews() {
        val intent = intent
        juridical = intent.getBooleanExtra("juridic", true)
        if (juridical) {
            juridical_face.visibility = View.VISIBLE
        } else {
            physical_face.visibility = View.VISIBLE
        }
    }

    override fun clickListener() {

        btn_register.setOnClickListener {
            if (juridical) {
                presenter.clickRegisterJuridical(
                    juridical_email_edit.text.toString(),
                    juridical_phone_edit.rawText.toString(),
                    juridical_password_edit.text.toString(),
                    juridical_re_password_edit.text.toString(),
                    juridical_name_edit.text.toString(),
                    juridical_address_edit.text.toString(),
                    juridical_bank_account_edit.text.toString(),
                    juridical_inn_edit.text.toString(),
                    juridical_kpp_edit.text.toString(),
                    juridical_ogrn_edit.text.toString(),
                    juridical_description_edit.text.toString()
                )
            } else {
                presenter.clickRegisterPhysical(
                    physical_email_edit.text.toString(),
                    physical_phone_edit.rawText.toString(),
                    physical_password_edit.text.toString(),
                    physical_re_password_edit.text.toString(),
                    physical_name_edit.text.toString(),
                    physical_surname_edit.text.toString(),
                    physical_patronymic_edit.text.toString(),
                    physical_birthday_edit.text.toString(),
                    physical_address_edit.text.toString(),
                    physical_passport_edit.text.toString(),
                    physical_bank_account_edit.text.toString(),
                    physical_company_edit.text.toString()
                )
            }
        }

        chbx_apply.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                btn_register.isEnabled = true
                btn_register.alpha = 1.0F
            } else {
                btn_register.isEnabled = false
                btn_register.alpha = 0.5F
            }
        }

        physical_phone_edit.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {

                    MotionEvent.ACTION_DOWN -> {
                        if (!flagsPhone) {
                            physical_phone_edit.mask = "+7(###)###-##-##"
                            flagsPhone = true
                        }
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }

        })

        physical_birthday_edit.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (!flagsBirthday) {
                            physical_birthday_edit.mask = "##.##.####"
                            flagsBirthday = true
                        }
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        physical_passport_edit.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (!flagsPassport) {
                            physical_passport_edit.mask = "##-## ######"
                            flagsPassport = true
                        }
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        physical_bank_account_edit.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (!flagsAccount) {
                            physical_bank_account_edit.mask = "####-####-####-####"
                            flagsAccount = true
                        }
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        juridical_phone_edit.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (!flagsPhone) {
                            juridical_phone_edit.mask = "+7(###)###-##-##"
                            flagsPhone = true
                        }
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }

        })

        juridical_bank_account_edit.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (!flagsAccount) {
                            juridical_bank_account_edit.mask = "####-####-####-####"
                            flagsAccount = true
                        }
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun errorEmptyFields() {
        Toast.makeText(this, getString(R.string.toast_fields_empty), Toast.LENGTH_SHORT).show()
    }

    override fun errorEmailNotValid() {
        Toast.makeText(this, getString(R.string.toast_email_not_valid), Toast.LENGTH_SHORT).show()
    }

    override fun errorPasswordNotEquals() {
        Toast.makeText(this, getString(R.string.toast_passwords_not_equals), Toast.LENGTH_SHORT)
            .show()
    }

    override fun errorPasswordNotValid(password: String) {
        val builder = AlertDialog.Builder(this@RegisterActivity)
        builder.setTitle("Слишком слабый пароль!")
        builder.setMessage(
            "Такой пароль не допустим!\n " +
                    "Пароль должен состоять  минимум из: \n" +
                    "1. Заглавной буквы\n" +
                    "2. Строчной буквы\n" +
                    "3. Одной цифры\n" +
                    "4. Специального знака(!@#$%)"
        )

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun enableViews(b: Boolean) {
        btn_register.isEnabled = b
        chbx_apply.isEnabled = b
    }

    override fun showHideLoadingView(b: Boolean) {
        if (b) {
            pb_register.visibility = View.VISIBLE
        } else
            pb_register.visibility = View.GONE
    }

    override fun registerSuccess() {
        Toast.makeText(
            this@RegisterActivity,
            getString(R.string.toast_user_register_success),
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    override fun userAlreadyExist() {
        Toast.makeText(
            this@RegisterActivity,
            getString(R.string.toast_user_already_exist),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun registerError() {
        Toast.makeText(
            this@RegisterActivity,
            getString(R.string.toast_register_error),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun tokenError() {}

    override fun errorServer() {
        Toast.makeText(
            this@RegisterActivity,
            getString(R.string.toast_server_error),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun errorNoInternetConnection() {
        Toast.makeText(
            this@RegisterActivity,
            getString(R.string.toast_no_internet_connection),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun showError() {
        Toast.makeText(
            this@RegisterActivity,
            getString(R.string.toast_register_error),
            Toast.LENGTH_SHORT
        ).show()
    }
}