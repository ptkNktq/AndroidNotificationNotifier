using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

namespace AndroidNotifier
{
    /// <summary>
    /// App.xaml の相互作用ロジック
    /// </summary>
    public partial class App : Application
    {
        private NotifyIconWrapper component;

        protected override void OnStartup(StartupEventArgs e)
        {
            base.OnStartup(e);

            this.ShutdownMode = System.Windows.ShutdownMode.OnExplicitShutdown;
            component = new NotifyIconWrapper();

            Task.Run(() => {
                var data = new StreamReader("./settings.json").ReadToEnd();
                DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(SettingData));
                using(MemoryStream ms = new MemoryStream(Encoding.UTF8.GetBytes(data)))
                {
                    SettingData setting = (SettingData)serializer.ReadObject(ms);
                    IPEndPoint ep = new IPEndPoint(IPAddress.Any, setting.port);
                    var client = new UdpClient(setting.port);
                    while (true)
                    {
                        var buff = client.Receive(ref ep);
                        var text = Encoding.UTF8.GetString(buff);
                        component.notify(text);
                    }
                }
            });
        }

        protected override void OnExit(ExitEventArgs e)
        {
            base.OnExit(e);
            component.Dispose();
        }
    }

    [DataContract]
    class SettingData
    {
        [DataMember]
        public int port;
    }
}
