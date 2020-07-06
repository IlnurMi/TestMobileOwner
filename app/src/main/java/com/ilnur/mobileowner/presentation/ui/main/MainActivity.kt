package com.ilnur.mobileowner.presentation.ui.main

import android.annotation.SuppressLint
import android.app.*
import android.content.*
import android.graphics.BitmapFactory
import android.os.*
import android.util.Base64
import androidx.core.view.GravityCompat
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.ilnur.mobileowner.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import com.ilnur.mobileowner.data.preferences.PreferenceRepository
import com.ilnur.mobileowner.data.utils.ConstantUtils
import com.ilnur.mobileowner.data.utils.NavigationUtil
import com.ilnur.mobileowner.interfaces.*
import com.ilnur.mobileowner.domain.models.response.route.CarOnMap
import com.ilnur.mobileowner.interfaces.views.ReplaceFragmentInterface
import com.ilnur.mobileowner.interfaces.views.main.MainView
import com.ilnur.mobileowner.presentation.presenters.main.MainPresenter
import com.ilnur.mobileowner.presentation.ui.login.LoginActivity
import com.ilnur.mobileowner.presentation.ui.map.MyMapFragment
import java.io.File

class MainActivity : AppCompatActivity(), MainView, NavigationView.OnNavigationItemSelectedListener,
    UpdaterInterface, ServiceCallbacks, FragmentsListener {

    private var presenter: MainPresenter? = null
    private var fragmentManager: FragmentManager? = null
    lateinit var actionBar: ActionBar
    private lateinit var tvTitleFio: TextView
    private lateinit var tvTitleOrganization: TextView
    private lateinit var navigationView: NavigationView
    private var homeShouldOpenDrawer: Boolean = false
    private lateinit var drawerLayout: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter()
        presenter?.setView(this)
        fragmentManager = supportFragmentManager
        initToolbar()
        initNavigationDrawer()

        createNotificationChannel(
            applicationContext,
            NotificationManagerCompat.IMPORTANCE_HIGH,
            true,
            getString(R.string.app_name),
            getString(R.string.app_notifi)
        )

        addFragment(MyMapFragment.getInstance())
        clickListeners()
    }

    @SuppressLint("StringFormatInvalid")
    override fun onStart() {
        super.onStart()

        //TODO start services
        //startService(Intent(applicationContext, MyService::class.java))


        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
    }


    override fun initToolbar() {
        setSupportActionBar(toolbar_main)
        actionBar = this.supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_open_menu)
        actionBar.title = getString(R.string.menu_title_map)
        homeShouldOpenDrawer = true
    }

    private fun initNavigationDrawer() {

        drawerLayout = drawer_layout

        navigationView = navigation_view
        navigationView.setNavigationItemSelectedListener(this)

        tvTitleFio = navigationView.getHeaderView(0).findViewById(R.id.tv_title_fio)
        tvTitleFio.text = PreferenceRepository.INSTANCE.getRegisterFIO()

        tvTitleOrganization =
            navigationView.getHeaderView(0).findViewById(R.id.tv_title_organization)
        tvTitleOrganization.text = PreferenceRepository.INSTANCE.getOrganization()

        val circleImageView: CircleImageView =
            navigationView.getHeaderView(0).findViewById(R.id.iv_user_image)
        if (PreferenceRepository.INSTANCE.getPhoto().isNotEmpty()) {
            val decodeString =
                Base64.decode(PreferenceRepository.INSTANCE.getPhoto(), Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.size)
            circleImageView.setImageBitmap(decodedByte)
        } else {
            circleImageView.setImageResource(R.drawable.ic_manager)
        }

        circleImageView.setOnClickListener {
            actionBar?.title = getString(R.string.toolbar_title_profile)
            searchView.visibility = View.GONE
            presenter?.drawerItemClick(ConstantUtils.DRAWER_PROFILE)
            drawer_layout?.closeDrawer(GravityCompat.START)
        }
    }

    @Override
    override fun updateNavigationView() {
        tvTitleFio.text = PreferenceRepository.INSTANCE.getRegisterFIO()
        tvTitleOrganization.text = PreferenceRepository.INSTANCE.getOrganization()
    }

    private fun createNotificationChannel(
        context: Context,
        importance: Int,
        showBadge: Boolean,
        name: String,
        descriprtion: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = descriprtion
            channel.setShowBadge(showBadge)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun messageCallbacks(message: String, date: String) {
        //TODO callbacks add message to MessageAdapter
    }

    override fun clickListeners() {

        searchView.setOnCloseListener {
            var fm = supportFragmentManager
            var textChangeListener: TextChangeListener? = null

            for (fragment in fm.fragments) {
                if (fragment is TextChangeListener) {
                    textChangeListener = fragment
                    break
                }
            }
            if (textChangeListener != null) {
                textChangeListener!!.closeSearchView()
            }
            false
        }

        searchView.setOnQueryTextFocusChangeListener { p0, p1 ->
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                var fm = supportFragmentManager
                var textChangeListener: TextChangeListener? = null

                for (fragment in fm.fragments) {
                    if (fragment is TextChangeListener) {
                        textChangeListener = fragment
                        break
                    }
                }
                textChangeListener?.closeList()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var fm = supportFragmentManager
                var textChangeListener: TextChangeListener? = null

                for (fragment in fm.fragments) {
                    if (fragment is TextChangeListener) {
                        textChangeListener = fragment
                        break
                    }
                }
                if (textChangeListener != null) {
                    textChangeListener!!.textChange(newText.toString())
                }
                return true
            }
        })
    }

    override fun addFragment(fragment: DrawerFragmentInterface) {
        fragmentManager
            ?.beginTransaction()
            ?.add(R.id.fragment_main, fragment as Fragment)
            ?.commit()
    }

    override fun enableViews(enable: Boolean) {
        homeShouldOpenDrawer = if (enable) {
            actionBar.setHomeAsUpIndicator(null)
            false
        } else {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_open_menu)
            true
        }
    }

    override fun replaceFragmentWithArrow(fragment: ReplaceFragmentInterface) {
        actionBar.setHomeAsUpIndicator(null)
        homeShouldOpenDrawer = false

        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.fragment_main, fragment as Fragment)
            ?.commit()
    }

    override fun showDialogKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    override fun onNavigationItemSelected(meniItem: MenuItem): Boolean {
        when (meniItem.itemId) {
            R.id.item_map -> {
                actionBar?.title = getString(R.string.menu_title_map)
                searchView.visibility = View.VISIBLE
                searchView.onActionViewCollapsed()
                presenter?.drawerItemClick(ConstantUtils.DRAWER_MAP)
            }

            R.id.item_message -> {
                // message fragment
            }
            R.id.item_drivers -> {
                // driver fragment
            }

            R.id.item_reports -> {
                actionBar?.title = "Отчеты"
                searchView.visibility = View.GONE
                searchView.isIconified = true
                presenter?.drawerItemClick(ConstantUtils.DRAWER_REPORT)
            }
            R.id.item_settings -> {
                actionBar?.title = "Настройки"
                searchView.visibility = View.GONE
                searchView.isIconified = true
                presenter?.drawerItemClick(ConstantUtils.DRAWER_SETTINGS)
            }
        }
        (drawer_layout as DrawerLayout).closeDrawer(GravityCompat.START)
        return true
    }

    override fun replaceFragment(fragment: DrawerFragmentInterface) {
        actionBar.show()
        actionBar.setHomeAsUpIndicator(R.drawable.ic_open_menu)
        homeShouldOpenDrawer = true
        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.fragment_main, fragment as Fragment)
            ?.commit()
    }

    override fun refreshFragment(fragment: DrawerFragmentInterface) {
        fragmentManager
            ?.beginTransaction()
            ?.detach(fragment as Fragment)
            ?.attach(fragment as Fragment)
            ?.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                if (homeShouldOpenDrawer)
                    drawerLayout.openDrawer(GravityCompat.START)
                else
                    onBackPressed()
                return true
            }
            else -> onOptionsItemSelected(item)
        }
    }

    override fun onItemSelected(car: CarOnMap) {
        searchView.visibility = View.GONE
        var arguments = Bundle()
        arguments.putParcelable("car", car)
        // replace fragment
    }

    override fun showCarOnMap(car: CarOnMap) {
        var bundle = Bundle()
        bundle.putParcelable("carId", car)
        MyMapFragment.getInstance().arguments = bundle
        replaceFragment(MyMapFragment.getInstance())
    }

    override fun refreshActivity() {
        onResume()
    }

    override fun joinUserOnline(userId: String) {
        //mServer.joinUserOnline(userId)
    }

    override fun sendMessage(message: String, recipientId: String, senderId: String, date: String) {
        //TODO send message at Hub
        //mServer.sendMessage(message, recipientId, senderId, PreferenceRepository.INSTANCE.getRegisterFIO(), date, PreferenceRepository.INSTANCE.getPhoto())
    }

    override fun onBackPressed() {
        var fm = supportFragmentManager
        var backPressedInterface: BackPressedInterface? = null

        for (fragment in fm.fragments) if (fragment is BackPressedInterface) {
            backPressedInterface = fragment
            break
        }

        if (backPressedInterface != null)
            backPressedInterface.onBackPressed()
        else
            showCloseAlert()
    }

    private fun showCloseAlert() {
        val quitDialog = AlertDialog.Builder(this@MainActivity)
        quitDialog.setTitle(getString(R.string.alert_dialog_title_quit))

        quitDialog.setPositiveButton(getString(R.string.text_yes)) { _, _ ->
            try {
                finish()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        quitDialog.setNegativeButton(getString(R.string.text_no)) { _, _ -> }
        quitDialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        for (frag in supportFragmentManager.fragments) {
            frag?.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            var v: View? = null
            if (currentFocus != null) v = currentFocus
            Log.d("mT", "C:: $currentFocus")
            if (v is EditText) {
                v.clearFocus()
                //searchView.clearFocus()
                Log.d("mT", "V:: $v")
                hideKeyboard()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    fun clearFocusOnSearchView() {
        searchView.clearFocus()
    }

    override fun hideKeyboard() {
        var imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        //TODO unbindService after Destroy MainActivity
        /*if (mConnection != null) {
            unbindService(mConnection!!)
        }*/
    }

    override fun setActionBarTitle(title: String) {
        actionBar.title = title
    }

    override fun replaceDeepFragment(fragment: ReplaceFragmentInterface) {
        replaceFragmentWithArrow(fragment)
    }

    override fun replaceBaseFragment(fragment: DrawerFragmentInterface) {
        replaceFragment(fragment)
    }

    override fun itemSelect(carOnMap: CarOnMap) {
        searchView.visibility = View.GONE
        var arguments = Bundle()
        arguments.putParcelable("car", carOnMap)
        // set fragments argument and replace fragment
    }

    override fun saveTextToClipBoard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip: ClipData? = ClipData.newPlainText("", text)
        clip?.let { clipboard?.setPrimaryClip(it) }
        showToast(getString(R.string.copy_coordinate))
    }

    override fun showCloseDialog() {
        val quitDialog = AlertDialog.Builder(this)
        quitDialog.setTitle(getString(R.string.alert_dialog_title_quit))

        quitDialog.setPositiveButton(getString(R.string.text_yes)) { _, _ ->
            try {
                NavigationUtil.instance.invokeActivityWithFlags(
                    this, LoginActivity::class.java, true
                )
                //                finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        quitDialog.setNegativeButton(getString(R.string.text_no)) { _, _ -> }

        quitDialog.show()
    }

    override fun createNotification(fileName: String) {
        val channelId = "${this.packageName}-${this.getString(R.string.app_name)}"
        val notificationBuilder = NotificationCompat.Builder(this, channelId).apply {
            setSmallIcon(R.drawable.ic_report)
            setContentTitle(getString(R.string.download_report))
            setContentText(fileName)
            priority = NotificationCompat.PRIORITY_MAX
            setAutoCancel(true)

            var sdPath = Environment.getExternalStorageDirectory()
            sdPath = File(sdPath.absolutePath + "/" + getString(R.string.download_file_name) + "/" + fileName)

            var uri = FileProvider.getUriForFile(
                applicationContext,
                "com.ilnur.mobileowner.provider",
                sdPath
            )

            var intent = Intent(Intent.ACTION_VIEW)
            intent.action = Intent.ACTION_VIEW
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            intent.data = uri

            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            setContentIntent(pendingIntent)
        }

        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.notify(10, notificationBuilder.build())
    }

    override fun selectedUserChat(id: String) {
        // replace fragment
    }

    override fun showArrow() {
        actionBar.setHomeAsUpIndicator(null)
        homeShouldOpenDrawer = false
    }

    override fun showHome() {
        actionBar.setHomeAsUpIndicator(R.drawable.ic_open_menu)
        homeShouldOpenDrawer = true
    }

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun showSearchView(show: Boolean) {
        if (show)
            searchView.visibility = View.VISIBLE
        else
            searchView.visibility = View.GONE
    }

}

