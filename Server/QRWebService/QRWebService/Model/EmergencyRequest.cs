using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace QRWebService.Model
{
    public class EmergencyRequest
    {
        public int Mode;
        public double X;
        public double Y;
        public double Buffer;
        public string QrCode;
        public int isMAP;
        public int isZDM;
        public int isHDM;
        public int is3D;

        public EmergencyRequest() { }     
    }
}