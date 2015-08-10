using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Web;
using System.Web.Services;

namespace QRWebService
{
    /// <summary>
    /// Service1 的摘要说明
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // 若要允许使用 ASP.NET AJAX 从脚本中调用此 Web 服务，请取消对下行的注释。
    // [System.Web.Script.Services.ScriptService]
    public class Service1 : System.Web.Services.WebService
    {
        DBOperation dbOperation = new DBOperation();

        [WebMethod(Description = "用户登陆验证")]
        public string UserLogin(string username,string password)
        {
            return dbOperation.LogIn(username, password);
        }

        [WebMethod(Description = "退出登录")]
        public bool UserExit(string username)
        {
            return dbOperation.Exit(username);
        }

        [WebMethod(Description = "记录客户端位置")]
        public bool LocationTrace(string latitude, string longitude,string username)
        {
            return dbOperation.insertLocation(latitude, longitude,username);
        }

        [WebMethod(Description = "任务发布")]
        public bool TaskPublish(string taskType, string username, string description,string detail)
        {
            return dbOperation.insertTask(taskType, username, description,detail);
        }

        [WebMethod(Description = "任务查询")]
        public string TaskQuery(string username)
        {
            return dbOperation.taskQuery(username);
        }

        [WebMethod(Description = "任务提交")]
        public bool TaskSubmit(string taskID)
        {
            return dbOperation.taskSubmit(taskID);
        }

        [WebMethod(Description = "添加一条反馈记录")]
        public bool EventSubmit(string username, string qrcode, string status, string detail, string picname, string b64strPic, string videoname, byte[] video, string audioname, byte[] audio)
        {
            //FileStream fs = new FileStream(Server.MapPath("image\\") + "201461604834.jpg",FileMode.Open,FileAccess.Read);
            //byte[] imgarr =  new byte[fs.Length];
            //fs.Read(imgarr,0,(Int32)fs.Length);
            //fs.Close();
            //string b64str = Convert.ToBase64String(imgarr);

            return dbOperation.InsertQRInfo(username, qrcode, status, detail, picname, b64strPic, videoname, video, audioname, audio);
        }

        [WebMethod(Description = "获取图片(字符串编码)")]
        public string GetIMG(int  event_id)
        {
            string imgb64str = null;
            string fileurl = Server.MapPath("image\\") + "1.jpg";
            imgb64str = dbOperation.GetIMG(event_id,fileurl);
            if (imgb64str != null)
            {
                return imgb64str;
            }
            else
            {
                return "无";
            }
        }

        [WebMethod(Description = "请求数据")]
        public string RequestData(string requestJson) 
        {
            GISOperation gisOperation = new GISOperation();
            return gisOperation.RequestData(requestJson);
        }

    }
}