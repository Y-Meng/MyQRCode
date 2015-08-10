using System;
using System.Collections.Generic;
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
        public string Login(string username,string password)
        {
            return dbOperation.LogIn(username, password);
        }
        [WebMethod(Description = "添加一条记录")]
        public bool Uploading(string username,string qrcode,string status,string detial)
        {
            /*string brr = UploadImg(image);*/
            return dbOperation.insertQRInfo(username,qrcode, status, detial);
        }
        [WebMethod(Description = "记录客户端位置")]
        public bool Location(string latitude, string longitude,string username)
        {
            return dbOperation.insertLocation(latitude, longitude,username);
        }
        [WebMethod(Description = "发布任务")]
        public bool Task(string taskID, string taskType, string username, string task)
        {
            return dbOperation.insertTask(taskID, taskType, username, task);
        }
        [WebMethod(Description = "任务查询")]
        public string TaskQuery(string username)
        {
            return dbOperation.taskQuery(username);
        }
        [WebMethod(Description = "图片上传")]
        public string UploadImg(string bytestr)
        {
            string name = "";
            string mess = "";
            string filePath = "";
            try
            {

                name = DateTime.Now.Year.ToString() + DateTime.Now.Month + DateTime.Now.Day + DateTime.Now.Hour + DateTime.Now.Minute + DateTime.Now.Second;
                if (Directory.Exists(Server.MapPath("image\\")) == false)//如果不存在就创建file文件夹
                {
                    Directory.CreateDirectory(Server.MapPath("image\\"));
                }
                bool flag = StringToFile(bytestr, Server.MapPath("image\\") + "" + name + ".jpg");
//                 string Path = Server.MapPath("image\\") + "" + name + ".jpg";
//                 byte[] arr = ConvertToBinary(Path);
                filePath = "/image/" + name + ".jpg";
/*                return arr;*/
            }
            catch (Exception ex)
            {
                mess = ex.Message;
                return mess;
            }
            if (mess != "")
            {
                return mess;
            }
            else
            {
                return "OK";
            }
        }
        public static byte[] ConvertToBinary(string Path)
        {
            FileStream stream = new FileInfo(Path).OpenRead();
            var buffer = new byte[stream.Length];
            stream.Read(buffer, 0, Convert.ToInt32(stream.Length));
            return buffer;
        }

        protected System.Drawing.Image Base64StringToImage(string strbase64)
        {
            try
            {
                byte[] arr = Convert.FromBase64String(strbase64);
                MemoryStream ms = new MemoryStream(arr);
                //Bitmap bmp = new Bitmap(ms);

                ms.Write(arr, 0, arr.Length);
                System.Drawing.Image image = System.Drawing.Image.FromStream(ms);
                ms.Close();
                return image;
                //return bmp;
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }
        /// <summary> 
        /// 把经过base64编码的字符串保存为文件 
        /// </summary> 
        /// <param name="base64String">经base64加码后的字符串 </param> 
        /// <param name="fileName">保存文件的路径和文件名 </param> 
        /// <returns>保存文件是否成功 </returns> 
        public static bool StringToFile(string base64String, string fileName)
        {
            //string path = Path.GetDirectoryName(Assembly.GetExecutingAssembly().GetName().CodeBase) + @"/beapp/" + fileName; 

            System.IO.FileStream fs = new System.IO.FileStream(fileName, System.IO.FileMode.Create);
            System.IO.BinaryWriter bw = new System.IO.BinaryWriter(fs);
            if (!string.IsNullOrEmpty(base64String) && File.Exists(fileName))
            {
                bw.Write(Convert.FromBase64String(base64String));
            }
            bw.Close();
            fs.Close();
            return true;
        }
    }
}