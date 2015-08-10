using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace QRWebService
{
    public class mappoints
    {
        public  List<mappoint> _mappoints { get; set; }
        public mappoints(List<mappoint> mappoints)
        {
            _mappoints = mappoints;
        }
    }
}