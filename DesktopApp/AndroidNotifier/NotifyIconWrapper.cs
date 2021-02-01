using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AndroidNotifier
{
    public partial class NotifyIconWrapper : Component
    {
        public NotifyIconWrapper()
        {
            InitializeComponent();

            exit.Click += exit_Click;
            notificationTest.Click += notificationTest_Click;
        }

        void notificationTest_Click(object sender, EventArgs e)
        {
            //バルーン通知
            notify("テスト通知です。");
        }

        void exit_Click(object sender, EventArgs e)
        {
            System.Windows.Application.Current.Shutdown();
        }

        public void notify(string text)
        {
            //バルーン通知
            notifyIcon.ShowBalloonTip(5 * 1000, "通知", text, System.Windows.Forms.ToolTipIcon.None);
        }
    }
}
