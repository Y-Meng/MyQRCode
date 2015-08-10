using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Gma.QrCodeNet.Encoding;
using Gma.QrCodeNet.Encoding.Windows.Render;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using QRWebService.Model;
using DotNet.Utilities;
using Newtonsoft.Json;

namespace QRWebService
{
    public class GISOperation
    {
        public GISOperation()
        {
 
        }

        public string RequestData(string json)
        {
            EmergencyRequest request;
            request = JsonConvert.DeserializeObject<EmergencyRequest>(json);

            switch (request.Mode)
            {
                case 1:
                    return GetResultsByLocation(request);
                     
                case 0:
                    return GetResultsByQrCode(request);
                
                default:
                    return "";
            }    
        }
        private string GetResultsByLocation(EmergencyRequest request) 
        {
            List<EmergencyResult> results = new List<EmergencyResult>();

            if (request.isMAP == 1)
            {
                results.Add( GetMAP(request.X,request.Y,request.Buffer));
            }

            if (request.isHDM == 1)
            {
                results.Add(GetHDM(request.X,request.Y,request.Buffer));
            }

            if (request.isZDM == 1)
            {
                results.Add(GetZDM(request.X, request.Y, request.Buffer));
            }

            if (request.is3D == 1)
            {
                results.Add(Get3D(request.X, request.Y, request.Buffer));
            }

            string jsonResults = JsonConvert.SerializeObject(results);
            
            return "{\"results\":"+jsonResults+"}";
        }

        private string GetResultsByQrCode(EmergencyRequest request)
        {
            //
            //需要先通过二维码确定location
            //
            string QrCode = request.QrCode;
            Point location =  GetLocationByQrCode(QrCode);
            
            

            //
            List<EmergencyResult> results = new List<EmergencyResult>();

            if (request.isMAP == 1)
            {
                results.Add(GetMAP(location.X, location.Y, request.Buffer));
            }

            if (request.isHDM == 1)
            {
                results.Add(GetHDM(location.X, location.Y, request.Buffer));
            }

            if (request.isZDM == 1)
            {
                results.Add(GetZDM(location.X, location.Y, request.Buffer));
            }

            if (request.is3D == 1)
            {
                results.Add(Get3D(location.X, location.Y, request.Buffer));
            }

            string jsonResults = JsonConvert.SerializeObject(results);

            return jsonResults;
        }

        private Point GetLocationByQrCode(string code)
        {
            Point location = new Point();
            //获取位置
            



            return location;
        }

        #region 生成数据
        private EmergencyResult GetMAP(double x,double y,double buffer)
        {
            EmergencyResult result = new EmergencyResult();
            result.FileName = "MAP_2582013222.jpg";
            result.DataType = "MAP";
            result.FileType = "jpg";
            //result.FileURL = "http://f.hiphotos.baidu.com/image/h%3D1080%3Bcrop%3D0%2C0%2C1920%2C1080/sign=79b9514aa6c27d1eba263fc423e5960d/2e2eb9389b504fc278e453ebe7dde71191ef6da1.jpg";
            result.FileURL = "http://192.168.191.1/QRWebService/upload/1.jpg";
            result.FileDescription = "综合管线平面图-密";
            return result;
        }

        private EmergencyResult GetZDM(double x, double y, double buffer)
        {
            EmergencyResult result = new EmergencyResult();
            result.FileName = "ZDM_2582013211.jpg";
            result.DataType = "ZDM";
            result.FileType = "jpg";
            //result.FileURL = "http://e.hiphotos.baidu.com/image/h%3D1080%3Bcrop%3D0%2C0%2C1920%2C1080/sign=b6a5cf530ef431ada3d24739730697cc/03087bf40ad162d9f6c148dd13dfa9ec8b13cdc7.jpg";
            result.FileURL = "http://192.168.191.1/QRWebService/upload/2.jpg";
            result.FileDescription = "综合管线纵断面图-密";
            return result; 
        }

        private EmergencyResult GetHDM(double x, double y, double buffer)
        {
            EmergencyResult result = new EmergencyResult();
            result.FileName = "HDM_2582013232.jpg";
            result.DataType = "HDM";
            result.FileType = "jpg";
            //result.FileURL = "http://b.hiphotos.baidu.com/image/h%3D1080%3Bcrop%3D0%2C0%2C1920%2C1080/sign=e3f8d2e0708b4710d12ff9ccfbfef89e/2fdda3cc7cd98d1058b026f0233fb80e7aec908e.jpg";
            result.FileURL = "http://192.168.191.1/QRWebService/upload/3.jpg";
            result.FileDescription = "综合管线横断面图-密";
            return result; 
        }

        private EmergencyResult Get3D(double x, double y, double buffer)
        {
            EmergencyResult result = new EmergencyResult();
            result.FileName = "3D_2582013542.jpg";
            result.DataType = "3D";
            result.FileType = "jpg";
            //result.FileURL = "http://b.hiphotos.baidu.com/image/w%3D1920%3Bcrop%3D0%2C0%2C1920%2C1080/sign=84e91d183b01213fcf334ad566d70db2/0bd162d9f2d3572c1826b51d8813632763d0c32e.jpg";
            result.FileURL = "http://192.168.191.1/QRWebService/upload/4.jpg";
            result.FileDescription = "综合管线3D模型-密";
            return result; 
        }

        private string GetQRCode(int OID)
        {
            QrEncoder mEncoder = new QrEncoder(ErrorCorrectionLevel.H);
            QrCode mQrCode = new QrCode();
            mEncoder.TryEncode("嘻嘻哈哈" + OID.ToString(),out mQrCode);
            GraphicsRenderer mRenderer = new GraphicsRenderer(new FixedModuleSize(5, QuietZoneModules.Two), Brushes.Black, Brushes.White);

            string filename ="qrcode_"+ OID.ToString()+".png";
            // filename = Environment.;

            using (FileStream stream = new FileStream(@"..\Data\" + filename, FileMode.OpenOrCreate))
            {
                mRenderer.WriteToStream(mQrCode.Matrix,ImageFormat.Png,stream);
            }

            //using (FileStream fs = new FileStream(@"..Data\temp.png", FileMode.Open))
            //{
            //    byte[] img = new byte[fs.Length];
            //    fs.Read(img, 0, img.Length);
            //    strImg = Convert.ToBase64String(img);
            //    //Transform.BinaryToFile(img,@"C:\temp1.png");
            //}

            return filename;
        }
        #endregion
    }
}