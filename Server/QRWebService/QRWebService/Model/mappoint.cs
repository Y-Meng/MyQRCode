using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace QRWebService
{
    public class mappoint
    {
        public double X { get; set; }
        public double Y { get; set; }
        public double Z { get; set; }

        public mappoint() { }

        public mappoint(double x, double y, double z)
        {
            X = x;
            Y = y;
            Z = z;
        }
    }
}