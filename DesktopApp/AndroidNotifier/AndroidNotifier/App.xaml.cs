using Codeplex.Data;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
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
                var json = DynamicJson.Parse(data);
                int port = (int) json.port;
                IPEndPoint ep = new IPEndPoint(IPAddress.Any, port);
                var client = new UdpClient(port);
                while(true)
                {
                    var buff = client.Receive(ref ep);
                    var text = Encoding.UTF8.GetString(buff);
                    component.notify(text);
                }
            });
        }

        protected override void OnExit(ExitEventArgs e)
        {
            base.OnExit(e);
            component.Dispose();
        }
    }
}
