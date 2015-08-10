using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Web;
using System.IO;
using System.Data.SqlClient;
using DotNet.Utilities;
using QR.DAL;
using QR.Model;

namespace QRWebService
{
    public class DBOperation
    {
  
        
        //默认构造函数
        public DBOperation()
        {
 
        }
  
        //插入巡查记录
        public bool InsertQRInfo(string username,string qrcode,string status,string detail,string picname,string b64strPic,string videoname,byte[] video,string audioname,byte[] audio) 
        {
            byte[] imgarr = Convert.FromBase64String(b64strPic);
            DateTime Time = System.DateTime.Now;

            QR.Model.Event mEvent = new QR.Model.Event();
            QR.DAL.Event eventDao = new QR.DAL.Event();
            
            mEvent.UserName = username;
            mEvent.QRcode = qrcode;
            mEvent.QRstatus = status;
            mEvent.QRdetail = detail;
            mEvent.PicName = picname;
            mEvent.Pic = imgarr;
            mEvent.VideoName = videoname;
            mEvent.Video = video;
            mEvent.AudioName = audioname;
            mEvent.Audio = audio;
            mEvent.Time = Time;

            try
            {
                eventDao.Add(mEvent);
                return true;
            }
            catch (Exception e)
            {
                return false;
                throw e;
            }
        }
   
