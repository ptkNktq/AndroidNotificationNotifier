using System;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Text.Json;
using System.Text.Json.Serialization;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Forms;

namespace AndroidNotificationNotifier
{
    /// <summary>
    /// Interaction logic for App.xaml
    /// </summary>
    public partial class App : System.Windows.Application
    {
        private NotifyIcon? notifyIcon;

        protected override void OnStartup(StartupEventArgs e)
        {
            base.OnStartup(e);

            SetUpTaskTray();
            StartApp();
        }

        /// <summary>
        /// アプリとしてのメイン機能の実行
        /// </summary>
        private void StartApp()
        {
            Task.Run(() => 
            {
                TcpListener? server = null;
                try
                {
                    var json = File.ReadAllText("./settings.json");
                    var settings = JsonSerializer.Deserialize<Settings>(json);
                    if (settings == null)
                    {
                        throw new Exception("設定ファイルを読み込めませんでした。修正して再起動してください。");
                    }
                    var ep = new IPEndPoint(IPAddress.Any, settings.Port);
                    server = new TcpListener(ep);
                    server.Start();
                    var buff = new byte[1024];
                    while (true)
                    {
                        using TcpClient client = server.AcceptTcpClient();
                        var stream = client.GetStream();
                        int i;
                        while ((i = stream.Read(buff, 0, buff.Length)) != 0)
                        {
                            var message = Encoding.UTF8.GetString(buff);
                            Notify(message);
                        }
                        Array.Clear(buff, 0, buff.Length);
                    }
                }
                catch (Exception e)
                {
                    Notify($"エラー: {e.Message}");
                }
                finally
                {
                    server?.Stop();
                }
            });
        }

        /// <summary>
        /// タスクトレイに表示する準備
        /// </summary>
        private void SetUpTaskTray()
        {
            var menu = new ContextMenuStrip();
            menu.Items.Add("通知テスト", null, OnClickTest);
            menu.Items.Add("終了", null, OnClickExit);
            var appIcon = GetResourceStream(new Uri("Resources/icon.ico", UriKind.Relative)).Stream;
            notifyIcon = new NotifyIcon
            {
                Visible = true,
                Icon = new System.Drawing.Icon(appIcon),
                Text = "通知配達ドロイド君",
                ContextMenuStrip = menu
            };
        }

        /// <summary>
        /// コンテキストメニューの「終了」を選択
        /// </summary>
        private void OnClickExit(object? sender, EventArgs e)
        {
            notifyIcon?.Dispose();
            Shutdown();
        }

        /// <summary>
        /// コンテキストメニューの「通知テスト」を選択
        /// </summary>
        private void OnClickTest(object? sender, EventArgs e)
        {
            Notify("通知テストです。");
        }
    
        private void Notify(string message)
        {
            notifyIcon?.ShowBalloonTip(5 * 1000, "通知", message, ToolTipIcon.None);
        }
    }

    public class Settings
    {
        [JsonPropertyName("port")]
        public int Port { get; set; }
    }
}
