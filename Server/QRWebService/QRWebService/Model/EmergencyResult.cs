using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace QRWebService.Model
{
    public class EmergencyResult
    {
        public string DataType { get; set; }
        public string FileName { get; set; }
        public string FileType { get; set; }
        public string FileURL { get; set; }
        public string FileDescription { get; set; }

        public EmergencyResult() { }

    }
}