        // 登陆验证
        public string  LogIn(string username, string password)
        {
            DateTime time = System.DateTime.Now;
            try
            {
                QR.DAL.UserTable userDao = new QR.DAL.UserTable();
                QR.Model.UserTable user = new QR.Model.UserTable();
                user = userDao.getUserByName(username);
                if (null != user) 
                {
                    if (password == user.Password)
                    {
                        user.LastLoginTime = time;
                        user.Status = "online";
                        userDao.Update(user);
                        return "登录成功";
                    }
                    else
                    {
                        return "密码错误";
                    }
                }
                else
                {
                        return "用户不存在";
                }
            }
            catch (Exception e)
            {
                throw e;
                return "登陆失败";
            }
  
        }
        //退出
        public bool Exit(string username)
        {

            DateTime time = System.DateTime.Now;
            try
            {
                QR.DAL.UserTable userDao = new QR.DAL.UserTable();
                QR.Model.UserTable user = new QR.Model.UserTable();
                user = userDao.getUserByName(username);
                user.LastExitTime = time;
                user.Status = "offline";
                userDao.Update(user);
                return true;
            }
            catch(Exception e)
            {
                return false;
            }
           
        }
        //插入位置信息
        public bool insertLocation(string latitude, string longitude,string username)
        {
           
            QR.Model.Location location = new QR.Model.Location();
            location.UserName = username;
            location.Latitude =(decimal) Convert.ToDouble(latitude);
            location.Longitude = (decimal)Convert.ToDouble(longitude);
            location.Time = System.DateTime.Now;

            QR.DAL.Location dao = new QR.DAL.Location();
            try
            {
                dao.Add(location);
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
            

        }
        //发布任务
        public bool insertTask(string taskType, string username,string description,string detail)
        {
            //--------------测试数据--------
            string detailjson;
            List<mappoint> _mappoints = new List<mappoint>();

            double X = 40827.686929041425;
            double Y = 3832659.949836881;
            mappoint p1 = new mappoint(39287.9, 3832370.0, 0);
            mappoint p2 = new mappoint(39822.8, 3832610.0, 0);
            mappoint p3 = new mappoint(39541.1, 3833230.0, 0);
            mappoint p4 = new mappoint(39009.6, 3832990.0, 0);
           
            _mappoints.Add(p1);
            _mappoints.Add(p2);
            _mappoints.Add(p3);
            _mappoints.Add(p4);

            //mappoints mps = new mappoints(_mappoints);
            //tring mpstr = ConvertJson.ToJson(mps);

            detailjson = ConvertToJson.ListToJson<mappoint>(_mappoints);
            byte[] buffer = System.Text.ASCIIEncoding.Default.GetBytes(detailjson);

            string base64_detail = Convert.ToBase64String(buffer);
            
            //detailjson = Convert.ToBase64String(detailjson);

            //------------------------------

            QR.Model.Task task = new QR.Model.Task();
            task.TaskType = taskType;
            task.UserName = username;
            task.Description = description;
            task.TaskDetail = base64_detail;
            task.Time = System.DateTime.Now;
            task.TaskStatus = "new";

            QR.DAL.Task dao = new QR.DAL.Task();

            try
            {
                dao.Add(task);
                return true;
            }
            catch (Exception e)
            {
                throw e;
                return false;
            }
        }
        
        //任务查询
        public string taskQuery(string username)
        {
            QR.DAL.Task dao = new QR.DAL.Task();
            DataSet ds = dao.QueryNew(username);
            //查询后应将任务状态设置为doing
            //测试阶段省略
            if (ds.Tables.Count==0)
            {
                return "null";   
            }
            else 
            {
                return DataTable2Json(ds.Tables[0]);
            }
            
        }

        //任务提交
        internal bool taskSubmit(string taskID)
        {
            try
            {
                QR.DAL.Task dao = new QR.DAL.Task();
                QR.Model.Task task = dao.GetModel(Convert.ToInt32(taskID));
                task.TaskStatus = "done";
                dao.Update(task);
                return true;
            }
            catch(Exception e)
            {
             return false;
            }
        }

        //获取图片字符串
        public  string GetIMG(int event_id,string filename)
        {
            try
            {
                byte[] imgarr = new byte[0];
                QR.DAL.Event eventDao = new QR.DAL.Event();
                QR.Model.Event _event = new QR.Model.Event();
                _event = eventDao.GetModel(event_id);
                imgarr = _event.Pic;  
                //直接读取到物理位置
                Transform.BinaryToFile(imgarr, filename);
                //通过内存传输
                //MemoryStream mss = new MemoryStream(imgarr);
                //System.Drawing.Image img = System.Drawing.Image.FromStream(mss,true);
                string img = Convert.ToBase64String(imgarr); 
                return img;  
            }
            catch (Exception e)
            {
                return null;
            }
        }

        //--------------------弃用---------------------
        /*
        public bool InsertMedia(string username,string videoname,byte[] video,string audioname,byte[] audio) 
        {
            
            DateTime Time = System.DateTime.Now;
            string sql = "INSERT INTO Media(UserName,Video,Audio,Time,VideoName,AudioName)VALUES(@username,@video,@audio,@time,@videoname,@audioname)";
            SqlParameter param1 = new SqlParameter("@username", SqlDbType.NVarChar);
            SqlParameter param2 = new SqlParameter("@video", SqlDbType.VarBinary);
            SqlParameter param3 = new SqlParameter("@audio", SqlDbType.VarBinary);
            SqlParameter param4 = new SqlParameter("@time", SqlDbType.Time);
            SqlParameter param5 = new SqlParameter("@videoname", SqlDbType.NVarChar);
            SqlParameter param6 = new SqlParameter("@audioname", SqlDbType.NVarChar);
            
            param1.Value = username;
            param2.Value = video;
            param3.Value = audio;
            param4.Value = Time;
            param5.Value = videoname;
            param6.Value = audioname;
 

            SqlCommand cmd = new SqlCommand(sql, sqlCon);
            cmd.Parameters.Add(param1);
            cmd.Parameters.Add(param2);
            cmd.Parameters.Add(param3);
            cmd.Parameters.Add(param4);
            cmd.Parameters.Add(param5);
            cmd.Parameters.Add(param6);
            try
            {
                cmd.ExecuteNonQuery();
                cmd.Dispose();
                return true;
            }
            catch (Exception e)
            {
                return false;
            }

        }*/
        //-----------------------------------------------------------------------------------------------------

        //数据表转化为json数组
        private string DataTable2Json(DataTable dt)
        {
            
            if (dt != null && dt.Rows.Count > 0)
            {
                StringBuilder jsonBuilder = new StringBuilder();
                jsonBuilder.Append("{");
                jsonBuilder.Append("\"tasks\"");
                jsonBuilder.Append(":[");
                //jsonBuilder.Append("[");
                for (int i = 0; i < dt.Rows.Count; i++)
                {
                    jsonBuilder.Append("{");
                    for (int j = 0; j < dt.Columns.Count; j++)
                    {
                        jsonBuilder.Append("\"" + dt.Columns[j].ColumnName + "\"");
                        jsonBuilder.Append(":\"");
                        jsonBuilder.Append(dt.Rows[i][j].ToString().Trim());
                        jsonBuilder.Append("\",");
                    }
                    jsonBuilder.Remove(jsonBuilder.Length - 1, 1);
                    jsonBuilder.Append("},");
                }
                jsonBuilder.Remove(jsonBuilder.Length - 1, 1);
                jsonBuilder.Append("]");
                jsonBuilder.Append("}");
                return jsonBuilder.ToString();
            }
            else
            {
                return "{\"ds\":{}}";
            }
        }

    }
}