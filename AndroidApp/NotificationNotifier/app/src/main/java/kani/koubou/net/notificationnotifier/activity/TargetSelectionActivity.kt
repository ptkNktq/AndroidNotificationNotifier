package kani.koubou.net.notificationnotifier.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.ListView
import android.widget.Toast
import com.rengwuxian.materialedittext.MaterialEditText
import kani.koubou.net.notificationnotifier.AppInfo
import kani.koubou.net.notificationnotifier.R
import kani.koubou.net.notificationnotifier.SPHelper
import kani.koubou.net.notificationnotifier.adapter.AppInfoAdapter
import java.util.*

class TargetSelectionActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target_selection)

        val handler = Handler()
        val spHelper = SPHelper(PreferenceManager.getDefaultSharedPreferences(applicationContext))
        
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val list = findViewById(R.id.list) as ListView
        val adapter = AppInfoAdapter(applicationContext)
        list.adapter = adapter
        list.setOnItemClickListener() 
        {
            adapterView, view, position, id ->
            val info = adapter.getItem(position)
            val targets = spHelper.getTargets()
            if(targets.split(",").contains(info.packageName))
            {
                Toast.makeText(applicationContext, "${info.name}は追加済みです。", Toast.LENGTH_LONG).show()
                return@setOnItemClickListener
            }
            spHelper.setTargets(if(targets.isNullOrEmpty()) info.packageName else "$targets,${info.packageName}")
            Toast.makeText(applicationContext, "${info.name}を追加しました。", Toast.LENGTH_LONG).show()
            setResult(RESULT_OK)
        }

        val searchText = findViewById(R.id.searchText) as MaterialEditText

        findViewById(R.id.searchButton).setOnClickListener()
        {
            adapter.filter.filter(searchText.text.toString())
        }
        findViewById(R.id.cancelButton).setOnClickListener()
        {
            adapter.filter.filter("")
            searchText.setText("")
        }

        Thread(Runnable()
        {
            val infos = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            val datas = ArrayList<AppInfo>()
            for(info in infos)
            {
                datas.add(AppInfo(info.packageName, packageManager.getApplicationLabel(info).toString(), packageManager.getApplicationIcon(info)))
            }
            handler.post()
            {
                adapter.addAll(datas)
            }
        }).start()
    }
}