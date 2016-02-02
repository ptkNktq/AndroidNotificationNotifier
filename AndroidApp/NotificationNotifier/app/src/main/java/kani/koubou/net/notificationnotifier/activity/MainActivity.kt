package kani.koubou.net.notificationnotifier.activity

import android.content.Intent
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
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class MainActivity : AppCompatActivity()
{
    val handler = Handler()
    var adapter : AppInfoAdapter? = null
    var spHelper : SPHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spHelper = SPHelper(PreferenceManager.getDefaultSharedPreferences(applicationContext))

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val ipText = findViewById(R.id.ipText) as MaterialEditText
        ipText.setText(spHelper!!.getIp())
        ipText.setSelection(ipText.length())

        val portText = findViewById(R.id.portText) as MaterialEditText
        portText.setText(spHelper!!.getPort().toString())
        portText.setSelection(portText.length())

        val list = findViewById(R.id.list) as ListView
        adapter = AppInfoAdapter(applicationContext)
        list.adapter = adapter
        list.setOnItemClickListener()
        {
            adapterView, view, position, id ->
            val info = adapter!!.getItem(position)
            spHelper!!.setTargets(spHelper!!.getTargets().replace(Regex(",?${info.packageName}"), ""))
            Toast.makeText(applicationContext, "${info.name}を削除しました。", Toast.LENGTH_LONG).show()
            loadTargets()
        }
        loadTargets()

        findViewById(R.id.ipSaveButton).setOnClickListener()
        {
            spHelper!!.setIp(ipText.text.toString())
        }
        findViewById(R.id.portSaveButton).setOnClickListener()
        {
            spHelper!!.setPort(portText.text.toString().toInt())
        }
        findViewById(R.id.testButton).setOnClickListener()
        {
            Thread(Runnable()
            {
                try
                {
                    val buff = "通知テスト".toByteArray()
                    val addr = InetAddress.getByName(spHelper!!.getIp())
                    val packet = DatagramPacket(buff, buff.size, addr, spHelper!!.getPort())
                    val socket = DatagramSocket()
                    socket.send(packet)
                    socket.close()
                    handler.post()
                    {
                        Toast.makeText(applicationContext, "送信しました。", Toast.LENGTH_LONG).show()
                    }
                }
                catch(e: Exception)
                {
                    e.printStackTrace()
                    handler.post()
                    {
                        Toast.makeText(applicationContext, "エラー：${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }).start()
        }
        findViewById(R.id.fab).setOnClickListener()
        {
            startActivityForResult(Intent(applicationContext, TargetSelectionActivity::class.java), 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        loadTargets()
    }

    fun loadTargets()
    {
        adapter!!.clear()
        Thread(Runnable()
        {
            val targets = spHelper!!.getTargets().split(",")
            val infos = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            for(info in infos)
            {
                if(!targets.contains(info.packageName))
                {
                    continue
                }
                handler.post()
                {
                    adapter!!.add(AppInfo(info.packageName, packageManager.getApplicationLabel(info).toString(), packageManager.getApplicationIcon(info)))
                }
            }
        }).start()
    }
}
