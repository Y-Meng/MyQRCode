using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Web;
using System.Data.SqlClient;

namespace QRWebService
{
    public class DBOperation:IDisposable
    {
        public static SqlConnection sqlCon;//用来连接数据库

        private String ConServerStr = @"Data Source=PC-21120113011;Initial Catalog=QR Code;User ID=sa;Password=111";
        
        //默认构造函数
        public DBOperation()
        {
            if (sqlCon == null)
            {
                sqlCon = new SqlConnection();
                sqlCon.ConnectionString = ConServerStr;
                sqlCon.Open();
            }
        }
        //关闭函数
        public void Dispose()
        {
            if (sqlCon != null)
            {
                sqlCon.Close();
                sqlCon = null;
            }
        }
        /// <summary>
        /// 添加一条记录
        /// </summary>
        /// <param name="qrcode"></param>
        /// <param name="status"></param>
        /// <param name="detial"></param>
        /// <returns></returns>
        public bool insertQRInfo(string username, string qrcode, string status, string detial)
        {
            try
            {
               /* byte[] arr = Convert.FromBase64String(image);*/
                DateTime Time = System.DateTime.Now;
                string sql = "insert into QRCode(UserName,QRCode,QRStatus,QRDetail,Time) values('"+username+"','" + qrcode + "','" + status + "','" + detial + "','"+Time+"')";
                SqlCommand cmd = new SqlCommand(sql, sqlCon);
                cmd.ExecuteNonQuery();
                cmd.Dispose();

                return true;
            }
            catch (System.Exception ex)
            {
                return false;
            }

        }
        /// <summary>
        /// 登陆验证
        /// </summary>
        /// <param name="username"></param>
        /// <returns></returns>
        public string  LogIn(string username, string password)
        {
            string sql = "SELECT UserType FROM UserTable WHERE UserName='" + username + "' AND Password='" + password +
                            "'";
            SqlCommand cmd = new SqlCommand(sql, sqlCon);
            SqlDataReader reader = cmd.ExecuteReader();
            try
            {
                if (reader.Read())
                {
                    return reader.GetString(0);
                }
                else
                {
                    return "登陆失败";
                }
            }
            catch (Exception e)
            {
                return "登陆失败";
            }
            finally
            {
                reader.Close();
            }
        }
        public bool insertLocation(string latitude, string longitude,string username)
        {
            try
            {
                double Latitude = Convert.ToDouble(latitude);
                double Longitude = Convert.ToDouble(longitude);
                DateTime Time = System.DateTime.Now;
                string sql = "insert into Location(Latitude,Longitude,UserName,Time) values('" + Latitude + "','" + Longitude + "','" + username + "','" + Time + "')";
                SqlCommand cmd = new SqlCommand(sql, sqlCon);
                cmd.ExecuteNonQuery();
                cmd.Dispose();

                return true;
            }
            catch (Exception e)
            {
                return false;
            }
        }

        public bool insertTask(string taskID, string taskType, string username,string task)
        {
            try
            {
                DateTime Time = System.DateTime.Now;
                string sql = "insert into Task(TaskID,TaskType,UserName,Task,Time) values('" + taskID + "','" + taskType +
                             "','" + username + "','" + task + "','" + Time + "')";
                SqlCommand cmd = new SqlCommand(sql, sqlCon);
                cmd.ExecuteNonQuery();
                cmd.Dispose();

                return true;
            }
            catch (Exception e)
            {
                return false;
            }
        }

        public string taskQuery(string username)
        {
            string sql = "SELECT * FROM Task WHERE UserName='" + username + "'";
            SqlCommand cmd = new SqlCommand(sql, sqlCon);
            SqlDataAdapter da = new SqlDataAdapter(cmd);
            DataSet ds = new DataSet();
            da.Fill(ds);
            return DataTable2Json(ds.Tables[0]);
            
        }

        private string DataTable2Json(DataTable dt)
        {
            if (dt != null && dt.Rows.Count > 0)
            {
                StringBuilder jsonBuilder = new StringBuilder();
                jsonBuilder.Append("{");
                jsonBuilder.Append("\"ds\"");
                jsonBuilder.Append(":[");
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