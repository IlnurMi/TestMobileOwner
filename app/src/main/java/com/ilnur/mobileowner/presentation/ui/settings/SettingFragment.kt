package com.ilnur.mobileowner.presentation.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ilnur.mobileowner.R
import kotlinx.android.synthetic.main.settings_fragment.*
import com.ilnur.mobileowner.interfaces.DrawerFragmentInterface
import com.ilnur.mobileowner.interfaces.FragmentsListener
import com.ilnur.mobileowner.interfaces.views.settings.SettingsContract
import com.ilnur.mobileowner.presentation.presenters.settings.SettingPresenter
import java.lang.ClassCastException

class SettingFragment : Fragment(), DrawerFragmentInterface, SettingsContract.View {

    private lateinit var fragmentsListener: FragmentsListener
    private lateinit var presenter: SettingsContract.Presenter

    companion object {
        private var INSTANCE: SettingFragment? = null

        fun getInstance(): SettingFragment {
            if (INSTANCE != null) {
                return INSTANCE!!
            }
            INSTANCE = SettingFragment()
            return INSTANCE!!
        }

        fun newInstance(): SettingFragment {
            INSTANCE = SettingFragment()
            return INSTANCE!!
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            fragmentsListener = context as FragmentsListener
        } catch (e: ClassCastException) {
            //TODO handle error
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = SettingPresenter()
        presenter.setView(this)

        clickListeners()
    }


    override fun clickListeners() {

        exit.setOnClickListener { presenter.exitItemClicked() }

        private_date.setOnClickListener { presenter.privateItemClicked() }

        interface_date.setOnClickListener { presenter.designItemClicked() }

        push_sound.setOnClickListener { presenter.soundItemClicked() }

        language.setOnClickListener { presenter.languageItemClicked() }

        help.setOnClickListener { presenter.helpItemClicked() }
    }

    override fun showExitDialog() {
        fragmentsListener.showCloseDialog()
    }

    override fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}