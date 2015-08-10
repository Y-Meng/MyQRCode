using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.IO;

namespace QRWebService
{
    public class Transform
    {
        //文件转换成byte数组
        public static byte[] FileToBinary(string Path)
        {
            FileStream stream = new FileInfo(Path).OpenRead();
            var buffer = new byte[stream.Length];
            stream.Read(buffer, 0, Convert.ToInt32(stream.Length));
            return buffer;
        }

        //byte数组转化为base64string
        public static string BinaryToBase64String(byte[] arr)
        {
            return Convert.ToBase64String(arr);//压缩存贮，并不是直接toString（）
        }

        //base64编码的字符串转化为byte数组
        public static byte[] Base64StringToBinary(string base64str)
        {
            return Convert.FromBase64String(base64str);
        }

        //byte数组转化成文件
        public static bool BinaryToFile(byte[] arr, string fileurl)
        {
            FileStream fs = new FileStream(fileurl, FileMode.OpenOrCreate, FileAccess.Write);
            BinaryWriter bw = new BinaryWriter(fs);
            try
            {
                bw.BaseStream.Write(arr, 0, arr.Length);
                bw.Flush();
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
            finally
            {
                bw.Close();
                fs.Close();
            }
        }

        //base64编码的字符串转化为图片 方法1
        protected System.Drawing.Image Base64StringToImage(string strbase64)
        {
            try
            {
                //先解码为二进制数组
                byte[] arr = Convert.FromBase64String(strbase64);
                MemoryStream ms = new MemoryStream(arr);
                ms.Write(arr, 0, arr.Length);
                System.Drawing.Image image = System.Drawing.Image.FromStream(ms);
                ms.Close();
                return image;
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }
        /// <summary> 
        /// 把经过base64编码的字符串保存为文件 方法2
        /// </summary> 
        /// <param name="base64String">经base64加码后的字符串 </param> 
        /// <param name="fileName">保存文件的路径和文件名 </param> 
        /// <returns>保存文件是否成功 </returns> 
        public static bool StringToFile(string base64String, string fileName)
        {
            //string path = Path.GetDirectoryName(Assembly.GetExecutingAssembly().GetName().CodeBase) + @"/beapp/" + fileName; 

            System.IO.FileStream fs = new System.IO.FileStream(fileName, System.IO.FileMode.OpenOrCreate);
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